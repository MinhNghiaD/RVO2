package model;

import agents.*;
import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;

public class Model extends SimState 
{
	private static final long serialVersionUID = 1L;
	public SparseGrid2D yard = new SparseGrid2D(Constants.GRID_SIZE,Constants.GRID_SIZE);
	
	public Model(long seed) 
	{
		super(seed);
	}
	
	public void start() 
	{
		super.start();
		yard.clear();
		addRandomAgents();
	}
	
	private void addRandomAgents() { 
		for(int  i  =  0;  i  <  Constants.NUM_AGENT;  i++) {
			Int2D position = randomPosition();
			double angle = this.random.nextInt(360);
			AgentType e = new AgentType(position.x, position.y, 1, 1, angle);
			yard.setObjectLocation(e, position);
			schedule.scheduleRepeating(e);
		}
	}
	
	public Int2D randomPosition() {
		int x = this.random.nextInt(Constants.GRID_SIZE);
		int y = this.random.nextInt(Constants.GRID_SIZE);
		return new Int2D(x, y);
	}
}
