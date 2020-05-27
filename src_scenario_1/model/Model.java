package model;

import model.Constants;
import agents.*;
import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import sim.util.Double2D;
import sim.util.Int2D;

public class Model extends SimState 
{
	private static final long serialVersionUID = 1L;
	private SparseGrid2D yard = new SparseGrid2D(Constants.GRID_SIZE,Constants.GRID_SIZE);
	private int numAgents = 0;
	private String type;
	
	public Model(long seed, String type) 
	{
		super(seed);
		yard.clear();
		numAgents = 0;
		addRandomAgents();
		this.type = type;
	}
	
	public Model(long seed) 
	{
		super(seed);
		yard.clear();
		numAgents = 0;
		addRandomAgents();
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
	public SparseGrid2D getYard() 
	{
		return yard;
	}
	
	/**
	 * @param yard the grid to set
	 */
	public void setYard(SparseGrid2D yard) 
	{
		this.yard = yard;
	}
	
	/**
	 * @return the numAgents
	 */
	public int getNumAgents() 
	{
		return numAgents;
	}


	/**
	 * @param numAgents the numAgents to set
	 */
	public void setNumAgents(int numAgents) 
	{
		this.numAgents = numAgents;
	}
	
	public int decrementNumAgents() 
	{
		return this.numAgents--;
	}
	
	private void addRandomAgents() 
	{ 
		for(int  i  =  0;  i  <  Constants.NUM_AGENT;  i++) 
		{
			Int2D position;
			double angle;
			
			if(type != null && type.equals("NormalScene")) 
			{
				position = randomPosition();
				angle = this.random.nextInt(360);	
			}
			else
			{
				position = randomPostionExtremes();
				angle = 0;
			}
			AgentType e = new AgentPeople(position.x, position.y, 1, 1, angle);
			yard.setObjectLocation(e, position);
			schedule.scheduleRepeating(e);
			numAgents++;
		}
	}
	
	public Int2D randomPosition() 
	{
		int x = this.random.nextInt(Constants.GRID_SIZE);
		int y = this.random.nextInt(Constants.GRID_SIZE);
		return new Int2D(x, y);
	}
	
	public Int2D randomPostionExtremes() {
		int x, y;
		int[] extremes = {0, Constants.GRID_SIZE};
		x = this.random.nextInt(Constants.GRID_SIZE);
		
		if(x != 0 || x != Constants.GRID_SIZE)
			y = extremes[this.random.nextInt(extremes.length)];
		else
			y = this.random.nextInt(Constants.GRID_SIZE);
			
		return new Int2D(x, y);
	}
}
