package model;

import model.Constants;

import java.util.Vector;
import java.util.List;

import agents.*;
import clearpath.CollisionAvoidanceManager;
import clearpath.Obstacle;
import clearpath.ObstacleVertex;
import sim.engine.ParallelSequence;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.continuous.Continuous2D;
import sim.util.Double2D;

public class Model extends SimState 
{
    public Model(long seed) 
    {
        super(seed);
        yard.clear();
        numAgents = 0;
    }

    @Override
    public void start()
    {
        super.start();
        yard.clear();

        // schedule EnvironmentController execution at before every step
        EnvironmentController envController = new EnvironmentController();
        schedule.addBefore(envController);

        Vector<CollisionAvoidanceManager> agentControllers = envController.getEnvironment().getAgents();

        addAgents(agentControllers);

        List<Obstacle> obstacles = envController.getEnvironment().getObstacles();
        
        for (Obstacle obtacle : obstacles)
        {
            addObstacles(obtacle);
        }

    }

    /**
     * @return the grid
     */
    public Continuous2D getYard() 
    {
        return yard;
    }

    public int decrementNumAgents() 
    {
        return numAgents--;
    }

    private void addAgents(Vector<CollisionAvoidanceManager> agentControllers) 
    {
        if (agentControllers == null || agentControllers.size() == 0) 
        {
            System.out.println("agents controllers are null");
            return;
        }

        numAgents = agentControllers.size();

        Steppable[] agents = new Steppable[numAgents];

        for (int i = 0; i < numAgents; ++i) 
        {
            agents[i] = new AgentPeople(agentControllers.get(i));

            Double2D position = new Double2D(Constants.adaptXGui(agentControllers.get(i).getPosition()[0]),
                                             Constants.adaptYGui(agentControllers.get(i).getPosition()[1]) );

            yard.setObjectLocation(agents[i], position);
        }

        ParallelSequence parellelAgents = new ParallelSequence(agents);
        schedule.scheduleRepeating(parellelAgents);

        // Sequence sequenceAgent = new Sequence(agents);
        // schedule.scheduleRepeating(sequenceAgent);
    }

    private void addObstacles(Obstacle obstacle) 
    {
        List<ObstacleVertex> vertices = obstacle.getVertices();
        
        for (int i = 0; i < vertices.size(); ++i)
        {
            ObstacleVertex vertex     = vertices.get(i);
            ObstacleVertex nextVertex = vertex.nextVertex;
            
            if (vertex.unitDirection()[0] == 0)
            {
                double y = vertex.position()[1];
                double direction = (vertex.unitDirection()[1] / Math.abs(vertex.unitDirection()[1]));
                
                while (true)
                {
                    if ( (direction > 0 && y > nextVertex.position()[1]) ||
                         (direction < 0 && y < nextVertex.position()[1]) )
                    {
                        break;
                    }
                    
                    yard.setObjectLocation(new Object(), new Double2D(Constants.adaptXGui(vertex.position()[0]), 
                                                                      Constants.adaptYGui(y)));
                    
                    y += direction;
                }
            }
            else
            {
                double tan = vertex.unitDirection()[1] / vertex.unitDirection()[0];
                double direction = (vertex.unitDirection()[0] / Math.abs(vertex.unitDirection()[0]));
                
                double x = vertex.position()[0];
                
                while (true)
                {
                    if ( (direction > 0 && x > nextVertex.position()[0]) ||
                         (direction < 0 && x < nextVertex.position()[0]) )
                    {
                        break;
                    }
                    
                    yard.setObjectLocation(new Object(), new Double2D(Constants.adaptXGui(x), 
                                                                      Constants.adaptYGui(nextVertex.position()[1] + (x - vertex.position()[0]) * tan)));
                    
                    x += direction;
                }
                
            }
        }
    }
    
    private static final long serialVersionUID = 1L;
    private Continuous2D yard = new Continuous2D(Constants.DISCRETIZATION, Constants.GRID_SIZE, Constants.GRID_SIZE);
    private int numAgents = 0;
}
