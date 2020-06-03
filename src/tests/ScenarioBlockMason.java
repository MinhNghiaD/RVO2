package tests;

import java.util.Arrays;
import java.util.Vector;

import clearpath.CollisionAvoidanceManager;
import clearpath.EnvironmentManager;
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
        double[] defaultVelocity = {0, 0};
        
        /* Specify the default parameters for agents that are subsequently added. */
        EnvironmentManager environment = EnvironmentManager.init(0.25, 15, 10, 5, 0.5, 1, defaultVelocity);
        
        /*
         * Add agents, specifying their start position, and store their goals on the
         * opposite side of the environment.
         */
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; ++j) 
            {
                // Agent 1 
                double[] position1 = {55 + i * 1,  55 + j * 1};
                double[] destination1 = {-75, -75};
                environment.addAgent(position1, destination1);
                
                // Agent 2
                double[] position2 = {-55 - i * 1,  55 + j * 1};
                double[] destination2 = {75, -75};
                environment.addAgent(position2, destination2);
                
                // Agent 3
                double[] position3 = {55 + i * 1, -55 - j * 1};
                double[] destination3 = {-75, 75};
                environment.addAgent(position3, destination3);

                // Agent 4
                double[] position4 = {-55 - i * 1, -55 - j * 1};
                double[] destination4 = {75, 75};
                environment.addAgent(position4, destination4);
            }
        }
        
        return environment;
    }
    
    static private boolean reachedGoal(EnvironmentManager environment)
    {
        Vector<CollisionAvoidanceManager> agents = environment.getAgents();
        
        /* Check if all agents have reached their goals. */
        for (int i = 0; i < agents.size(); ++i) 
        {   
            if (! agents.get(i).reachedGoal()) 
            {
                return false;
            }
        }

        return true;
    }
}
