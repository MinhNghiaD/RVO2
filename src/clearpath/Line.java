package clearpath;

/**
 * 
 * @author minhnghiaduong
 * class Line define a line in K-dimension space
 * A line consists of a starting point and a vector point to the destination : end point = start point + direction
 */
public class Line 
{
	Line(double[] point, double[] direction)
	{
		assert (point.length == direction.length);
		
		this.dimension = point.length;
		this.point 	   = point.clone();
		this.direction = direction.clone();
	}
	
	public double[] getPosition()
	{
		return point;
	}
	
	public double[] getVelocity()
	{
		return direction;
	}
	
	
	int dimension;
	double[] point;
	double[] direction;
}
