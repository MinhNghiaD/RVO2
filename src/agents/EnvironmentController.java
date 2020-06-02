package agents;

import sim.engine.SimState;
import sim.engine.Steppable;

public class EnvironmentController implements Steppable
{
    public EnvironmentController()
    {
        super();
    }
    
    @Override
    public void step(SimState state) 
    {   
        System.out.println("Controller at step: " + state.schedule.getSteps());
    }
    
    private static final long serialVersionUID = 1L;
}