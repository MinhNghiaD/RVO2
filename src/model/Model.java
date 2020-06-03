package model;

import model.Constants;

import java.util.Vector;

import agents.*;
import clearpath.CollisionAvoidanceManager;
import sim.engine.ParallelSequence;
import sim.engine.Sequence;
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
    }

    /**
     * @return the grid
     */
    public Continuous2D getYard()
    {
        return yard;
    }
	
	/**
	 * @param yard the grid to set
	 */
	public void setYard(Continuous2D yard) 
	{
		this.yard = yard;
	}
	
	/**
	 * @return the numAgents
	 */
	public int getNumAgents() 
	{
		return numAgents;
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
            
            Double2D position = new Double2D(agentControllers.get(i).getPosition()[0],
                                             agentControllers.get(i).getPosition()[1]);
            
            yard.setObjectLocation(agents[i], position);
        }
        
        ParallelSequence parellelAgents = new ParallelSequence(agents);
        schedule.scheduleRepeating(parellelAgents);
        
//        Sequence sequenceAgent = new Sequence(agents);
//        schedule.scheduleRepeating(sequenceAgent);
    }
    
    private static final long  serialVersionUID = 1L;
    private Continuous2D       yard             = new Continuous2D(Constants.DISCRETIZATION, Constants.GRID_SIZE, Constants.GRID_SIZE);
    private int                numAgents        = 0;
}
