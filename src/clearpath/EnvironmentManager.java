package clearpath;

import java.util.Random;
import java.util.Vector;

import kdtree.KDTree;

/**
 * 
 * @author minhnghiaduong class EnvironmentManager : a singleton manage all
 *         collision avoidance agents in a simulation
 */
public class EnvironmentManager
{
    private static EnvironmentManager instance = null;

    // construct environment
    public static final EnvironmentManager init()
    {
        instance = new EnvironmentManager();
        
        return instance;
    }
    
    // construct environment with default parameters for agents
    public static final EnvironmentManager init(double timeStep,
                                                double neighborDistance, 
                                                int    maxNeighbors,
                                                double timeHorizon,
                                                double radius, 
                                                double maxSpeed,
                                                double[] velocity) 
    {
        instance = new EnvironmentManager(timeStep, 
                                          neighborDistance, 
                                          maxNeighbors, 
                                          timeHorizon, 
                                          radius, 
                                          maxSpeed,
                                          velocity);

        return instance;
    }

    public static final EnvironmentManager getInstance() 
    {
        assert (instance != null);
        
        return instance;
    }
    
    // default constructor
    private EnvironmentManager()
    {
        globalTime    = 0;
        timeStep      = 0;
        obstaclesTree = new KDTree(2);
    }

    private EnvironmentManager(double timeStep, 
                               double neighborDistance,
                               int    maxNeighbors, 
                               double timeHorizon,
                               double radius,
                               double maxSpeed, 
                               double[] velocity)
    {
        this.timeStep         = timeStep;
        this.globalTime       = 0;
        this.neighborDistance = neighborDistance;
        this.maxNeighbors     = maxNeighbors;
        this.timeHorizon      = timeHorizon;
        this.radius           = radius;
        this.maxSpeed         = maxSpeed;
        this.velocity         = velocity.clone();

        obstaclesTree         = new KDTree(2);

    }
    
    public void setTimeStep(double period)
    {
        timeStep = period;
    }

    public CollisionAvoidanceManager addAgent(double[] position, double[] destination) 
    {
        CollisionAvoidanceManager agent = new CollisionAvoidanceManager(position, 
                                                                        destination,
                                                                        velocity, 
                                                                        timeHorizon, 
                                                                        timeStep,
                                                                        maxSpeed,
                                                                        neighborDistance,
                                                                        maxNeighbors, 
                                                                        obstaclesTree);

        if(obstaclesTree.add(agent))
        {
            return agent;
        }
        
        return null;
    }

    public CollisionAvoidanceManager addAgent(double[] position, 
                                              double[] destination,
                                              double[] velocity, 
                                              double timeHorizon,
                                              double timeStep,
                                              double maxSpeed,
                                              double neighborDistance,
                                              int    maxNeighbors) 
    {
        CollisionAvoidanceManager agent = new CollisionAvoidanceManager(position, 
                                                                        destination,
                                                                        velocity, 
                                                                        timeHorizon,
                                                                        timeStep,
                                                                        maxSpeed,
                                                                        neighborDistance,
                                                                        maxNeighbors, 
                                                                        obstaclesTree);

        if(obstaclesTree.add(agent))
        {
            return agent;
        }
        
        return null;
    }

    public void doStep() 
    {
        obstaclesTree.update();
/*
        for (CollisionAvoidanceManager agent : obstaclesTree.getAgents())
        {
            agent.update();
        }
*/
        globalTime += timeStep;
        
        //System.out.println("Global controller at " + globalTime);
    }
    
    public Vector<CollisionAvoidanceManager> getAgents()
    {
        return obstaclesTree.getAgents();
    }

    // TODO: queryVisibility with static obstacle

    // Default parameters of simulation
    double         timeStep;
    double         globalTime;
    double         neighborDistance;
    int            maxNeighbors;
    double         timeHorizon;
    // double timeHorizonObst;
    double         radius;
    double         maxSpeed;
    double[]       velocity;

    // Partition agents position in space using KD-Tree
    private KDTree obstaclesTree;
}
