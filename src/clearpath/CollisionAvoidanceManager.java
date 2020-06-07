package clearpath;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import ec.util.MersenneTwisterFast;

import kdtree.KDNode;
import kdtree.KDTree;
import model.Constants;

public class CollisionAvoidanceManager
{
    CollisionAvoidanceManager(double[] position, 
                              double[] destination,
                              double[] velocity, 
                              double timeHorizon, 
                              double timeStep,                       
                              double maxSpeed, 
                              double neighborDistance,
                              int    maxNeighbors,
                              KDTree tree,
                              int 	 type)
    {
        this.position         = position.clone();
        this.destination      = destination.clone();
        this.velocity         = velocity.clone();
        this.newVelocity      = new Double[2];
        this.newVelocity[0]   = 0.0;
        this.newVelocity[1]   = 0.0;
        this.timeHorizon      = timeHorizon;
        this.timeStep         = timeStep;
        this.maxSpeed         = maxSpeed;
        this.maxNeighbors     = maxNeighbors;
        this.neighborDistance = neighborDistance;

        this.orcaLines        = new ArrayList<Line>();
        this.obstaclesTree    = tree;
        
        this.type			  = type;
    }

    public double[] getPosition() 
    {
        return position.clone();
    }

    public double[] getVelocity() 
    {
        return velocity.clone();
    }
    
    public int getType() {
		return type;
	}

	public void setDestination(double[] goal)
    {
        destination = goal.clone();
    }
    
    public boolean reachedGoal()
    {
        double[] distanceToGoal = RVO.vectorSubstract(position, destination);
        
        final double sqrDistance = RVO.vectorProduct(distanceToGoal, distanceToGoal);
        
        if (sqrDistance > 400) 
        {
            return false;
        }
        
        return true;
    }
    
    public void update(MersenneTwisterFast random)
    {   
        orient(random);
        computeNewVelocity();

        for (int i = 0; i < 2; ++i)
        {
            velocity[i]  = newVelocity[i];
            position[i] += velocity[i] * timeStep;
        }
    }

    private void addNeighborOrcaLine(TreeMap<Double, Vector<KDNode>> neighbors) 
    {
        final double invTimeHorizon = 1 / timeHorizon;

        for (Double key : neighbors.keySet())
        {
            for (KDNode node : neighbors.get(key))
            {
                CollisionAvoidanceManager neighbor = node.getAgent();

                final double[] relativePosition = RVO.vectorSubstract(neighbor.position, position);
                final double[] relativeVelocity = RVO.vectorSubstract(velocity, neighbor.velocity);

                final double distSq = RVO.vectorProduct(relativePosition, relativePosition);
                final double combinedmaxSpeed = maxSpeed + neighbor.maxSpeed;
                final double combinedmaxSpeedSq = Math.pow(combinedmaxSpeed, 2);

                Line     line = new Line(new double[2], new double[2]);
                double[] u    = new double[2];

                if (distSq > combinedmaxSpeedSq)
                {
                    /* No collision. */
                    final double[] w = RVO.vectorSubstract(relativeVelocity,
                            RVO.scalarProduct(relativePosition, invTimeHorizon));
                    /* Vector from cutoff center to relative velocity. */
                    final double wLengthSq = RVO.vectorProduct(w, w);

                    final double dotProduct1 = RVO.vectorProduct(w, relativePosition);

                    if ((dotProduct1 < 0.0f) && (Math.pow(dotProduct1, 2) > (combinedmaxSpeedSq * wLengthSq)))
                    {
                        /* Project on cut-off circle. */
                        final double wLength = Math.sqrt(wLengthSq);
                        final double[] unitW = RVO.scalarProduct(w, 1 / wLength);

                        line.direction[0] = unitW[1];
                        line.direction[1] = -unitW[0];

                        u                 = RVO.scalarProduct(unitW, (combinedmaxSpeed * invTimeHorizon - wLength));
                    } 
                    else
                    {
                        /* Project on legs. */
                        final double leg = Math.sqrt(distSq - combinedmaxSpeedSq);

                        if (RVO.det2D(relativePosition, w) > 0.0f)
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
                    final double[] w = RVO.vectorSubstract(relativeVelocity,
                            RVO.scalarProduct(relativePosition, invTimeStep));

                    final double wLength = Math.sqrt(RVO.vectorProduct(w, w));
                    final double[] unitW = RVO.scalarProduct(w, 1 / wLength);

                    line.direction[0] =  unitW[1];
                    line.direction[1] = -unitW[0];

                    u                 = RVO.scalarProduct(unitW, (combinedmaxSpeed * invTimeStep - wLength));
                }

                line.point[0] = velocity[0] + u[0] * 0.5;
                line.point[1] = velocity[1] + u[1] * 0.5;

                orcaLines.add(line);
            }
        }
    }

    private void computeNewVelocity() 
    {
        orcaLines.clear();
        // TODO addObstacleOrcaLine

        addNeighborOrcaLine(getClosestNeighbors());

        int lineFail = RVO.checkCollision(orcaLines, maxSpeed, preferenceVelocity, false, newVelocity);

        if (lineFail < orcaLines.size())
        {
            // start optimizing from the first collision
            newVelocity = RVO.collisionFreeVelocity(orcaLines, lineFail, maxSpeed, 0, newVelocity);
        }
    }

    private TreeMap<Double, Vector<KDNode>> getClosestNeighbors() 
    {
        double searchRange = Math.pow(neighborDistance, 2);

        return obstaclesTree.getClosestNeighbors(position, searchRange, maxNeighbors);
    }

    // TODO: implement static obstacle and function getClosestObstacles()

    
    private void orient(MersenneTwisterFast random)
    {   
        preferenceVelocity = RVO.vectorSubstract(destination, position);

        double absSqrGoalVector = RVO.vectorProduct(preferenceVelocity, preferenceVelocity);
        
        if (absSqrGoalVector > 1) 
        {
            preferenceVelocity = RVO.scalarProduct(preferenceVelocity, 1/Math.sqrt(absSqrGoalVector));
        }

        /*
         * pivot a little to avoid deadlocks due to perfect symmetry.
         */
        synchronized(random) 
        { 
            // NOTE: don't use another random generator in order to be compatible with MASON
            final double angle    = random.nextGaussian() * 2 * Constants.PI;
            final double distance = random.nextGaussian() * 1;
            
            preferenceVelocity[0] += distance * Math.cos(angle);
            preferenceVelocity[1] += distance * Math.sin(angle);
        }
    }

    private double     timeHorizon;
    private double     maxSpeed;
    private double     neighborDistance;
    private double     timeStep;
    private int        maxNeighbors;

    private double[]   position;
    private double[]   destination;
    private double[]   velocity;
    private Double[]   newVelocity;
    private double[]   preferenceVelocity;

    // constrain lines
    private List<Line> orcaLines;
    private KDTree     obstaclesTree;
    
    // type of agent
    private int		   type;
}
