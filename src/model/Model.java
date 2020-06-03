package model;

import model.Constants;
import agents.*;
import sim.engine.SimState;
import sim.field.continuous.Continuous2D;
import sim.util.Double2D;

public class Model extends SimState 
{
	private static final long serialVersionUID = 1L;
	private Continuous2D yard = new Continuous2D(Constants.DISCRETIZATION,Constants.GRID_SIZE,Constants.GRID_SIZE);
	private int numAgents = 0;
	
	public Model(long seed) 
	{
		super(seed);
		yard.clear();
		numAgents = 0;
	}
	
	@Override
	public void start() 
	{
		super.start();
		yard.clear();
		int scenario = this.random.nextInt(4);
		addRandomAgents(0);
	}
	
	/**
	 * @return the grid
	 */
	public Continuous2D getYard() 
	{
		return yard;
	}
	
	/**
	 * @param yard the grid to set
	 */
	public void setYard(Continuous2D yard) 
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
	
	
	/**
	 * Initialize the agent according to the scenario
	 * Case 0 : The agent moves in a circular orbit
	 * Case 1 : Two groups of opposite agents passing through each other
	 * Case 2 : Fixed obstacles in the middle of the yard
	 * Case 3 : Crossroads
	 * Default : Agent random move
	 * @param nbScenario
	 */
	private void addRandomAgents(int nbScenario) 
	{ 
		switch(nbScenario) {
		case 0:
			addAgentInCircle();
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		default:
			for(int  i  =  0;  i  <  Constants.NUM_AGENT;  i++) 
			{
				Double2D position = randomPosition();
				double angle = this.random.nextInt(360);
				AgentType e = new AgentPeople(position.x, position.y, 1, 1, angle);
				yard.setObjectLocation(e, position);
				schedule.scheduleRepeating(e);
				numAgents++;
			}
		}
	}
	
	private void addAgentInCircle() {
		double radius = (this.random.nextDouble() + 0.1 ) * Constants.GRID_SIZE / 2;
		Double2D center = new Double2D(yard.getWidth()/2, yard.getHeight()/2);
		
		for(int  i  =  0;  i  <  Constants.NUM_AGENT;  i++) 
		{
			Double2D position = randomPosition(radius, center);
			double angle = this.random.nextInt(360);
			AgentType e = new AgentPeople(position.x, position.y, 1, 1, angle);
			yard.setObjectLocation(e, position);
			schedule.scheduleRepeating(e);
			numAgents++;
		}
	}
	
	/**
	 * Create obstacle with position, size and type of obstacle
	 * Case 0 : Square obstacle
	 * Case 1 : Horizontal line
	 * Case 2 : Vertical line
	 * @param nbScenario
	 */
	private void addObstacle(Double2D position, int taille, int type) {
		switch(type) {
		case 0:
			for(int  i  =  0;  i  <=  taille;  i++) 
			{
				for(int  j  =  0;  j  <=  taille;  j++) 
				{
					AgentType obs = new AgentObstacle(position.x+i, position.y+j);
				    yard.setObjectLocation(obs, new Double2D(position.x+i, position.y+j));
				}
			}	
			break;
		case 1:
			for(int  i  =  0;  i  <=  taille;  i++) 
			{
				AgentType obs = new AgentObstacle(position.x+i, position.y);
			    yard.setObjectLocation(obs, new Double2D(position.x+i, position.y));
			}	
			break;
		case 2:
			for(int  i  =  0;  i  <=  taille;  i++) 
			{
				AgentType obs = new AgentObstacle(position.x, position.y+i);
			    yard.setObjectLocation(obs, new Double2D(position.x, position.y+i));
			}	
			break;
		}
	}
	
	private void testObstacles() {
		//Use example for obstacles
		addObstacle(new Double2D(0,0), 10, 0);
		addObstacle(new Double2D(20,10), 10, 1);
		addObstacle(new Double2D(20,10), 10, 2);
	}
		
	private Double2D randomPosition(double radius, Double2D center) {
		double x = 0;
		double y = 0;
		boolean free = false;
		
		while (!free) {
			double theta =  this.random.nextDouble() * 2 * Math.PI;
			x = center.x + radius * Math.cos(theta);
			y = center.y + radius * Math.sin(theta); 
			
			if(yard.numObjectsAtLocation(new Double2D(x,y)) == 0) {
				free = true;
			}
		}
		
		return new Double2D(x,y);
	}


	public Double2D randomPosition() 
	{
		double x = this.random.nextInt(Constants.GRID_SIZE);
		double y = this.random.nextInt(Constants.GRID_SIZE);
		return new Double2D(x, y);
	}
}
