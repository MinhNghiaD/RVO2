package clearpath;
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class RVO 
{
	/**
     * A sufficiently small positive number.
     */
    static final double EPSILON = 0.00001;
	
    static public double vectorProduct(double[] vector1, double[] vector2)
	{
		assert (vector1.length == vector2.length);
		
		double product = 0;
		
		for (int i = 0; i < vector1.length; ++i)
		{
			product += vector1[i] * vector2[i];
		}
		
		return product;
	}
	
	static public double[] vectorSubstract(double[] vector1, double[] vector2)
	{
		assert (vector1.length == vector2.length);
		
		double[] result = new double[vector1.length];
		
		for (int i = 0; i < vector1.length; ++i)
		{
			result[i] = vector1[i] - vector2[i];
		}
		
		return result;
	}
	
	static public double[] vectorSubstract(double[] vector1, Double[] vector2)
	{
		assert (vector1.length == vector2.length);
		
		double[] result = new double[vector1.length];
		
		for (int i = 0; i < vector1.length; ++i)
		{
			result[i] = vector1[i] - vector2[i];
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param vector1
	 * @param vector2
	 * @return determinant of matrix from 2 2D vectors
	 */
	static public double det2D(double[] vector1, double[] vector2)
	{
		assert (vector1.length == 2);
		assert (vector1.length == vector2.length);
		
		return ( (vector1[0] * vector2[1]) - (vector1[1] * vector2[0]) );
	}
	
	static public double[] scalarProduct(double[] vector, double scalar)
	{
		double[] result = vector.clone();
		
		for (int i = 0; i < result.length; ++i)
		{
			result[i] *= scalar;
		}
		
		return result;
	}
	
    /**
     * Solves a one-dimensional linear program on a specified line subject to
     * linear constraints defined by lines and a circular constraint.
     *
     * @param lines                Lines defining the linear constraints.
     * @param lineNo               The specified line constraint.
     * @param optimizationVelocity The optimization velocity.
     * @param directionOptimal     True if the direction should be optimized.
     * @return True if successful.
     */
	static private boolean avoidCollisionWithLine(List<Line> lines, int lineID, double maxSpeed, double[] optimizationVelocity, boolean directionOptimal, Double[] newVelocity)
	{
        final double dotProduct = vectorProduct(lines.get(lineID).point, lines.get(lineID).direction);
        
        final double discriminant = Math.pow(dotProduct, 2) + Math.pow(maxSpeed, 2) - vectorProduct(lines.get(lineID).point, lines.get(lineID).point);

        if (discriminant < 0.0) 
        {
            // Max speed circle fully invalidates line lineID.
            return false;
        }

        final double sqrtDiscriminant = Math.sqrt(discriminant);
        double scalarLeft  = -sqrtDiscriminant - dotProduct;
        double scalarRight =  sqrtDiscriminant - dotProduct;

        // Check if there is any collision between obstacle with lineID and other obstacles before it
        for (int i = 0; i < lineID; ++i) 
        {
            final double denominator = det2D(lines.get(lineID).direction, lines.get(i).direction);
            final double numerator   = det2D(lines.get(i).direction, vectorSubstract(lines.get(lineID).point, lines.get(i).point));

            // Check if is there any objects going in opposite direction
            if (Math.abs(denominator) <= EPSILON) 
            {
                // Lines lineID and i are (almost) parallel.
                if (numerator < 0.0) 
                {
                	// 2 objects go in opposite direction
                    return false;
                }

                continue;
            }

            final double scalar = numerator / denominator;

            if (denominator >= 0.0) 
            {
                // Line i bounds line lineID on the right.
                scalarRight = Math.min(scalarRight, scalar);
            } 
            else 
            {
                // Line i bounds line lineID on the left.
                scalarLeft = Math.max(scalarLeft, scalar);
            }

            if (scalarLeft > scalarRight) 
            {
                return false;
            }
        }

        // Optimize direction.
        if (directionOptimal) 
        {
        	// if optVelocity and lines[lineID].direction on the same direction
            if (vectorProduct(optimizationVelocity, lines.get(lineID).direction) > 0.0) 
            {
                // Take right extreme.
            	for (int j = 0; j < lines.get(lineID).point.length; ++j)
            	{
            		newVelocity[j] = lines.get(lineID).point[j] + (scalarRight * lines.get(lineID).direction[j]);
            	}
            }
            else 
            {
                // Take left extreme.
            	for (int j = 0; j < lines.get(lineID).point.length; ++j)
            	{
            		newVelocity[j] = lines.get(lineID).point[j] + (scalarLeft * lines.get(lineID).direction[j]);
            	}
            }
        } 
        else 
        {
            // Optimize closest point.
            double scalar = vectorProduct(lines.get(lineID).direction, 
            							  vectorSubstract(optimizationVelocity, lines.get(lineID).point));

            scalar = Math.max(scalarLeft, scalar);
            scalar = Math.min(scalarRight, scalar);
            
            for (int j = 0; j < lines.get(lineID).point.length; ++j)
            {
            	newVelocity[j] = lines.get(lineID).point[j] + (scalar * lines.get(lineID).direction[j]);
            }
        }

        return true;
    }
	
	/**
     * Solves a two-dimensional linear program subject to linear constraints
     * defined by lines and a circular constraint.
     *
     * @param lines                Lines defining the linear constraints.
     * @param optimizationVelocity The optimization velocity.
     * @param directionOptimal     True if the direction should be optimized.
     * @return The number of the line on which it fails, or the number of lines if successful.
     */
    static int checkCollision(List<Line> lines, double maxSpeed, double[] optimizationVelocity, boolean directionOptimal, Double[] newVelocity) 
    {
        if (directionOptimal) 
        {
            // Optimize direction. Note that the optimization velocity is of unit length in this case.
        	for (int j = 0; j < newVelocity.length; ++j)
        	{
        		newVelocity[j] = optimizationVelocity[j] * maxSpeed;
        	}
        	
        } 
        else if ( vectorProduct(optimizationVelocity, optimizationVelocity) > Math.pow(maxSpeed, 2) ) 
        {
        	// if optVelocity points to outside the circle => retrive to the closest point outside the circle on the direction of optVelocity
        	double norm = Math.sqrt(vectorProduct(optimizationVelocity, optimizationVelocity));
        	
        	for (int j = 0; j < newVelocity.length; ++j)
        	{
        		newVelocity[j] = optimizationVelocity[j] * (maxSpeed / norm);
        	}
        } 
        else 
        {
            // Optimize closest point and inside circle.
        	for (int j = 0; j < newVelocity.length; ++j)
        	{
        		newVelocity[j] = optimizationVelocity[j];
        	}
        }

        int lineID = 0;
        
        while (lineID < lines.size()) 
        {
        	// Verify Linear constraint
        	if (det2D(lines.get(lineID).direction, vectorSubstract(lines.get(lineID).point, newVelocity)) > 0.0) 
            {
                // There will be collision with lineID, try to compute new velocity to avoid collision
                final Double[] tempResult = newVelocity.clone();
                
                if (! avoidCollisionWithLine(lines, lineID, maxSpeed, optimizationVelocity, directionOptimal, newVelocity)) 
                {
                	// If can not find another result that satisfy an obstacle i => return the id of obstacle where it fails
                    newVelocity = tempResult;

                    break;
                }
            }
        	
        	++lineID;
        }

        return lineID;
    }
    
    /**
     * Solves a two-dimensional linear program subject to linear constraints
     * defined by lines and a circular constraint.
     *
     * @param numObstacleLines Count of obstacle lines.
     * @param beginLine        The line on which the 2-D linear program failed.
     * @param maxSpeed         Limit of speed.
     * @param numObstacleLines Number of obstacles
     * @param velocity		   Current velocity to optimize
     * 
     * @return new velocity
     */
    static Double[] collisionFreeVelocity(List<Line> lines, int beginLine, double maxSpeed, int numObstacleLines, Double[] velocity) 
    {
    	// range of avoidance
        double distance = 0.0;

        for (int i = beginLine; i < lines.size(); ++i) 
        {
        	// verify constraint with obstacle i
            if ( det2D(lines.get(i).direction, (vectorSubstract(lines.get(i).point, velocity))) > distance ) 
            {
                // Result does not satisfy constraint of line i.
            	
            	// Copy lines from 0 to numObstacleLines 
                final List<Line> projectedLines = new ArrayList<>(lines);
                projectedLines.subList(numObstacleLines, projectedLines.size()).clear();

                for (int j = numObstacleLines; j < i; j++) 
                {
                    final double determinant = det2D(lines.get(i).direction, lines.get(j).direction);
                    
                    double[] point = new double[lines.get(0).point.length];

                    // Check if the object going in opposite direction
                    if (Math.abs(determinant) <= EPSILON) 
                    {
                        // Line i and line j are parallel.
                        if (vectorProduct(lines.get(i).direction, lines.get(j).direction) > 0.0) 
                        {
                            // Line i and line j point in the same direction.
                            continue;
                        }

                        // Line i and line j point in opposite direction.
                        // get the middle of 2 points
                        for (int k = 0; k < point.length; ++k)
                        {
                        	point[k] = (lines.get(i).point[k] + lines.get(j).point[k]) * 0.5;
                        }
                    } 
                    else 
                    {
                    	double scalar = det2D(lines.get(j).direction, vectorSubstract(lines.get(i).point, lines.get(j).point)) / determinant;
                    	
                    	for (int k = 0; k < point.length; ++k)
                        {
                        	point[k] = (lines.get(i).point[k] + lines.get(j).direction[k]) * scalar;
                        }
                    }

                    final double[] direction = vectorSubstract(lines.get(j).direction, lines.get(i).direction);
                    
                    double directionNorm = Math.sqrt(vectorProduct(direction, direction));
                    
                    for (int k = 0; k < direction.length; ++k)
                    {
                    	direction[k] /= directionNorm;
                    }

                    projectedLines.add(new Line(point, direction));
                }

                final Double[] tempResult = velocity.clone();
                
                double[] optimizationVelocity = {-lines.get(i).direction[1], lines.get(i).direction[0]};
                
                // Final check if there will be any collision
                if (checkCollision(projectedLines, maxSpeed, optimizationVelocity, true, velocity) < projectedLines.size())
                {
                    // This should in principle not happen. The result is by
                    // definition already in the feasible region of this linear
                    // program. If it fails, it is due to small floating point
                    // error, and the current result is kept.
                    velocity = tempResult;
                }

                distance = det2D(lines.get(i).direction, vectorSubstract(lines.get(i).point, velocity));
            }
        }
        
        return velocity;
    }
}
