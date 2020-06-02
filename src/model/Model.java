package model;

import model.Constants;
import agents.*;
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
        // addRandomAgents();
    }

    @Override
    public void start() 
    {
        super.start();
        yard.clear();
        
        addRandomAgents();
    }

    /**
     * @return the grid
     */
    public Continuous2D getYard()
    {
        return yard;
    }
    
    
// NOTE: don't create setter and getter if not necessary
//    /**
//     * @param yard the grid to set
//     */
//    public void setYard(Continuous2D yard)
//    {
//        this.yard = yard;
//    }
//
//    /**
//     * @return the numAgents
//     */
//    public int getNumAgents()
//    {
//        return numAgents;
//    }
//
//    /**
//     * @param numAgents the numAgents to set
//     */
//    public void setNumAgents(int numAgents) 
//    {
//        this.numAgents = numAgents;
//    }

    public int decrementNumAgents() 
    {
        return numAgents--;
    }
    
    private void initEnvironment()
    {
    /*    
        environmentData = EnvironmentManager.init(double timeStep,
                double neighborDistance, 
                int    maxNeighbors,
                double timeHorizon,
                double radius, 
                double maxSpeed,
                double[] velocity)
    */            
    }

    private void addRandomAgents()
    {
        for (int i = 0; i < Constants.NUM_AGENT; i++)
        {
            Double2D position   = randomPosition();
            double angle        = this.random.nextInt(360);
            AgentType e         = new AgentPeople(position.x, position.y, 1, 1, angle);
            
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
    private EnvironmentManager environmentData;
}
