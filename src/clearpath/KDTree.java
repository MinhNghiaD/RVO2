package clearpath;

import java.util.Vector;

public class KDTree 
{
	public KDTree()
	{
		
	}
	
	public void buildAgentTree()
	{
		
	}
	
	private static class AgentTreeNode
	{
        int begin	= 0;
        int end 	= 0;
        int left	= 0;
        int right 	= 0;
        
        double maxX = 0.0;
        double maxY = 0.0;
        double minX = 0.0;
        double minY = 0.0;
    }
	
	private static class ObstacleTreeNode 
	{
		// TODO: define obstacles
        //Obstacle obstacle = null;
        ObstacleTreeNode left = null;
        ObstacleTreeNode right = null;
    }
	
	private Vector<AgentTreeNode>    agentTree;
	private Vector<ObstacleTreeNode> obstacleTree;
	
}
