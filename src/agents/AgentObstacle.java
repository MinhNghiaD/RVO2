package agents;

import model.Model;
import clearpath.*;
import sim.engine.SimState;
import sim.util.Double2D;

public class AgentObstacle extends AgentType 
{
	private static final long serialVersionUID = 1L;

	public AgentObstacle(double x, double y) 
	{
		super(x, y);
	}
	
	@Override
	public void step(SimState state) 
	{
		Model beings = (Model) state; 
	}

}
