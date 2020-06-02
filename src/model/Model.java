package model;

import model.Constants;

import java.util.Vector;

import agents.*;
import clearpath.CollisionAvoidanceManager;
import clearpath.EnvironmentManager;
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
        
        // TODO create agent to content agentControllers
        
        addRandomAgents();
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

    
    
    
    
    
    
    
    
    private void addRandomAgents()
    {
        for (int i = 0; i < Constants.NUM_AGENT; i++)
        {
            Double2D position   = randomPosition();
            double angle        = this.random.nextInt(360);
            AgentType e         = new AgentPeople(position.x, position.y, angle);
            
            yard.setObjectLocation(e, position);
            schedule.scheduleRepeating(e);
            
            numAgents++;
        }
    }

    private Double2D randomPosition() 
    {
        double x = this.random.nextInt(Constants.GRID_SIZE);
        double y = this.random.nextInt(Constants.GRID_SIZE);
        
        return new Double2D(x, y);
    }
    
    private static final long  serialVersionUID = 1L;
    private Continuous2D       yard             = new Continuous2D(Constants.DISCRETIZATION, Constants.GRID_SIZE, Constants.GRID_SIZE);
    private int                numAgents        = 0;
}
