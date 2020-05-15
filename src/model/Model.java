package model;

import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;

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
	}
}
