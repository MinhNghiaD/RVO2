package tests;

import clearpath.EnvironmentManager;
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
        
        View    gui     = new View(model);
        Console console = new Console(gui);
        
        console.setVisible(true);
    }
    
    static private EnvironmentManager setupScenario()
    {   
        /* Specify the default parameters for agents that are subsequently added. */
        EnvironmentManager environment = EnvironmentManager.init(Constants.TIME_STEP, Constants.DIST_NEIGHBOR, Constants.MAX_NEIGHBOR,Constants.TIME_HORIZON, Constants.SCALE_AGENT, Constants.MAX_SPEED, Constants.VELOCITY);
        
        /*
         * Add agents, specifying their start position, and store their goals on the
         * opposite side of the environment.
         */
        double headPos = Constants.pctToIdxGrid(20);
        double tailPos = Constants.pctToIdxGrid(80);

        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; ++j) 
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
