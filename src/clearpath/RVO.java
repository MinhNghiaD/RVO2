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
	
	
	
    /**
     * Solves a one-dimensional linear program on a specified line subject to
     * linear constraints defined by lines and a circular constraint.
     *
     * @param lines                Lines defining the linear constraints.
     * @param lineNo               The specified line constraint.
     * @param optimizationVelocity The optimization velocity.
     * @param optimizeDirection    True if the direction should be optimized.
     * @return True if successful.
     */
	private boolean linearProgram1(List<Line> lines, int lineID, double maxSpeed, double[] optimizationVelocity, boolean optimizeDirection, Double[] newVelocity)
	{
        final double dotProduct = Line.vectorProduct(lines.get(lineID).point, lines.get(lineID).direction);
        
        final double discriminant = Math.pow(dotProduct, 2) + Math.pow(maxSpeed, 2) - Line.vectorProduct(lines.get(lineID).point, lines.get(lineID).point);

        if (discriminant < 0.0) 
        {
            // Max speed circle fully invalidates line lineID.
            return false;
        }

        final double sqrtDiscriminant = Math.sqrt(discriminant);
        double tLeft = -sqrtDiscriminant - dotProduct;
        double tRight = sqrtDiscriminant - dotProduct;

        for (int i = 0; i < lineID; ++i) 
        {
            final double denominator = Line.det2D(lines.get(lineID).direction, lines.get(i).direction);
            final double numerator = Line.det2D(lines.get(i).direction, Line.vectorSubstract(lines.get(lineID).point, lines.get(i).point));

            if (Math.abs(denominator) <= EPSILON) 
            {
                // Lines lineID and i are (almost) parallel.
                if (numerator < 0.0) 
                {
                    return false;
                }

                continue;
            }

            final double t = numerator / denominator;

            if (denominator >= 0.0) 
            {
                // Line i bounds line lineID on the right.
                tRight = Math.min(tRight, t);
            } 
            else 
            {
                // Line i bounds line lineID on the left.
                tLeft = Math.max(tLeft, t);
            }

            if (tLeft > tRight) 
            {
                return false;
            }
        }

        if (optimizeDirection) 
        {
            // Optimize direction.
            if (Line.vectorProduct(optimizationVelocity, lines.get(lineID).direction) > 0.0) 
            {
                // Take right extreme.
            	for (int j = 0; j < lines.get(lineID).point.length; ++j)
            	{
            		newVelocity[j] = lines.get(lineID).point[j] + (tRight * lines.get(lineID).direction[j]);
            	}
            }
            else 
            {
                // Take left extreme.
            	for (int j = 0; j < lines.get(lineID).point.length; ++j)
            	{
            		newVelocity[j] = lines.get(lineID).point[j] + (tLeft * lines.get(lineID).direction[j]);
            	}
            }
        } 
        else 
        {
            // Optimize closest point.
            final double t = Line.vectorProduct(lines.get(lineID).direction, 
            									Line.vectorSubstract(optimizationVelocity, lines.get(lineID).point));

            if (t < tLeft) 
            {
            	for (int j = 0; j < lines.get(lineID).point.length; ++j)
            	{
            		newVelocity[j] = lines.get(lineID).point[j] + (tLeft * lines.get(lineID).direction[j]);
            	}
            } 
            else if (t > tRight) 
            {
            	for (int j = 0; j < lines.get(lineID).point.length; ++j)
            	{
            		newVelocity[j] = lines.get(lineID).point[j] + (tRight * lines.get(lineID).direction[j]);
            	}
            } 
            else 
            {
            	for (int j = 0; j < lines.get(lineID).point.length; ++j)
            	{
            		newVelocity[j] = lines.get(lineID).point[j] + (t * lines.get(lineID).direction[j]);
            	}
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
     * @param optimizeDirection    True if the direction should be optimized.
     * @return The number of the line on which it fails, or the number of lines
     * if successful.
     */
    private int linearProgram2(List<Line> lines, double maxSpeed, double[] optimizationVelocity, boolean optimizeDirection, Double[] newVelocity) 
    {
        if (optimizeDirection) 
        {
            // Optimize direction. Note that the optimization velocity is of unit length in this case.
        	for (int j = 0; j < newVelocity.length; ++j)
        	{
        		newVelocity[j] = optimizationVelocity[j] * maxSpeed;
        	}
        	
        } 
        else if ( Line.vectorProduct(optimizationVelocity, optimizationVelocity) > Math.pow(maxSpeed, 2) ) 
        {
            // Optimize closest point and outside circle.
        	double norm = Math.sqrt(Line.vectorProduct(optimizationVelocity, optimizationVelocity));
        	
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

        for (int lineID = 0; lineID < lines.size(); ++lineID) 
        {
            if (Line.det2D(lines.get(lineID).direction, Line.vectorSubstract(lines.get(lineID).point, newVelocity)) > 0.0) 
            {
                // Result does not satisfy constraint i. Compute new optimal
                // result.
                final Double[] tempResult = newVelocity.clone();
                
                if (! linearProgram1(lines, lineID, maxSpeed, optimizationVelocity, optimizeDirection, newVelocity)) 
                {
                    newVelocity = tempResult;

                    return lineID;
                }
            }
        }

        return lines.size();
    }
    
    /**
     * Solves a two-dimensional linear program subject to linear constraints
     * defined by lines and a circular constraint.
     *
     * @param numObstacleLines Count of obstacle lines.
     * @param beginLine        The line on which the 2-D linear program failed.
     */
    private void linearProgram3(List<Line> lines, double maxSpeed, int numObstacleLines, int beginLine, Double[] newVelocity) 
    {
        double distance = 0.0;

        for (int i = beginLine; i < lines.size(); ++i) 
        {
            if ( Line.det2D(lines.get(i).direction, (Line.vectorSubstract(lines.get(i).point, newVelocity))) > distance ) 
            {
                // Result does not satisfy constraint of line i.
            	
            	// Copy lines from 0 to numObstacleLines 
                final List<Line> projectedLines = new ArrayList<>(lines);
                projectedLines.subList(numObstacleLines, projectedLines.size()).clear();

                for (int j = numObstacleLines; j < i; j++) 
                {
                    final double determinant = Line.det2D(lines.get(i).direction, lines.get(j).direction);
                    
                    double[] point = new double[lines.get(0).point.length];

                    if (Math.abs(determinant) <= EPSILON) 
                    {
                        // Line i and line j are parallel.
                        if (Line.vectorProduct(lines.get(i).direction, lines.get(j).direction) > 0.0) 
                        {
                            // Line i and line j point in the same direction.
                            continue;
                        }

                        // Line i and line j point in opposite direction.
                        for (int k = 0; k < point.length; ++k)
                        {
                        	point[k] = (lines.get(i).point[k] + lines.get(j).point[k]) * 0.5;
                        }
                    } 
                    else 
                    {
                    	double scalar = Line.det2D(lines.get(j).direction, Line.vectorSubstract(lines.get(i).point, lines.get(j).point)) / determinant;
                    	
                    	for (int k = 0; k < point.length; ++k)
                        {
                        	point[k] = (lines.get(i).point[k] + lines.get(j).direction[k]) * scalar;
                        }
                    }

                    final double[] direction = Line.vectorSubstract(lines.get(j).direction, lines.get(i).direction);
                    
                    double directionNorm = Math.sqrt(Line.vectorProduct(direction, direction));
                    
                    for (int k = 0; k < direction.length; ++k)
                    {
                    	direction[k] /= directionNorm;
                    }

                    projectedLines.add(new Line(point, direction));
                }

                final Double[] tempResult = newVelocity.clone();
                
                double[] optimizationVelocity = {-lines.get(i).direction[1], lines.get(i).direction[0]};
                
                if (linearProgram2(projectedLines, maxSpeed, optimizationVelocity, true, newVelocity) < projectedLines.size())
                {
                    // This should in principle not happen. The result is by
                    // definition already in the feasible region of this linear
                    // program. If it fails, it is due to small floating point
                    // error, and the current result is kept.
                    newVelocity = tempResult;
                }

                distance = Line.det2D(lines.get(i).direction, Line.vectorSubstract(lines.get(i).point, newVelocity));
            }
        }
    }
}
