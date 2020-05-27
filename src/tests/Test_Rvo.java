package tests;

import java.util.Arrays;

public class Test_Rvo 
{
	public static void main(String[] args) 
	{
		Double[] x = new Double[5];
		
		for (int i = 0; i < x.length; ++i)
		{
			x[i] = 0.0;
		}
		
		passDoubleReference(x);
		System.out.println("x = " + Arrays.toString(x));
		
		passDoubleReference(x);
		System.out.println("x = " + Arrays.toString(x));
	}
	
	static private void passDoubleReference(Double[] x)
	{
		for (int i = 0; i < x.length; ++i)
		{
			x[i]++;
		}
	}

}
