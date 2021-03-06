package agents;

import model.Constants;
import model.Model;

import java.awt.Color;

import clearpath.*;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Double2D;


public class AgentPeople implements Steppable
{
    public AgentPeople(CollisionAvoidanceManager controller)
    {
        //super(controller.getPosition()[0], controller.getPosition()[1]);
        super();
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
        double x  = Constants.adaptXGui(position[0]);
        double y  = Constants.adaptYGui(position[1]);
        
        beings.getYard().setObjectLocation(this, new Double2D(x, y));
    }
    
    public double getAngle()
    {
        double[] velocity = controller.getVelocity();
        
        // NOTE : coordinate of algo has Oy inverse with MASON
        return Math.atan2(-velocity[1], velocity[0]);
    }
    
    public Color getColorType()
    {
    	int type = controller.getType();
    	if (type >= colorType.length || type < 0)
    	{
    		type = 0;
    	}
    	return colorType[type];
    }
    
    private static final long serialVersionUID = 1L;
    private CollisionAvoidanceManager controller;
    
    public static double SCALE =  Constants.SCALE_AGENT;
    
    public static Color [] colorType = 
    {
    	Color.DARK_GRAY,
    	Color.RED,
    	Color.GREEN,
    	Color.BLUE,
    	Color.CYAN,
    	Color.MAGENTA,
    	Color.YELLOW,
    };
}
