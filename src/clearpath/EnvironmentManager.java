package clearpath;

import kdtree.KDTree;

/**
 * 
 * @author minhnghiaduong class EnvironmentManager : a singleton manage all
 *         collision avoidance agents in a simulation
 */
public class EnvironmentManager
{
    private static EnvironmentManager instance = null;

    public static final EnvironmentManager init(double timeStep,
                                                double neighborDistance, 
                                                int maxNeighbors,
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
        return instance;
    }

    private EnvironmentManager(double timeStep, 
                               double neighborDistance,
                               int maxNeighbors, 
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

    public boolean addAgent(double[] position) 
    {
        CollisionAvoidanceManager agent = new CollisionAvoidanceManager(position, 
                                                                        velocity, 
                                                                        timeHorizon, 
                                                                        timeStep,
                                                                        maxSpeed,
                                                                        neighborDistance,
                                                                        maxNeighbors, 
                                                                        obstaclesTree);

        return obstaclesTree.add(agent);
    }

    public boolean addAgent(double[] position, 
                            double[] velocity, 
                            double timeHorizon, 
                            double timeStep,
                            double maxSpeed,
                            double neighborDistance,
                            int maxNeighbors) 
    {
        CollisionAvoidanceManager agent = new CollisionAvoidanceManager(position, 
                                                                        velocity, 
                                                                        timeHorizon,
                                                                        timeStep,
                                                                        maxSpeed,
                                                                        neighborDistance,
                                                                        maxNeighbors, 
                                                                        obstaclesTree);

        return obstaclesTree.add(agent);
    }

    public void doStep() 
    {
        obstaclesTree.update();

        for (CollisionAvoidanceManager agent : obstaclesTree.getAgents())
        {
            agent.update();
        }

        globalTime += timeStep;
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
