package agents;

import sim.engine.SimState;
import sim.engine.Steppable;

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
        System.out.println("Controller at step: " + state.schedule.getSteps());
        manager.doStep();
    }
    
    
    private static final long serialVersionUID = 1L;
    private EnvironmentManager manager;
}