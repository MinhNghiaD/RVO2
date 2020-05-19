package agents;

import model.Model;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;

public class AgentType implements Steppable 
{

	private static final long serialVersionUID = 1L;
	public int x;
	public int y;
	public double velocity;
	public double radius;
	public double angle;
	
	protected Model beings;
	
	public AgentType(int x, int y)
	{
		super();
		this.x = x;
		this.y = y;
		this.velocity = 1;
		this.radius = 1;
		this.angle = 0;
	}
	
	public AgentType(int x, int y, double velocity, double radius, double angle)
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
		Int2D direction = directionFromAngle(angle);
		x += direction.x;
		y += direction.y;
		x = beings.yard.stx(x);
		y = beings.yard.sty(y);
		beings.yard.setObjectLocation(this, x, y);
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
	public Int2D directionFromAngle(double angle)
	{
		setAngle(angle);
		Int2D [] directions = {
				new Int2D( 0,-1),
				new Int2D( 1,-1),
				new Int2D( 1, 0),
				new Int2D( 1, 1),
				new Int2D( 0, 1),
				new Int2D(-1, 1),
				new Int2D(-1, 0),
				new Int2D(-1,-1),
				new Int2D( 0,-1),
		};
		
		return directions[(int) Math.round(angle/45)];
	}
	
	

}
