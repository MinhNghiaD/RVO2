package main;

import java.util.ArrayList;
import java.util.List;

import clearpath.EnvironmentManager;
import clearpath.Obstacle;
import model.Constants;
import model.Model;
import sim.display.Console;
import view.View;

public class ScenarioOfficeEnv
{
 // TODO: add obstacles
    public static void main(String[] args) 
    {
        setupScenario();
        
        Model   model   = new Model(System.currentTimeMillis());  
        model.setScenarionId(2);
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
    	//Room 1
        List<double[]> mur1Vertices = new ArrayList<double[]>();
        double[] vertex11 = {-60, 100};
        double[] vertex12 = {-60, 70};
        mur1Vertices.add(vertex11);
        mur1Vertices.add(vertex12);
        
        //Room 2
        List<double[]> mur2Vertices = new ArrayList<double[]>();
        double[] vertex21 = {-30, 100};
        double[] vertex22 = {-30, 50};
        mur2Vertices.add(vertex21);
        mur2Vertices.add(vertex22);
        
        List<double[]> mur3Vertices = new ArrayList<double[]>();
        double[] vertex31 = {-30, 50};
        double[] vertex32 = {-20, 50};
        mur3Vertices.add(vertex31);
        mur3Vertices.add(vertex32);
        
        List<double[]> mur4Vertices = new ArrayList<double[]>();
        double[] vertex41 = {0, 50};
        double[] vertex42 = {40, 50};
        mur4Vertices.add(vertex41);
        mur4Vertices.add(vertex42);
        
        List<double[]> mur5Vertices = new ArrayList<double[]>();
        double[] vertex51 = {40, 50};
        double[] vertex52 = {40, 100};
        mur5Vertices.add(vertex51);
        mur5Vertices.add(vertex52);
        
        //Room 3
        List<double[]> mur6Vertices = new ArrayList<double[]>();
        double[] vertex61 = {60, 100};
        double[] vertex62 = {60, 90};
        mur6Vertices.add(vertex61);
        mur6Vertices.add(vertex62);
        
        List<double[]> mur7Vertices = new ArrayList<double[]>();
        double[] vertex71 = {60, 80};
        double[] vertex72 = {60, 50};
        mur7Vertices.add(vertex71);
        mur7Vertices.add(vertex72);
        
        List<double[]> mur8Vertices = new ArrayList<double[]>();
        double[] vertex81 = {60, 50};
        double[] vertex82 = {100, 50};
        mur8Vertices.add(vertex81);
        mur8Vertices.add(vertex82);
        
        //Room 4
        List<double[]> mur9Vertices = new ArrayList<double[]>();
        double[] vertex91 = {-100, 55};
        double[] vertex92 = {-60, 55};
        mur9Vertices.add(vertex91);
        mur9Vertices.add(vertex92);
        
        List<double[]> mur10Vertices = new ArrayList<double[]>();
        double[] vertex101 = {-60, 55};
        double[] vertex102 = {-60, 40};
        mur10Vertices.add(vertex101);
        mur10Vertices.add(vertex102);
        
        List<double[]> mur11Vertices = new ArrayList<double[]>();
        double[] vertex111 = {-60, 25};
        double[] vertex112 = {-60, -25};
        mur11Vertices.add(vertex111);
        mur11Vertices.add(vertex112);
        
        List<double[]> mur12Vertices = new ArrayList<double[]>();
        double[] vertex121 = {-100, -25};
        double[] vertex122 = {-75, -25};
        mur12Vertices.add(vertex121);
        mur12Vertices.add(vertex122);

        // Room 5 - Center
        List<double[]> mur13Vertices = new ArrayList<double[]>();
        double[] vertex131 = {-30, 35};
        double[] vertex132 = {60, 35};
        mur13Vertices.add(vertex131);
        mur13Vertices.add(vertex132);
        
//        List<double[]> mur14Vertices = new ArrayList<double[]>();
//        double[] vertex141 = {15, 35};
//        double[] vertex142 = {60, 35};
//        mur14Vertices.add(vertex141);
//        mur14Vertices.add(vertex142);
//        
        List<double[]> mur15Vertices = new ArrayList<double[]>();
        double[] vertex151 = {60, 35};
        double[] vertex152 = {60, -15};
        mur15Vertices.add(vertex151);
        mur15Vertices.add(vertex152);
        
        List<double[]> mur16Vertices = new ArrayList<double[]>();
        double[] vertex161 = {60, -30};
        double[] vertex162 = {60, -55};
        mur16Vertices.add(vertex161);
        mur16Vertices.add(vertex162);
        
        List<double[]> mur18Vertices = new ArrayList<double[]>();
        double[] vertex181 = {60, -55};
        double[] vertex182 = {-30, -55};
        mur18Vertices.add(vertex181);
        mur18Vertices.add(vertex182);
        
        List<double[]> mur19Vertices = new ArrayList<double[]>();
        mur19Vertices.add(vertex182);
        mur19Vertices.add(vertex131);
        
        //Room 6
        List<double[]> mur20Vertices = new ArrayList<double[]>();
        double[] vertex201 = {60, 5};
        double[] vertex202 = {80, 5};
        mur20Vertices.add(vertex202);
        mur20Vertices.add(vertex201);
        
        List<double[]> mur21Vertices = new ArrayList<double[]>();
        double[] vertex211 = {60, -35};
        double[] vertex212 = {80, -35};
        mur21Vertices.add(vertex212);
        mur21Vertices.add(vertex211);
        
        //Room 7
        List<double[]> mur22Vertices = new ArrayList<double[]>();
        double[] vertex221 = {-80, -25};
        double[] vertex222 = {-80, -55};
        mur22Vertices.add(vertex222);
        mur22Vertices.add(vertex221);
        
        List<double[]> mur23Vertices = new ArrayList<double[]>();
        double[] vertex231 = {-80, -55};
        double[] vertex232 = {-100, -55};
        mur23Vertices.add(vertex232);
        mur23Vertices.add(vertex231);
        
        //Room 8
        List<double[]> mur24Vertices = new ArrayList<double[]>();
        double[] vertex241 = {-100, -75};
        double[] vertex242 = {-20, -75};
        mur24Vertices.add(vertex241);
        mur24Vertices.add(vertex242);
        
        //Room 9
        List<double[]> mur25Vertices = new ArrayList<double[]>();
        double[] vertex251 = {-20, -65};
        double[] vertex252 = {-20, -100};
        mur25Vertices.add(vertex252);
        mur25Vertices.add(vertex251);
        
        List<double[]> mur26Vertices = new ArrayList<double[]>();
        double[] vertex261 = {-20, -65};
        double[] vertex262 = {-5, -65};
        mur26Vertices.add(vertex262);
        mur26Vertices.add(vertex261);
        
        List<double[]> mur27Vertices = new ArrayList<double[]>();
        double[] vertex271 = {10, -65};
        double[] vertex272 = {45, -65};
        mur27Vertices.add(vertex272);
        mur27Vertices.add(vertex271);
        
        List<double[]> mur28Vertices = new ArrayList<double[]>();
        double[] vertex281 = {45, -65};
        double[] vertex282 = {45, -100};
        mur28Vertices.add(vertex282);
        mur28Vertices.add(vertex281);
        
        // Room 10
        List<double[]> mur29Vertices = new ArrayList<double[]>();
        double[] vertex291 = {45, -70};
        double[] vertex292 = {60, -70};
        mur29Vertices.add(vertex292);
        mur29Vertices.add(vertex291);
        
        List<double[]> mur30Vertices = new ArrayList<double[]>();
        double[] vertex301 = {70, -35};
        double[] vertex302 = {70, -85};
        mur30Vertices.add(vertex302);
        mur30Vertices.add(vertex301);
        
        List<double[]> mur31Vertices = new ArrayList<double[]>();
        double[] vertex311 = {70, -95};
        double[] vertex312 = {70, -100};
        mur31Vertices.add(vertex312);
        mur31Vertices.add(vertex311);
        
        
        List<Obstacle> obstacles = new ArrayList<Obstacle>();
        
        obstacles.add(new Obstacle(mur1Vertices));
        obstacles.add(new Obstacle(mur2Vertices));
        obstacles.add(new Obstacle(mur3Vertices));
        obstacles.add(new Obstacle(mur4Vertices));
        obstacles.add(new Obstacle(mur5Vertices));
        obstacles.add(new Obstacle(mur6Vertices));
        obstacles.add(new Obstacle(mur7Vertices));
        obstacles.add(new Obstacle(mur8Vertices));
        obstacles.add(new Obstacle(mur9Vertices));
        obstacles.add(new Obstacle(mur10Vertices));
        obstacles.add(new Obstacle(mur11Vertices));
        obstacles.add(new Obstacle(mur12Vertices));
        obstacles.add(new Obstacle(mur13Vertices));
//        obstacles.add(new Obstacle(mur14Vertices));
        obstacles.add(new Obstacle(mur15Vertices));
        obstacles.add(new Obstacle(mur16Vertices));
        obstacles.add(new Obstacle(mur18Vertices));
        obstacles.add(new Obstacle(mur19Vertices));
        obstacles.add(new Obstacle(mur20Vertices));
        obstacles.add(new Obstacle(mur21Vertices));
        obstacles.add(new Obstacle(mur22Vertices));
        obstacles.add(new Obstacle(mur23Vertices));
        obstacles.add(new Obstacle(mur24Vertices));
        obstacles.add(new Obstacle(mur25Vertices));
        obstacles.add(new Obstacle(mur26Vertices));
        obstacles.add(new Obstacle(mur27Vertices));
        obstacles.add(new Obstacle(mur28Vertices));
        obstacles.add(new Obstacle(mur29Vertices));
        obstacles.add(new Obstacle(mur30Vertices));
        obstacles.add(new Obstacle(mur31Vertices));
		
		return obstacles;
    }
    
    /**
     * List destination to get the exit
     * @return
     */
    private List<double[]> setDestinations1()
    {
    	double[] dest1 = {60, 20};
    	double[] dest2 = {70, 5};
    	double[] dest3 = {-40, 40};
    	double[] dest4 = {-40, -60};
    	double[] dest5 = {-90, -65};
    	
    	List<double[]> destinationsList = new ArrayList<>();
    	destinationsList.add(dest1);
    	destinationsList.add(dest2);
    	destinationsList.add(dest3);
    	destinationsList.add(dest4);
    	destinationsList.add(dest5);
		
    	return destinationsList;
    	
    }
    
    private List<double[]> setDestinations2()
    {
    	double[] dest1 = {60, -20};
    	double[] dest2 = {70, -35};
    	double[] dest3 = {70, -90};
    	double[] dest4 = {50, -70};
    	double[] dest5 = {50, -60};
    	double[] dest6 = {-40, -60};
    	double[] dest7 = {-90, -65};
    	
    	List<double[]> destinationsList = new ArrayList<>();
    	destinationsList.add(dest1);
    	destinationsList.add(dest2);
    	destinationsList.add(dest3);
    	destinationsList.add(dest4);
    	destinationsList.add(dest5);
    	destinationsList.add(dest6);
    	destinationsList.add(dest7);
		
    	return destinationsList;
    	
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
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; ++j) 
            {
                double[] position1 = {i * 5, j * 5};
                //TODO use setDirections1 or setDirections2
                //double[] destination1 = {-90, -65};
                //environment.addAgent(position1, destination1, 6);
                
            }
        }
        
        return environment;
    }
}
