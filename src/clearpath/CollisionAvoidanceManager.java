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
	CollisionAvoidanceManager(int id, double timeHorizon, double timeStep, KDTree tree)
	{
		this.id 			= id;
		this.timeHorizon 	= timeHorizon;
		this.timeStep		= timeStep;
		this.orcaLines 	 	= new ArrayList<Line>();
		this.obstaclesTree 	= tree;
	}
	
	
	public double[] getPosition()
	{
		return position;
	}
	
	public double[] getVelocity()
	{
		return velocity;
	}

	private void addNeighborOrcaLine(TreeMap<Double, Vector<KDNode> > neighbors)
	{
		final double invTimeHorizon = 1 / timeHorizon;
		
		for (Double key : neighbors.keySet())
		{
			for (KDNode node : neighbors.get(key))
			{
				CollisionAvoidanceManager neighbor = node.getAgent();
				
				final double[] relativePosition = RVO.vectorSubstract(neighbor.position, position);
				final double[] relativeVelocity = RVO.vectorSubstract(velocity, neighbor.velocity);
				
				final double distSq             = RVO.vectorProduct(relativePosition, relativePosition);
				final double combinedmaxSpeed   = maxSpeed + neighbor.maxSpeed;
				final double combinedmaxSpeedSq = Math.pow(combinedmaxSpeed, 2);
				
				Line line  = new Line(new double[2], new double[2]);
				double[] u = new double[2];
				
				if (distSq > combinedmaxSpeedSq)
	            {
	                /* No collision. */
					final double[] w = RVO.vectorSubstract(relativeVelocity, RVO.scalarProduct(relativePosition, invTimeHorizon));
	                /* Vector from cutoff center to relative velocity. */
					final double wLengthSq = RVO.vectorProduct(w, w);
					
					final double dotProduct1 = RVO.vectorProduct(w, relativePosition);
					
					if ( (dotProduct1 < 0.0f) && 
						 (Math.pow(dotProduct1, 2) > (combinedmaxSpeedSq * wLengthSq)) )
	                {
	                    /* Project on cut-off circle. */
						final double wLength = Math.sqrt(wLengthSq);
						final double[] unitW = RVO.scalarProduct(w, 1/wLength);

	                    line.direction[0] =  unitW[1];
	                    line.direction[1] = -unitW[0];

	                    u = RVO.scalarProduct(unitW, (combinedmaxSpeed * invTimeHorizon - wLength));
	                }
					else
					{
						/* Project on legs. */
						final double leg = Math.sqrt(distSq - combinedmaxSpeedSq);
						
						if(RVO.det2D(relativePosition, w) > 0.0f)
	                    {
	                        /* Project on left leg. */
	                        line.direction[0] = (relativePosition[0] * leg - relativePosition[1] * combinedmaxSpeed) / distSq;
	                        line.direction[1] = (relativePosition[1] * leg + relativePosition[0] * combinedmaxSpeed) / distSq;
	                    }
						else
	                    {
	                        /* Project on right leg. */
	                        line.direction[0] = -(relativePosition[0] * leg + relativePosition[1] * combinedmaxSpeed) / distSq;
	                        line.direction[1] = -(relativePosition[1] * leg - relativePosition[0] * combinedmaxSpeed) / distSq;
	                    }
						
						final double dotProduct2 = RVO.vectorProduct(relativeVelocity, line.direction);

	                    u = RVO.vectorSubstract(RVO.scalarProduct(line.direction, dotProduct2), relativeVelocity);
					}	
	            }
				else
	            {
					/* Collision. Project on cut-off circle of time timeStep. */
					final double invTimeStep = 1.0 / timeStep;

	                /* Vector from cutoff center to relative velocity. */
	                final double[] w = RVO.vectorSubstract(relativeVelocity, RVO.scalarProduct(relativePosition, invTimeStep));
	                
	                final double wLength = Math.sqrt(RVO.vectorProduct(w, w));
	                final double[] unitW = RVO.scalarProduct(w, 1/wLength);

	                line.direction[0] =  unitW[1];
	                line.direction[1] = -unitW[0];
	                
	                u = RVO.scalarProduct(unitW, (combinedmaxSpeed * invTimeStep - wLength));
	            }
				
				line.point[0] = velocity[0] + u[0] * 0.5;
				line.point[1] = velocity[1] + u[1] * 0.5;
				
				orcaLines.add(line);
			}
		}
	}
	
	private int 	id;
	
	private double 	timeHorizon;
	private double  maxSpeed;
	private double  timeStep;
	private int 	maxNeighbors;
	
	private double[] position;
	private double[] velocity;
	private double[] preferenceVelocity;
	
	private List<Line> orcaLines;
	private KDTree obstaclesTree;
	
}
