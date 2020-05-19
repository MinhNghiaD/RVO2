package model;

import agents.AgentPeople;
import model.Constants;
import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;

public class Model extends SimState 
{
	private static final long serialVersionUID = 1L;
	public SparseGrid2D yard = new SparseGrid2D(Constants.GRID_SIZE,Constants.GRID_SIZE);
	private int numAgents = 0;
	
	public Model(long seed) 
	{
		super(seed);
		yard.clear();
		numAgents = 0;
		for(int nbAgent = 0; nbAgent < Constants.NUM_AGENT; ++nbAgent) {
			 int x = this.random.nextInt(Constants.GRID_SIZE);
			 int y = this.random.nextInt(Constants.GRID_SIZE);
			 // TODO: Constructeur d'AgentPeople prenant en paramètre les coordonnées
			 AgentPeople people = new AgentPeople(x, y);
			 yard.setObjectLocation(people, x, y);
			 numAgents++;
		}
	}
	
	public void start() 
	{
		super.start();
	}
	
	/**
	 * @return the grid
	 */
	public SparseGrid2D getYard() {
		return yard;
	}
	
	/**
	 * @param grid the grid to set
	 */
	public void setYard(SparseGrid2D yard) {
		this.yard = yard;
	}
	
	/**
	 * @return the numInsects
	 */
	public int getNumAgents() {
		return numAgents;
	}


	/**
	 * @param numInsects the numInsects to set
	 */
	public void setNumAgents(int numAgents) {
		this.numAgents = numAgents;
	}
	
	public int decrementNumAgents() {
		return this.numAgents--;
	}
}
