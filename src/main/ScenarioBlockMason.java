package main;

import java.util.ArrayList;
import java.util.List;

import clearpath.EnvironmentManager;
import clearpath.Obstacle;
import model.Constants;
import model.Model;
import sim.display.Console;
import view.View;

public class ScenarioBlockMason
{
 // TODO: add obstacles
    public static void main(String[] args) 
    {
        setupScenario();
        
        Model   model   = new Model(System.currentTimeMillis());
        model.setScenarionId(1);
        View    gui     = new View(model);
        Console console = new Console(gui);
        
        console.setVisible(true);
    }
    
    
    /**
     * Create a list of obstacles with defined positions and defined types
     * 
     * @return obstacles
     */
    private static List<Obstacle> addObstacles()
    {
        List<double[]> obstacle1Vertices = new ArrayList<double[]>();
        double[] vertex11 = {-20, 50};
        double[] vertex12 = {-50, 50};
        double[] vertex13 = {-50, 20};
        double[] vertex14 = {-20, 20};
        obstacle1Vertices.add(vertex11);
        obstacle1Vertices.add(vertex12);
        obstacle1Vertices.add(vertex13);
        obstacle1Vertices.add(vertex14);
       
        List<double[]> obstacle2Vertices = new ArrayList<double[]>();
        double[] vertex21 = {20, 50};
        double[] vertex22 = {20, 20};
        double[] vertex23 = {50, 20};
        double[] vertex24 = {50, 50};
        obstacle2Vertices.add(vertex21);
        obstacle2Vertices.add(vertex22);
        obstacle2Vertices.add(vertex23);
        obstacle2Vertices.add(vertex24);
        
        List<double[]> obstacle3Vertices = new ArrayList<double[]>();
        double[] vertex31 = {20, -50};
        double[] vertex32 = {50, -50};
        double[] vertex33 = {50, -20};
        double[] vertex34 = {20, -20};
        obstacle3Vertices.add(vertex31);
        obstacle3Vertices.add(vertex32);
        obstacle3Vertices.add(vertex33);
        obstacle3Vertices.add(vertex34);
        
        List<double[]> obstacle4Vertices = new ArrayList<double[]>();
        double[] vertex41 = {-20, -50};
        double[] vertex42 = {-20, -20};
        double[] vertex43 = {-50, -20};
        double[] vertex44 = {-50, -50};
        obstacle4Vertices.add(vertex41);
        obstacle4Vertices.add(vertex42);
        obstacle4Vertices.add(vertex43);
        obstacle4Vertices.add(vertex44);      

        
        List<Obstacle> obstacles = new ArrayList<Obstacle>();
        
        obstacles.add(new Obstacle(obstacle1Vertices));
        obstacles.add(new Obstacle(obstacle2Vertices));
        obstacles.add(new Obstacle(obstacle3Vertices));
        obstacles.add(new Obstacle(obstacle4Vertices));
		
		return obstacles;
    }

	static private EnvironmentManager setupScenario()
    {   
        /* Specify the default parameters for agents that are subsequently added. */
        EnvironmentManager environment = EnvironmentManager.init(Constants.TIME_STEP, 
                                                                 Constants.DIST_NEIGHBOR, 
                                                                 Constants.MAX_NEIGHBOR,
                                                                 Constants.TIME_HORIZON, 
                                                                 Constants.SCALE_AGENT, 
                                                                 Constants.MAX_SPEED, 
                                                                 Constants.VELOCITY);
        
        environment.addObstacles(addObstacles());
        
        /*
         * Add agents, specifying their start position, and store their goals on the
         * opposite side of the environment.
         */
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 7; ++j) 
            {
                /** 
                 * Grid view
                 * Agent 4          Agent 3
                 * Agent 2          Agent 1
                 */
                // Agent 1
                double[] position1 = {55 + i * 5, 55 + j * 5};
                List<double[]> destinations1 = new ArrayList<double[]>();
                double[] destination11= {-75, -75};
                double[] destination12= {75, -75};
                destinations1.add(destination11);
                destinations1.add(destination12);
                environment.addAgent(position1, destinations1, 4);
                
                // Agent 2
                double[] position2 = {-55  - i * 5, 55 + j * 5};
                List<double[]> destinations2 = new ArrayList<double[]>();
                double[] destination21 = {75, -75};
                double[] destination22 = {-75, -75};
                destinations2.add(destination21);
                destinations2.add(destination22);
                environment.addAgent(position2, destinations2, 1);
                
                // Agent 3
                double[] position3 = {55 + i * 5, -55 - j * 5};
                List<double[]> destinations3 = new ArrayList<double[]>();
                double[] destination31 = {-75, 75};
                double[] destination32 = {75, 75};
                destinations3.add(destination31);
                destinations3.add(destination32);
                environment.addAgent(position3, destinations3, 2);

                // Agent 4
                double[] position4 = {-55  - i * 5, -55 - j * 5};
                List<double[]> destinations4 = new ArrayList<double[]>();
                double[] destination41 = {75, 75};
                double[] destination42 = {-75, 75};
                destinations4.add(destination41);
                destinations4.add(destination42);
                environment.addAgent(position4, destinations4, 3);
            }
        }
        
        return environment;
    }
}
