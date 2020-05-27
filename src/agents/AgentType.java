package agents;

import model.Model;
import sim.engine.Steppable;

public abstract class AgentType implements Steppable 
{

	private static final long serialVersionUID = 1L;
	public double x;
	public double y;
	
	protected Model beings;
	
	public AgentType(double x, double y)
	{
		super();
		this.x = x;
		this.y = y;
	}	

}
