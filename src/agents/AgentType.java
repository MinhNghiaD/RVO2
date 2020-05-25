package agents;

import model.Model;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Double2D;

public class AgentType implements Steppable 
{

	private static final long serialVersionUID = 1L;
	public double x;
	public double y;
	public double velocity;
	public double radius;
	public double angle;
	
	protected Model beings;
	
	public AgentType(double x, double y)
	{
		super();
		this.x = x;
		this.y = y;
		this.velocity = 1;
		this.radius = 1;
		this.angle = 0;
	}
	
	public AgentType(double x, double y, double velocity, double radius, double angle)
	{
		super();
		this.x = x;
		this.y = y;
		this.velocity = velocity;
		this.radius = radius;
		this.angle = angle;
	}

	@Override
	public void step(SimState state) 
	{
		Model beings = (Model) state; 
		move(beings);
	}
	
	public void move(Model beings) {
		Double2D direction = directionFromAngle(angle);
		x += direction.x;
		y += direction.y;
		x = beings.getYard().stx(x);
		y = beings.getYard().sty(y);
		beings.getYard().setObjectLocation(this, new Double2D(x, y));
	}
	
	/**
	 * Ensure angle is within 0-360 degrees
	 * @param angle
	 */
	public void setAngle(double angle) {
		this.angle = angle % 360;
	}
	
	/**
	 * Turn 360 degrees angle into discrete direction
	 * @param angle in degrees
	 * @return direction in (x,y)
	 */
	public Double2D directionFromAngle(double angle)
	{
		setAngle(angle);
		Double2D [] directions = {
				new Double2D( 0,-1),
				new Double2D( 1,-1),
				new Double2D( 1, 0),
				new Double2D( 1, 1),
				new Double2D( 0, 1),
				new Double2D(-1, 1),
				new Double2D(-1, 0),
				new Double2D(-1,-1),
				new Double2D( 0,-1),
		};
		
		return directions[(int) Math.round(angle/45)];
	}
	
	

}
