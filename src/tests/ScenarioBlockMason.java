package tests;

import java.util.ArrayList;
import java.util.List;

import clearpath.EnvironmentManager;
import clearpath.Obstacle;
import model.Constants;
import model.Model;
import sim.display.Console;
import sim.util.Double2D;
import view.View;

public class ScenarioBlockMason
{
 // TODO: add obstacles
    public static void main(String[] args) 
    {
        setupScenario();
        
        Model   model   = new Model(System.currentTimeMillis());        
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
        double[] vertex11 = {Constants.pctToIdxGrid(50-7), Constants.pctToIdxGrid(50-25)};
        double[] vertex12 = {Constants.pctToIdxGrid(50-25), Constants.pctToIdxGrid(50-25)};
        double[] vertex13 = {Constants.pctToIdxGrid(50-25), Constants.pctToIdxGrid(50-7)};
        double[] vertex14 = {Constants.pctToIdxGrid(50-7), Constants.pctToIdxGrid(50-7)};
        obstacle1Vertices.add(vertex11);
        obstacle1Vertices.add(vertex12);
        obstacle1Vertices.add(vertex13);
        obstacle1Vertices.add(vertex14);
        
        List<double[]> obstacle2Vertices = new ArrayList<double[]>();
        double[] vertex21 = {Constants.pctToIdxGrid(50+7), Constants.pctToIdxGrid(50-25)};
        double[] vertex22 = {Constants.pctToIdxGrid(50+7), Constants.pctToIdxGrid(50-7)};
        double[] vertex23 = {Constants.pctToIdxGrid(50+25), Constants.pctToIdxGrid(50-7)};
        double[] vertex24 = {Constants.pctToIdxGrid(50+25), Constants.pctToIdxGrid(50-25)};
        obstacle2Vertices.add(vertex21);
        obstacle2Vertices.add(vertex22);
        obstacle2Vertices.add(vertex23);
        obstacle2Vertices.add(vertex24);
        
        List<double[]> obstacle3Vertices = new ArrayList<double[]>();
        double[] vertex31 = {Constants.pctToIdxGrid(50+7), Constants.pctToIdxGrid(50+25)};
        double[] vertex32 = {Constants.pctToIdxGrid(50+25), Constants.pctToIdxGrid(50+25)};
        double[] vertex33 = {Constants.pctToIdxGrid(50+25), Constants.pctToIdxGrid(50+7)};
        double[] vertex34 = {Constants.pctToIdxGrid(50+7), Constants.pctToIdxGrid(50+7)};
        obstacle3Vertices.add(vertex31);
        obstacle3Vertices.add(vertex32);
        obstacle3Vertices.add(vertex33);
        obstacle3Vertices.add(vertex34);
        
        List<double[]> obstacle4Vertices = new ArrayList<double[]>();
        double[] vertex41 = {Constants.pctToIdxGrid(50-7), Constants.pctToIdxGrid(50+25)};
        double[] vertex42 = {Constants.pctToIdxGrid(50-7), Constants.pctToIdxGrid(50+7)};
        double[] vertex43 = {Constants.pctToIdxGrid(50-25), Constants.pctToIdxGrid(50+7)};
        double[] vertex44 = {Constants.pctToIdxGrid(50-25), Constants.pctToIdxGrid(50+25)};
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
        double headPos = Constants.pctToIdxGrid(20);
        double tailPos = Constants.pctToIdxGrid(80);

        for (int i = 0; i < 1; i++)
        {
            for (int j = 0; j < 1; ++j) 
            {
                /** 
                 * Grid view
                 * Agent 4          Agent 3
                 * Agent 2          Agent 1
                 */
                // Agent 1 
                double[] position1 = {tailPos + i * 1,  tailPos + j * 1};
                double[] destination1 = {headPos, headPos};
                environment.addAgent(position1, destination1);
                
                // Agent 2
                double[] position2 = {headPos  - i * 1,  tailPos + j * 1};
                double[] destination2 = {tailPos,headPos};
                environment.addAgent(position2, destination2);
                
                // Agent 3
                double[] position3 = {tailPos + i * 1, headPos - j * 1};
                double[] destination3 = {headPos, tailPos};
                environment.addAgent(position3, destination3);

                // Agent 4
                double[] position4 = {headPos  - i * 1, headPos  - j * 1};
                double[] destination4 = {tailPos, tailPos};
                environment.addAgent(position4, destination4);
            }
        }
        
        return environment;
    }
}
