package tests;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import clearpath.CollisionAvoidanceManager;
import clearpath.EnvironmentManager;
import ec.util.MersenneTwisterFast;

public class ScenarioBlock
{
    // TODO: add obstacles
    public static void main(String[] args) 
    {
        EnvironmentManager environment = setupScenario();
        int nbStep = 0;
        
        do 
        {
            environment.doStep();
            
            Vector<CollisionAvoidanceManager> agents = environment.getAgents();
            
            for (int i = 0; i < agents.size(); i++)
            {
                agents.get(i).update(random);
                System.out.println("agent " + i + "move to : " + Arrays.toString(agents.get(i).getPosition()));
            }
            
            System.out.println("Step "+ nbStep++ + " :");
        }
        while (!reachedGoal(environment));
        
        System.out.println("Finish with "+ nbStep + " !!!");
    }
    
    static private EnvironmentManager setupScenario()
    {
        double[] defaultVelocity = {0, 0};
        
        /* Specify the default parameters for agents that are subsequently added. */
        EnvironmentManager environment = EnvironmentManager.init(0.25, 15, 10, 5, 2, 2, defaultVelocity);
        
        /*
         * Add agents, specifying their start position, and store their goals on the
         * opposite side of the environment.
         */
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; ++j) 
            {
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
    
    static final MersenneTwisterFast random = new MersenneTwisterFast(System.currentTimeMillis());
}


