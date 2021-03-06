package main;

import java.util.ArrayList;
import java.util.List;

import clearpath.EnvironmentManager;
import clearpath.Obstacle;
import model.Constants;
import model.Model;
import sim.display.Console;
import view.View;

public class ScenarioShibuya
{
 // TODO: add obstacles
    public static void main(String[] args) 
    {
        setupScenario();
        
        Model   model   = new Model(System.currentTimeMillis());
        model.setScenarionId(4);
        View    gui     = new View(model);
        Console console = new Console(gui);
        
        console.setVisible(true);
    }
    
    
    /**
     * Create a list of obstacles with defined positions and defined types
     * nowing that the origin of the grid axis is located in the center of the grid
     * 
     * @return obstacles
     */
    private static List<Obstacle> addObstacles()
    {
        List<double[]> obstacle1Vertices = new ArrayList<double[]>();
        double[] vertex11 = {-30, 100};
        double[] vertex12 = {-100, 100};
        double[] vertex13 = {-100, 30};
        double[] vertex14 = {-30, 30};
        obstacle1Vertices.add(vertex11);
        obstacle1Vertices.add(vertex12);
        obstacle1Vertices.add(vertex13);
        obstacle1Vertices.add(vertex14);
       
        List<double[]> obstacle2Vertices = new ArrayList<double[]>();
        double[] vertex21 = {30, 100};
        double[] vertex22 = {30, 30};
        double[] vertex23 = {100, 30};
        double[] vertex24 = {100, 100};
        obstacle2Vertices.add(vertex21);
        obstacle2Vertices.add(vertex22);
        obstacle2Vertices.add(vertex23);
        obstacle2Vertices.add(vertex24);
        
        List<double[]> obstacle3Vertices = new ArrayList<double[]>();
        double[] vertex31 = {30, -100};
        double[] vertex32 = {100, -100};
        double[] vertex33 = {100, -30};
        double[] vertex34 = {30, -30};
        obstacle3Vertices.add(vertex31);
        obstacle3Vertices.add(vertex32);
        obstacle3Vertices.add(vertex33);
        obstacle3Vertices.add(vertex34);
        
        List<double[]> obstacle4Vertices = new ArrayList<double[]>();
        double[] vertex41 = {-30, -100};
        double[] vertex42 = {-30, -30};
        double[] vertex43 = {-100, -30};
        double[] vertex44 = {-100, -100};
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
                 * 			Agent 4          
                 * Agent 2          Agent 1
                 * 			Agent 3
                 */
                // Agent 1
            	
                double[] position1 = {65 + i * 5, -15 + j * 5};
                List<double[]> destinations1 = new ArrayList<double[]>();
                double[] destination11= {-90, 0};
                double[] destination12= {90, 0};
                destinations1.add(destination11);
                destinations1.add(destination12);
                environment.addAgent(position1, destinations1, 4);
                
                // Agent 2
                double[] position2 = {-65 - i * 5, -15 + j * 5};
                List<double[]> destinations2 = new ArrayList<double[]>();
                double[] destination21 = {90, 0};
                double[] destination22 = {-90, 0};
                destinations2.add(destination21);
                destinations2.add(destination22);
                environment.addAgent(position2, destinations2, 1);
                
                // Agent 3
                double[] position3 = {-15 + i * 5, 65 + j * 5};
                List<double[]> destinations3 = new ArrayList<double[]>();
                double[] destination31 = {0, -90};
                double[] destination32 = {0, 90};
                destinations3.add(destination31);
                destinations3.add(destination32);
                environment.addAgent(position3, destinations3, 2);

                // Agent 4
                double[] position4 = {-15 + i * 5, -65 - j * 5};
                List<double[]> destinations4 = new ArrayList<double[]>();
                double[] destination41 = {0, 90};
                double[] destination42 = {0, -90};
                destinations4.add(destination41);
                destinations4.add(destination42);
                environment.addAgent(position4, destinations4, 3);
            }
        }
        
        return environment;
    }
}
