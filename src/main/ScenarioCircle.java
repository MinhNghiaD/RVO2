package main;

import java.util.ArrayList;
import java.util.List;

import clearpath.EnvironmentManager;
import clearpath.Obstacle;
import model.Constants;
import model.Model;
import sim.display.Console;
import view.View;

public class ScenarioCircle
{
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
        double[] vertex11 = {-10, 40};
        double[] vertex12 = {-40, 40};
        double[] vertex13 = {-40, 10};
        //double[] vertex14 = {-10, 10};
        obstacle1Vertices.add(vertex11);
        obstacle1Vertices.add(vertex12);
        obstacle1Vertices.add(vertex13);
        //obstacle1Vertices.add(vertex14);
       
        List<double[]> obstacle2Vertices = new ArrayList<double[]>();
        double[] vertex21 = {10, 40};
        double[] vertex22 = {10, 10};
        //double[] vertex23 = {40, 10};
        double[] vertex24 = {40, 40};
        obstacle2Vertices.add(vertex21);
        obstacle2Vertices.add(vertex22);
        //obstacle2Vertices.add(vertex23);
        obstacle2Vertices.add(vertex24);
        
        List<double[]> obstacle3Vertices = new ArrayList<double[]>();
        double[] vertex31 = {10, -40};
        double[] vertex32 = {40, -40};
        double[] vertex33 = {40, -10};
        //double[] vertex34 = {10, -10};
        obstacle3Vertices.add(vertex31);
        obstacle3Vertices.add(vertex32);
        obstacle3Vertices.add(vertex33);
        //obstacle3Vertices.add(vertex34);
        
        List<double[]> obstacle4Vertices = new ArrayList<double[]>();
        double[] vertex41 = {-10, -40};
        //double[] vertex42 = {-10, -10};
        double[] vertex43 = {-40, -10};
        double[] vertex44 = {-40, -40};
        obstacle4Vertices.add(vertex41);
        //obstacle4Vertices.add(vertex42);
        obstacle4Vertices.add(vertex43);
        obstacle4Vertices.add(vertex44);      
        /* 
        List<double[]> obstacle5Vertices = new ArrayList<double[]>();
        double[] vertex51 = {-20, -60};
        double[] vertex53 = {-50, -20};
        //double[] vertex54 = {-10, -40};
        obstacle5Vertices.add(vertex51);
        obstacle5Vertices.add(vertex53);
        //obstacle5Vertices.add(vertex54);  
      
        List<double[]> obstacle4Vertices = new ArrayList<double[]>();
        double[] vertex41 = {-10, -40};
        //double[] vertex42 = {-10, -10};
        double[] vertex43 = {-40, -10};
        double[] vertex44 = {-40, -40};
        obstacle4Vertices.add(vertex41);
        //obstacle4Vertices.add(vertex42);
        obstacle4Vertices.add(vertex43);
        obstacle4Vertices.add(vertex44);  
        
        List<double[]> obstacle4Vertices = new ArrayList<double[]>();
        double[] vertex41 = {-10, -40};
        //double[] vertex42 = {-10, -10};
        double[] vertex43 = {-40, -10};
        double[] vertex44 = {-40, -40};
        obstacle4Vertices.add(vertex41);
        //obstacle4Vertices.add(vertex42);
        obstacle4Vertices.add(vertex43);
        obstacle4Vertices.add(vertex44);  
      */  
        List<Obstacle> obstacles = new ArrayList<Obstacle>();
        
        obstacles.add(new Obstacle(obstacle1Vertices));
        obstacles.add(new Obstacle(obstacle2Vertices));
        obstacles.add(new Obstacle(obstacle3Vertices));
        obstacles.add(new Obstacle(obstacle4Vertices));
        //obstacles.add(new Obstacle(obstacle5Vertices));
        
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
        
        for (int i = 0; i < 150; ++i) 
        {
            double[] position = {100 * Math.cos(2*i*Constants.PI/150), 100 * Math.sin(2*i*Constants.PI/150)};
            double[] destination = {-100 * Math.cos(2*i*Constants.PI/150), -100 * Math.sin(2*i*Constants.PI/150)};
            environment.addAgent(position, destination, 3);
        }
        
        return environment;
    }
}
