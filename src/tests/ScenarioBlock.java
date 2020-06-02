package tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import clearpath.CollisionAvoidanceManager;
import clearpath.EnvironmentManager;
import clearpath.RVO;
import model.Constants;

public class ScenarioBlock
{

    public static void main(String[] args) 
    {
        EnvironmentManager environment = setupScenario();
        
        do 
        {
            environment.doStep();
            setPreferredVelocities(environment);
            
            Vector<CollisionAvoidanceManager> agents = environment.getAgents();
            
            for (int i = 0; i < agents.size(); i++)
            {
                agents.get(i).update();
                System.out.println("agent " + i + "move to : " + Arrays.toString(agents.get(i).getPosition()));
            }
        }
        while (!reachedGoal(environment));
        
        System.out.println("Finish !!!");
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
                double[] position1 = {55 + i * 10,  55 + j * 10};
                environment.addAgent(position1);
                double[] destination1 = {-75, -75};
                goals.add(destination1);

                
                // Agent 2
                double[] position2 = {-55 - i * 10,  55 + j * 10};
                environment.addAgent(position2);
                double[] destination2 = {75, -75};
                goals.add(destination2);
                
                // Agent 3
                double[] position3 = {55 + i * 10, -55 - j * 10};
                environment.addAgent(position3);
                double[] destination3 = {-75, 75};
                goals.add(destination3);


                // Agent 4
                double[] position4 = {-55 - i * 10, -55 - j * 10};
                environment.addAgent(position4);
                double[] destination4 = {75, 75};
                goals.add(destination4);
            }
        }
        
        return environment;
    }
    
    static private void setPreferredVelocities(EnvironmentManager environment)
    {
        /*
         * Set the preferred velocity to be a vector of unit magnitude (speed) in the
         * direction of the goal.
         */
        
        Vector<CollisionAvoidanceManager> agents = environment.getAgents();
        
        for (int i = 0; i < agents.size(); ++i) 
        {
            double[] goalVector = RVO.vectorSubstract(goals.get(i), agents.get(i).getPosition());

            double absSqrGoalVector = RVO.vectorProduct(goalVector, goalVector);
            
            if (absSqrGoalVector > 1) 
            {
                goalVector = RVO.scalarProduct(goalVector, 1/Math.sqrt(absSqrGoalVector));
            }

            /*
             * pivot a little to avoid deadlocks due to perfect symmetry.
             */
            final double angle    = random.nextDouble() * 2 * Constants.PI;
            final double distance = random.nextDouble() * 0.0001;

            goalVector[0] += distance * Math.cos(angle);
            goalVector[1] += distance * Math.sin(angle);
            
            agents.get(i).setPreferenceVelocity(goalVector);
        }
    }
    
    static private boolean reachedGoal(EnvironmentManager environment)
    {
        Vector<CollisionAvoidanceManager> agents = environment.getAgents();
        
        /* Check if all agents have reached their goals. */
        for (int i = 0; i < agents.size(); ++i) 
        {
            double[] distanceToGoal = RVO.vectorSubstract(agents.get(i).getPosition(), goals.get(i));
            
            final double sqrDistance = RVO.vectorProduct(distanceToGoal, distanceToGoal);
            
            //System.out.println("distance from agent " + i + " to is goal: " + sqrDistance);
            
            if (sqrDistance > 400) 
            {
                return false;
            }
        }

        return true;
    }
    
    static private List<double[]> goals = new ArrayList<>();
    static private final Random random = new Random();
}


