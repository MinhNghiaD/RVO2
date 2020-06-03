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
        controller.update(beings.random);

        double[] position = controller.getPosition();
        x  = position[0];
        y  = position[1];
        
        beings.getYard().setObjectLocation(this, new Double2D(x, y));
    }
    
    public double getAngle()
    {
        double[] velocity = controller.getVelocity();
        
        return Math.atan2(velocity[1], velocity[0]);
    }
    
    private static final long serialVersionUID = 1L;
    private CollisionAvoidanceManager controller;
}
