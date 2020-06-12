package agents;

import sim.engine.SimState;
import sim.engine.Steppable;

import java.util.Vector;

import clearpath.CollisionAvoidanceManager;
import clearpath.EnvironmentManager;

public class EnvironmentController implements Steppable
{
    public EnvironmentController()
    {
        super();
        
        // NOTE: EnvironmentManager has to be initiated before being used in this class;
        this.manager = EnvironmentManager.getInstance();
    }
    
    @Override
    public void step(SimState state) 
    {   
        // Simulation finish condition
        if(reachedGoal())
        {
            //System.out.println("Finish !!!");
            state.finish();
            
            return;
        }
        
        //System.out.println("Controller at step: " + state.schedule.getSteps());

        manager.doStep();
    }
    
    public EnvironmentManager getEnvironment()
    {
        return manager;
    }
    
    private boolean reachedGoal()
    {
        Vector<CollisionAvoidanceManager> agents = manager.getAgents();
        
        /* Check if all agents have reached their goals. */
        for (int i = 0; i < agents.size(); ++i) 
        {   
            if (! agents.get(i).reachedGoal()) 
            {
                return false;
            }
        }

        return true;
    }
    
    
    private static final long serialVersionUID = 1L;
    private EnvironmentManager manager;
}