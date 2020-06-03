package model;

import model.Constants;

import java.util.Vector;
import java.util.ArrayList;

import agents.*;
import clearpath.CollisionAvoidanceManager;
import sim.engine.ParallelSequence;
import sim.engine.Sequence;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.continuous.Continuous2D;
import sim.util.Double2D;

public class Model extends SimState {
	public Model(long seed) {
		super(seed);
		yard.clear();
		numAgents = 0;
	}

	@Override
	public void start() {
		super.start();
		yard.clear();

		// schedule EnvironmentController execution at before every step
		EnvironmentController envController = new EnvironmentController();
		schedule.addBefore(envController);

		Vector<CollisionAvoidanceManager> agentControllers = envController.getEnvironment().getAgents();

		addAgents(agentControllers);

		// TODO create Vector<CollisionAvoidanceManager> obstaclesControllers
		for (Obstacle obs : this.obstacles) {
			addObstacles(obs.getPosition(), obs.getTaille(), obs.getType());
		}

	}

	/**
	 * @return the grid
	 */
	public Continuous2D getYard() {
		return yard;
	}

	public int decrementNumAgents() {
		return numAgents--;
	}

	/**
	 * @return the obstacles ArrayList
	 */
	public ArrayList<Obstacle> getObstacles() {
		return obstacles;
	}

	/**
	 * @param obstacles the obstacles ArrayList to set
	 */
	public void setObstacles(ArrayList<Obstacle> obstacles) {
		this.obstacles = obstacles;
	}

	private void addAgents(Vector<CollisionAvoidanceManager> agentControllers) {
		if (agentControllers == null || agentControllers.size() == 0) {
			System.out.println("agents controllers are null");
			return;
		}

		numAgents = agentControllers.size();

		Steppable[] agents = new Steppable[numAgents];

		for (int i = 0; i < numAgents; ++i) {
			agents[i] = new AgentPeople(agentControllers.get(i));

			Double2D position = new Double2D(agentControllers.get(i).getPosition()[0],
					agentControllers.get(i).getPosition()[1]);

			yard.setObjectLocation(agents[i], position);
		}

		ParallelSequence parellelAgents = new ParallelSequence(agents);
		schedule.scheduleRepeating(parellelAgents);

		// Sequence sequenceAgent = new Sequence(agents);
		// schedule.scheduleRepeating(sequenceAgent);
	}

	/**
	 * Create obstacle with position, size and type of obstacle Case 0 : Square
	 * obstacle Case 1 : Horizontal line Case 2 : Vertical line
	 */
	private void addObstacles(Double2D position, int taille, int type) {
		switch (type) {
		case 0:
			for (int i = 0; i <= taille; i++) {
				for (int j = 0; j <= taille; j++) {
					AgentType obs = new AgentObstacle(position.x + i, position.y + j);
					yard.setObjectLocation(obs, new Double2D(position.x + i, position.y + j));
				}
			}
			break;
		case 1:
			for (int i = 0; i <= taille; i++) {
				AgentType obs = new AgentObstacle(position.x + i, position.y);
				yard.setObjectLocation(obs, new Double2D(position.x + i, position.y));
			}
			break;
		case 2:
			for (int i = 0; i <= taille; i++) {
				AgentType obs = new AgentObstacle(position.x, position.y + i);
				yard.setObjectLocation(obs, new Double2D(position.x, position.y + i));
			}
			break;
		default:
			break;
		}
	}

	// TODO : setup senario in Main
	// /**
	// * Initialize the agent according to the scenario Case 0 : The agent moves in
	// a
	// * circular orbit Case 1 : Two groups of opposite agents passing through each
	// * other Case 2 : Fixed obstacles in the middle of the yard Case 3 :
	// Crossroads
	// * Default : Agent random move
	// *
	// * @param nbScenario
	// */
	// private void addRandomAgents(int nbScenario) {
	// switch (nbScenario) {
	// case 0:
	// addAgentInCircle();
	// break;
	// case 1:
	// break;
	// case 2:
	// break;
	// case 3:
	// break;
	// default:
	// for (int i = 0; i < Constants.NUM_AGENT; i++) {
	// Double2D position = randomPosition();
	// double angle = this.random.nextInt(360);
	// AgentType e = new AgentPeople(position.x, position.y, 1, 1, angle);
	// yard.setObjectLocation(e, position);
	// schedule.scheduleRepeating(e);
	// numAgents++;
	// }
	// }
	// }

	// private void addAgentInCircle() {
	// double radius = (this.random.nextDouble() + 0.1) * Constants.GRID_SIZE / 2;
	// Double2D center = new Double2D(yard.getWidth() / 2, yard.getHeight() / 2);

	// for (int i = 0; i < Constants.NUM_AGENT; i++) {
	// Double2D position = randomPosition(radius, center);
	// double angle = this.random.nextInt(360);
	// AgentType e = new AgentPeople(position.x, position.y, 1, 1, angle);
	// yard.setObjectLocation(e, position);
	// schedule.scheduleRepeating(e);
	// numAgents++;
	// }
	// }

	// private void addRandomAgents()
	// {
	// for (int i = 0; i < Constants.NUM_AGENT; i++)
	// {
	// Double2D position = randomPosition();
	// double angle = this.random.nextInt(360);
	// AgentType e = new AgentPeople(position.x, position.y, angle);
	//
	// yard.setObjectLocation(e, position);
	// schedule.scheduleRepeating(e);
	//
	// numAgents++;
	// }
	// }
	//
	// private Double2D randomPosition()
	// {
	// double x = this.random.nextInt(Constants.GRID_SIZE);
	// double y = this.random.nextInt(Constants.GRID_SIZE);
	//
	// return new Double2D(x, y);
	// }

	private static final long serialVersionUID = 1L;
	private Continuous2D yard = new Continuous2D(Constants.DISCRETIZATION, Constants.GRID_SIZE, Constants.GRID_SIZE);
	private int numAgents = 0;
	private ArrayList<Obstacle> obstacles;
}
