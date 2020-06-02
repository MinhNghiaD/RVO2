package tests;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.ParallelSequence;


public class Test_Schedule
{
    public static void main(String[] args) 
    {
        Model model = new Model(System.currentTimeMillis());
        
        model.start();
        
        do
        {
            if (!model.schedule.step(model)) 
                break;
        }
        while(model.schedule.getSteps() < 10);
            
        model.finish();
    }
}

class Model extends SimState
{
    public Model(long seed)
    {
        super(seed);
    }

    @Override
    public void start() 
    {
        super.start();
        
        Controller controller = new Controller();
        schedule.addBefore(controller);
        
        Steppable[] agents = new Steppable[10];
        
        for (int i = 0; i < 10; ++i)
        {
            agents[i] = new Agent(i);
        }
        
        ParallelSequence parellelAgents = new ParallelSequence(agents);
        
        schedule.scheduleRepeating(parellelAgents);
    }
    
    private static final long serialVersionUID = 1L;
}


class Controller implements Steppable
{
    public Controller()
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


class Agent implements Steppable
{
    public Agent(int x)
    {
        super();
        
        var = x;
    }
    
    @Override
    public void step(SimState state) 
    {   
        System.out.println("Agent " + var + "at step: " + state.schedule.getSteps());
    }
    
    private int var;
    private static final long serialVersionUID = 1L;
}