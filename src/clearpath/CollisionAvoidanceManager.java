package clearpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import kdtree.KDNode;
import kdtree.KDTree;

public class CollisionAvoidanceManager 
{
	CollisionAvoidanceManager(int id, float timeHorizon, KDTree tree)
	{
		this.id 			= id;
		this.timeHorizon 	= timeHorizon;
		this.orcaLines 	 	= new ArrayList<Line>();
		this.obstaclesTree 	= tree;
	}
	
	
	public double[] getPosition()
	{
		return position;
	}

	private void addNeighborOrcaLine(TreeMap<Double, Vector<KDNode> > neighbors)
	{
		final float invTimeHorizon = 1 / timeHorizon;
		
		for (Double key : neighbors.keySet())
		{
			for (KDNode node : neighbors.get(key))
			{
				
			}
		}
		
	}
	
	private int 	id;
	
	private float 	timeHorizon;
	private float  	maxSpeed;
	private int 	maxNeighbors;
	
	private double[] position;
	private double[] velocity;
	private double[] preferenceVelocity;
	
	private List<Line> orcaLines;
	private KDTree obstaclesTree;
	
}
