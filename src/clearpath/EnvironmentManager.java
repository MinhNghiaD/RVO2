package clearpath;

import java.util.List;
import java.util.Vector;
import kdtree.KDTreeAgent;
import kdtree.KDTreeObstacle;

/**
 * 
 * @author minhnghiaduong class EnvironmentManager : a singleton manage all
 *         collision avoidance agents in a simulation
 */
public class EnvironmentManager
{
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
    
    public void addObstacles(List<Obstacle> obstacles)
    {
        obstaclesTree = new KDTreeObstacle(obstacles);
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
                                                                        agentsTree,
                                                                        obstaclesTree);

        if(agentsTree.add(agent))
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
                                                                        agentsTree,
                                                                        obstaclesTree);

        if(agentsTree.add(agent))
        {
            return agent;
        }
        
        return null;
    }
    

    public void doStep() 
    {
        agentsTree.update();

        globalTime += timeStep;
        
        //System.out.println("Global controller at " + globalTime);
    }
    
    public double getAgentRadius()
    {
        return radius;
    }
    
    public Vector<CollisionAvoidanceManager> getAgents()
    {
        return agentsTree.getAgents();
    }
    
    // default constructor
    private EnvironmentManager()
    {
        globalTime    = 0;
        timeStep      = 0;
        agentsTree    = new KDTreeAgent(2);
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

        agentsTree            = new KDTreeAgent(2);
    }

    // TODO: queryVisibility with static obstacle
    
    // Default parameters of simulation
    private double   timeStep;
    private double   globalTime;
    private double   neighborDistance;
    private int      maxNeighbors;
    private double   timeHorizon;
    // double timeHorizonObst;
    private double   radius;
    private double   maxSpeed;
    private double[] velocity;

    // Partition agents position in space using KD-Tree
    private KDTreeAgent    agentsTree;
    private KDTreeObstacle obstaclesTree;
    
    private static EnvironmentManager instance = null;
}
