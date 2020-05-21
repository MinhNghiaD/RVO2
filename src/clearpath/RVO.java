package clearpath;
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

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
	
	
}
