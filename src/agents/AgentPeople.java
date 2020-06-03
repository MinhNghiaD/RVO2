package agents;

import model.Model;
import clearpath.*;
import sim.engine.SimState;
import sim.util.Double2D;


public class AgentPeople extends AgentType
{
    public AgentPeople(CollisionAvoidanceManager controller)
    {
        super(controller.getPosition()[0], controller.getPosition()[1]);
        
        this.controller = controller;
    }
    
    @Override
    public void step(SimState state) 
    {
        Model beings = (Model) state;
        
        move(beings);
    }

    public void move(Model beings) 
    {
        double[] velocity = controller.getVelocity();

        synchronized(beings.random) 
        { 
            // NOTE: don't use another random generator in order to be compatible with MASON
            controller.update(beings.random);
        }

        double[] position = controller.getPosition();

        x  = beings.getYard().stx(position[0]);
        y  = beings.getYard().sty(position[1]);

        beings.getYard().setObjectLocation(this, new Double2D(position[0], position[1]));
        
        this.angle = angleFromDirection(velocity);
    }
    
    public double getAngle()
    {
        return angle;
    }
    

    /**
     * Get radian angle from agent's position
     * 
     * @param y
     * @param x
     * @param currY
     * @param currX
     * @return
     */
    public static Double angleFromDirection(double[] velocity) 
    {
        double res = Math.atan2(velocity[1], velocity[0]);

        return res;
    }
    
    
    private static final long serialVersionUID = 1L;
    private double            angle;
    private CollisionAvoidanceManager controller;
}
