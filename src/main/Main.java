package main;

import sim.display.Console;
import view.View;
import clearpath.EnvironmentManager;
import model.Model;

public class Main
{
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
        EnvironmentManager environment = EnvironmentManager.init(0.25, 15, 10, 5, 2, 2, defaultVelocity);
        
        // Setup scenario here
        
        return environment;
    }
}