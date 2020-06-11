package clearpath;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import ec.util.MersenneTwisterFast;

import kdtree.KDNodeAgent;
import kdtree.KDNodeObstacle;
import kdtree.KDTreeAgent;
import kdtree.KDTreeObstacle;
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
                              KDTreeAgent agentTree,
                              KDTreeObstacle obstacleTree)
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
        this.agentsTree       = agentTree;
        this.obstaclesTree    = obstacleTree;
    }

    public double[] getPosition() 
    {
        return position.clone();
    }

    public double[] getVelocity() 
    {
        return velocity.clone();
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
    
    private void computeNewVelocity() 
    {
        orcaLines.clear();
        addObstacleOrcaLine(getClosestObstacle());
        addNeighborOrcaLine(getClosestNeighbors());

        int lineFail = RVO.checkCollision(orcaLines, maxSpeed, preferenceVelocity, false, newVelocity);

        if (lineFail < orcaLines.size())
        {
            // start optimizing from the first collision
            newVelocity = RVO.collisionFreeVelocity(orcaLines, lineFail, maxSpeed, 0, newVelocity);
        }
    }

    private TreeMap<Double, Vector<KDNodeAgent>> getClosestNeighbors() 
    {
        double searchRange = Math.pow(neighborDistance, 2);

        return agentsTree.getClosestNeighbors(position, searchRange, maxNeighbors);
    }

    private TreeMap<Double, Vector<KDNodeObstacle>> getClosestObstacle() 
    {
        double searchRange = Math.pow((timeHorizon * maxSpeed + maxSpeed), 2);

        if (obstaclesTree == null)
        {
            return new TreeMap<Double, Vector<KDNodeObstacle>>();
        }
        
        return obstaclesTree.getClosestObstacles(position, searchRange);
    }
    
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

    private void addNeighborOrcaLine(TreeMap<Double, Vector<KDNodeAgent>> neighbors) 
    {
        final double invTimeHorizon = 1 / timeHorizon;

        for (Double key : neighbors.keySet())
        {
            for (KDNodeAgent node : neighbors.get(key))
            {
                CollisionAvoidanceManager neighbor = node.getAgent();

                final double[] relativePosition = RVO.vectorSubstract(neighbor.position, position);
                final double[] relativeVelocity = RVO.vectorSubstract(velocity, neighbor.velocity);

                final double distSq = RVO.vectorProduct(relativePosition, relativePosition);
                final double combinedmaxSpeed   = maxSpeed + neighbor.maxSpeed;
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

                        u  = RVO.scalarProduct(unitW, (combinedmaxSpeed * invTimeHorizon - wLength));
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

                    u = RVO.scalarProduct(unitW, (combinedmaxSpeed * invTimeStep - wLength));
                }

                line.point[0] = velocity[0] + u[0] * 0.5;
                line.point[1] = velocity[1] + u[1] * 0.5;

                orcaLines.add(line);
            }
        }
    }
    
    private void addObstacleOrcaLine(TreeMap<Double, Vector<KDNodeObstacle>> neighbors) 
    {
        for (Double key : neighbors.keySet())
        {
            for (KDNodeObstacle node : neighbors.get(key))
            {
                Line line = obstacleOrcaLine(node.getVertex());
                
                if (line != null)
                {
                    orcaLines.add(line);
                }
            }
        }
    }
    
    private Line obstacleOrcaLine(ObstacleVertex vertex)
    {
        final double invTimeHorizon = 1 / timeHorizon;
        
        ObstacleVertex nextVertex = vertex.nextVertex;

        final double[] relativePosition1 = RVO.vectorSubstract(vertex.position, position);
        final double[] relativePosition2 = RVO.vectorSubstract(nextVertex.position, position);
        
        if (obstacleAlreadyScanned(invTimeHorizon, relativePosition1, relativePosition2))
        {
            return null;
        }
        
        // Check for collisions
        final double distSq1 = RVO.vectorProduct(relativePosition1, relativePosition1);
        final double distSq2 = RVO.vectorProduct(relativePosition2, relativePosition2);

        final double maxSpeedSq = Math.pow(maxSpeed, 2);

        final double[] obstacleVector = RVO.vectorSubstract(nextVertex.position, vertex.position);
        
        final double scalar = RVO.vectorProduct(RVO.scalarProduct(relativePosition1, -1), obstacleVector) / 
                              RVO.vectorProduct(obstacleVector, obstacleVector);
        
        final double[] orthogonalVector = RVO.vectorSubstract(RVO.scalarProduct(relativePosition1, -1), 
                                          RVO.scalarProduct(obstacleVector, scalar));
        
        final double distSqLine = RVO.vectorProduct(orthogonalVector, orthogonalVector);

        if (scalar < 0 && distSq1 <= maxSpeedSq)
        {
            /* Collision with left vertex. Ignore if non-convex. */
            if (vertex.isConvex)
            {
                double[] point = {0, 0};
                double[] direction = new double[2];
                
                direction[0] = -relativePosition1[1] / Math.sqrt(RVO.vectorProduct(relativePosition1, relativePosition1));
                direction[1] =  relativePosition1[0] / Math.sqrt(RVO.vectorProduct(relativePosition1, relativePosition1));
                
                return new Line(point, direction);
            }

            return null;
        }
        else if (scalar > 1 && distSq2 <= maxSpeedSq)
        {
            /* 
             * Collision with right vertex. Ignore if non-convex
             * or if it will be taken care of by neighbor obstacle
             */
            if (nextVertex.isConvex && RVO.det2D(relativePosition2, nextVertex.unitDirection) >= 0)
            {
                double[] point = {0, 0};
                double[] direction = new double[2];
                
                direction[0] = -relativePosition2[1] / Math.sqrt(RVO.vectorProduct(relativePosition2, relativePosition2));
                direction[1] =  relativePosition2[0] / Math.sqrt(RVO.vectorProduct(relativePosition2, relativePosition2));

                return new Line(point, direction);
            }

            return null;
        }
        else if (scalar >= 0 && scalar < 1 && distSqLine <= maxSpeedSq)
        {
            /* Collision with obstacle segment. */
            double[] point = {0, 0};
            double[] direction = RVO.scalarProduct(vertex.unitDirection, -1);

            return new Line(point, direction);
        }
        
        /*
         * No collision.
         * Compute legs. When obliquely viewed, both legs can come from a single
         * vertex. Legs extend cut-off line when non-convex vertex.
         */
        double[] leftLegDirection = new double[2];
        double[] rightLegDirection = new double[2];
        
        if (scalar < 0 && distSqLine <= maxSpeedSq)
        {
            // Obstacle viewed obliquely so that left vertex defines velocity obstacle.
            if (! vertex.isConvex)
            {
                return null;
            }

            nextVertex = vertex;

            final double leg1 = Math.sqrt(distSq1 - maxSpeedSq);
            
            leftLegDirection[0] = (relativePosition1[0] * leg1 - relativePosition1[1] * maxSpeed) / distSq1;
            leftLegDirection[1] = (relativePosition1[0] * maxSpeed + relativePosition1[1] * leg1) / distSq1;
            
            rightLegDirection[0] = (relativePosition1[0] * leg1 + relativePosition1[1] * maxSpeed) / distSq1;
            rightLegDirection[1] = (-relativePosition1[0] * maxSpeed + relativePosition1[1] * leg1) / distSq1;
        }
        else if (scalar > 1 && distSqLine <= maxSpeedSq)
        {
            // Obstacle viewed obliquely so that right vertex defines velocity obstacle.
            if (! nextVertex.isConvex)
            {
                return null;
            }

            vertex = nextVertex;

            final double leg2 = Math.sqrt(distSq2 - maxSpeedSq);
            
            leftLegDirection[0] = (relativePosition2[0] * leg2 - relativePosition2[1] * maxSpeed) / distSq2;
            leftLegDirection[1] = (relativePosition2[0] * maxSpeed + relativePosition2[1] * leg2) / distSq2;
            
            rightLegDirection[0] = (relativePosition2[0] * leg2 + relativePosition2[1] * maxSpeed) / distSq2;
            rightLegDirection[1] = (-relativePosition2[0] * maxSpeed + relativePosition2[1] * leg2) / distSq2;
        }
        else
        {
            // Usual situation. 
            if (vertex.isConvex)
            {
                final double leg1 = Math.sqrt(distSq1 - maxSpeedSq);
                
                leftLegDirection[0] = (relativePosition1[0] * leg1 - relativePosition1[1] * maxSpeed) / distSq1;
                leftLegDirection[1] = (relativePosition1[0] * maxSpeed + relativePosition1[1] * leg1) / distSq1;
            }
            else
            {
                /* Left vertex non-convex; left leg extends cut-off line. */
                leftLegDirection = RVO.scalarProduct(vertex.unitDirection, -1);
            }

            if (nextVertex.isConvex)
            {
                final double leg2 = Math.sqrt(distSq2 - maxSpeedSq);
                
                rightLegDirection[0] = (relativePosition2[0] * leg2 + relativePosition2[1] * maxSpeed) / distSq2;
                rightLegDirection[1] = (-relativePosition2[0] * maxSpeed + relativePosition2[1] * leg2) / distSq2;
            }
            else
            {
                /* Right vertex non-convex; right leg extends cut-off line. */
                rightLegDirection = vertex.unitDirection.clone();
            }
        }
        
        /*
         * Legs can never point into neighboring edge when convex vertex,
         * take cutoff-line of neighboring edge instead. If velocity projected on
         * "foreign" leg, no constraint is added.
         */
        ObstacleVertex previousVertex = vertex.previousVertex;
        
        boolean isLeftLegForeign  = false;
        boolean isRightLegForeign = false;
        
        if (vertex.isConvex && RVO.det2D(leftLegDirection, RVO.scalarProduct(previousVertex.unitDirection, -1)) >= 0)
        {
            /* Left leg points into obstacle. */
            leftLegDirection = RVO.scalarProduct(previousVertex.unitDirection, -1);
            isLeftLegForeign = true;
        }
        
        if (nextVertex.isConvex && RVO.det2D(rightLegDirection, nextVertex.unitDirection) <= 0)
        {
            /* Right leg points into obstacle. */
            rightLegDirection = nextVertex.unitDirection.clone();
            isRightLegForeign = true;
        }
        
        /* Compute cut-off centers. */
        final double[] leftCutoff  = RVO.scalarProduct(RVO.vectorSubstract(vertex.position, position), invTimeHorizon);
        final double[] rightCutoff = RVO.scalarProduct(RVO.vectorSubstract(nextVertex.position, position), invTimeHorizon);
        final double[] cutoffVec   = RVO.vectorSubstract(rightCutoff, leftCutoff);
        
        /* Project current velocity on velocity obstacle. */
        
        final double tLeft  = RVO.vectorProduct(RVO.vectorSubstract(velocity, leftCutoff), leftLegDirection);
        final double tRight = RVO.vectorProduct(RVO.vectorSubstract(velocity, rightCutoff), rightLegDirection);
        
        double t;
        if (vertex == nextVertex)
        {
            t = 0.5;
        }
        else
        {
            t = RVO.vectorProduct(RVO.vectorSubstract(velocity, leftCutoff), cutoffVec) / 
                RVO.vectorProduct(cutoffVec, cutoffVec);
        }

        /* Check if current velocity is projected on cutoff circles. */
        if ((t < 0 && tLeft < 0) ||
            (vertex == nextVertex && tLeft < 0 && tRight < 0))
        {
            /* Project on left cut-off circle. */
            double[] projection = RVO.vectorSubstract(velocity, leftCutoff);

            final double[] unitW = RVO.scalarProduct(projection, 
                                                     Math.sqrt(RVO.vectorProduct(projection, projection)));

            double[] point     = new double[unitW.length];
            double[] direction = new double[unitW.length];

            for (int i = 0; i < point.length; ++i)
            {
                point[i] = leftCutoff[i] + maxSpeed * invTimeHorizon * unitW[i];
            }

            direction[0] =  unitW[1];
            direction[1] = -unitW[0];

            return new Line(point, direction);
        }
        else if (t > 1 && tRight < 0)
        {
            /* Project on right cut-off circle. */
            double[] projection = RVO.vectorSubstract(velocity, rightCutoff);
            
            final double[] unitW = RVO.scalarProduct(projection, 
                                                     Math.sqrt(RVO.vectorProduct(projection, projection)));

            double[] point     = new double[unitW.length];
            double[] direction = new double[unitW.length];

            for (int i = 0; i < point.length; ++i)
            {
                point[i] = rightCutoff[i] + maxSpeed * invTimeHorizon * unitW[i];
            }
            
            direction[0] =  unitW[1];
            direction[1] = -unitW[0];
            
            return new Line(point, direction);
        }
        
        /*
         * Project on left leg, right leg, or cut-off line, whichever is closest
         * to velocity.
         */
        double distSqCutoff, distSqLeft, distSqRight;
        
        if (t < 0 || t > 1 || vertex == nextVertex)
        {
            distSqCutoff = Double.MAX_VALUE;
        }
        else
        {
            double[] cutOff = new double[velocity.length];
            
            for (int i = 0; i < cutOff.length; ++i)
            {
                cutOff[i] = velocity[i] - (leftCutoff[i] + t * cutoffVec[i]);
            }
            
            distSqCutoff = RVO.vectorProduct(cutOff, cutOff);
        }
        
        if (tLeft < 0)
        {
            distSqLeft = Double.MAX_VALUE;
        }
        else
        {
            double[] left = new double[velocity.length];
            
            for (int i = 0; i < left.length; ++i)
            {
                left[i] = velocity[i] - (leftCutoff[i] + tLeft * leftLegDirection[i]);
            }
            
            distSqLeft = RVO.vectorProduct(left, left);
        }
        
        if (tRight < 0)
        {
            distSqRight = Double.MAX_VALUE;
        }
        else
        {
            double[] right = new double[velocity.length];
            
            for (int i = 0; i < right.length; ++i)
            {
                right[i] = velocity[i] - (rightCutoff[i] + tRight * rightLegDirection[i]);
            }
            
            distSqRight = RVO.vectorProduct(right, right);
        }

        if (distSqCutoff <= distSqLeft && distSqCutoff <= distSqRight)
        {
            /* Project on cut-off line. */
            double[] direction = RVO.scalarProduct(vertex.unitDirection, -1);
            double[] point = new double[2];
            
            point[0] = leftCutoff[0] - (maxSpeed * invTimeHorizon * direction[1]);
            point[1] = leftCutoff[1] + (maxSpeed * invTimeHorizon * direction[0]);

            return new Line(point, direction);
        }
        else if (distSqLeft <= distSqRight)
        {
            /* Project on left leg. */
            if (isLeftLegForeign)
            {
                return null;
            }

            double[] point = new double[2];
            point[0] = leftCutoff[0] - (maxSpeed * invTimeHorizon * leftLegDirection[1]);
            point[1] = leftCutoff[1] + (maxSpeed * invTimeHorizon * leftLegDirection[0]);

            return new Line(point, leftLegDirection);
        }
        
        /* Project on right leg. */
        if (isRightLegForeign)
        {
            return null;
        }

        double[] direction = RVO.scalarProduct(rightLegDirection, -1);
        double[] point = new double[2];
        point[0] = rightCutoff[0] - (maxSpeed * invTimeHorizon * direction[1]);
        point[1] = rightCutoff[1] + (maxSpeed * invTimeHorizon * direction[0]);
        
        return new Line(point, direction);
    }
    
    private boolean obstacleAlreadyScanned(double invTimeHorizon, double[] relativePosition1, double[] relativePosition2)
    {
        for (int i = 0; i < orcaLines.size(); ++i)
        {
            double weight1 = RVO.det2D(RVO.vectorSubstract(RVO.scalarProduct(relativePosition1, invTimeHorizon), orcaLines.get(i).point), 
                                       orcaLines.get(i).direction) - (invTimeHorizon*maxSpeed);
            
            double weight2 = RVO.det2D(RVO.vectorSubstract(RVO.scalarProduct(relativePosition2, invTimeHorizon), orcaLines.get(i).point), 
                                       orcaLines.get(i).direction) - (invTimeHorizon*maxSpeed);
            
            if (weight1 >= -RVO.EPSILON && weight2 >= -RVO.EPSILON)
            {
                return true;
            }
        }
        
        return false;
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
    private List<Line>     orcaLines;
    private KDTreeAgent    agentsTree;
    private KDTreeObstacle obstaclesTree;
}
