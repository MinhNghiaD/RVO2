package agents;

import model.Model;
import clearpath.*;
import sim.engine.SimState;
import sim.util.Double2D;

public class AgentPeople extends AgentType 
{
	private static final long serialVersionUID = 1L;
	public double velocity;
	public double radius;
	public double angle;
	public Line line;

	public AgentPeople(double x, double y) 
	{
		super(x, y);
	}

	public AgentPeople(double x, double y, double velocity, double radius, double angle)
	{
		super(x, y);
		this.velocity = velocity;
		this.radius = radius;
		this.angle = getRadianAngle(angle);
		
		Double2D direction2D = directionFromAngle(angle);
		
		double[] point = {x,y};
		double[] direction = {direction2D.x, direction2D.y};
		this.line = new Line(point, direction);
	}
	

	public AgentPeople(double sourceX, double sourceY, double velocity, double radius, double directionX, double directionY)
	{
		super(sourceX, sourceY);
		this.velocity = velocity;
		this.radius = radius;
		
		double[] point = {sourceX, sourceY};
		double[] direction = {directionX, directionY};
		this.line = new Line(point, direction);
		
	}
	
	@Override
	public void step(SimState state) 
	{
		Model beings = (Model) state; 
		move(beings);
	}
	
	public void move(Model beings) 
	{
		double currX = this.x;
		double currY = this.y;
		
		x += this.line.getDirection()[0];
		y += this.line.getDirection()[1];
		
		x = beings.getYard().stx(x);
		y = beings.getYard().sty(y);
		
		beings.getYard().setObjectLocation(this, new Double2D(x, y));
		this.angle = angleFromDirection(y, x, currY, currX);
	}
	
	/**
	 * Ensure angle is within 0-360 degrees
	 * @param angle
	 * @return modulated angle
	 */
	public double clampAngle(double angle) 
	{
		return angle % 360;
	}
	
	/**
	 * Degrees to radians angle
	 * @param angle
	 * @return
	 */
	private double getRadianAngle(double angle) {
		return clampAngle(angle)/(2*3.14);
	}
	
	
	/**
	 * Turn 360 degrees angle into discrete direction
	 * @param angle in degrees
	 * @return direction in (x,y)
	 */
	public Double2D directionFromAngle(double angle)
	{
		angle = clampAngle(angle);
		Double2D[] directions = {
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
		
		return directions[(int)Math.round(angle/45)];
	}
	
	/**
	 * Get radian angle from agent's position
	 * @param y
	 * @param x
	 * @param currY
	 * @param currX
	 * @return
	 */
	public Double angleFromDirection(double y, double x, double currY, double currX) {
		double res = Math.atan2(y-currY, x-currX);
		
		return res;
	}

}
