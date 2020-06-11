package tests;

import java.util.ArrayList;

import model.Model;
import model.Obstacle;
import sim.display.Console;
import sim.util.Double2D;
import view.View;

public class Test_AgentObstacle {

	private static ArrayList<Obstacle> testObstacles() {
		//Use example for obstacles
		ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
		obstacles.add(new Obstacle(new Double2D(0,0), 10, 0));
		obstacles.add(new Obstacle(new Double2D(20,10), 10, 1));
		obstacles.add(new Obstacle(new Double2D(20,10), 10, 2));
		return obstacles;
	}
	
	public static void main(String[] args) 
	{
		Model model = new Model(System.currentTimeMillis());
		ArrayList<Obstacle> obstacles = testObstacles();
		model.setObstacles(obstacles);
		View gui = new View(model);
		Console console = new Console(gui);
		console.setVisible(true);
	}
}
