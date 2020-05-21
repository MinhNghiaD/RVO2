package tests;

import java.util.Arrays;
import java.util.TreeMap;

import kdtree.*;

public class Test_KDTree {

	public static void main(String[] args) 
	{
		KDTree tree = new KDTree(2);
		
		double[] position1 = {11, 2};
		tree.add(position1);
		
		double[] position2 = {2, 12};
		tree.add(position2);
		
		double[] position3 = {3, 12};
		tree.add(position3);
		
		double[] position4 = {25, 0};
		tree.add(position4);
		
		double[] position5 = {22, 5};
		tree.add(position5);
		
		double[] position6 = {12, 1};
		tree.add(position6);
		
		double[] position7 = {11, 4};
		tree.add(position7);
		
		double[] position8 = {2, 6};
		tree.add(position8);
		
		
		double[] currentPosition = {0, 0};
		TreeMap<Double, KDNode> closestNodes = tree.getClosestNeighbors(currentPosition, Double.MAX_VALUE, 2);
		
		System.out.println("Closest neighbors to " + Arrays.toString(currentPosition) + ": ");
				
		for (Double key : closestNodes.keySet())
		{
			System.out.println(Arrays.toString(closestNodes.get(key).getPosition()) + ", distance = " + key);
		}
   
	}

}
