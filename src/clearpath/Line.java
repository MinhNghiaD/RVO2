package clearpath;

/**
 * 
 * @author minhnghiaduong
 * class Line define a line in K-dimension space
 * A line consists of a starting point and a vector point to the destination : end point = start point + direction
 */
public class Line 
{
	public Line(double[] point, double[] direction)
	{
		assert (point.length == direction.length);
		
		this.dimension = point.length;
		this.point 	   = point.clone();
		this.direction = direction.clone();
	}
	
	
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
	 * @return determinant of matrice from 2 2D vectors
	 */
	static public double det2D(double[] vector1, double[] vector2)
	{
		assert (vector1.length == 2);
		assert (vector1.length == vector2.length);
		
		return ( (vector1[0] * vector2[1]) - (vector1[1] * vector2[0]) );
	}
	
	public double[] getPosition()
	{
		return point;
	}
	
	public double[] getDirection()
	{
		return direction;
	}
	
	
	int dimension;
	double[] point;
	double[] direction;
}
