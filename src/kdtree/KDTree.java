package kdtree;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.Vector;

import clearpath.CollisionAvoidanceManager;

public class KDTree
{   
    public KDTree(int dim)
    {
    	root 		= null;
        nbDimension = dim;
        agents 		= new Vector<CollisionAvoidanceManager>();
    }

    /**
     * Add a new Node to the tree
     * @param position
     * @return
     */
    public boolean add(CollisionAvoidanceManager client)
    {
        if (root == null)
        {
        	root = new KDNode(client, 0, nbDimension);
        	//System.out.println("root : " + Arrays.toString(position));
        	
        	if (! agents.contains(client))
        	{
        		agents.add(client);
        	}
        } 
        else
        {
            if (root.insert(client) != null)
            {
            	if (! agents.contains(client))
            	{
            		agents.add(client);
            	}
            }
        }
        
        return true;
    }
    
    /**
     * 
     * @param position
     * @param sqRange
     * @param maxNbNeighbors
     * @return Map of N-nearest neighbors, sorted by distance
     */
    public TreeMap<Double, Vector<KDNode> > getClosestNeighbors(double[] position, double sqRange, int maxNbNeighbors)
    {
    	// Map of distance and nodes
    	TreeMap<Double, Vector<KDNode> > closestNeighbors = new TreeMap<Double, Vector<KDNode> >();
    	
    	sqRange = root.getClosestNeighbors(closestNeighbors, position, sqRange, maxNbNeighbors);
    	
    	return closestNeighbors;
    }
    
    public void update()
    {
    	// clean old tree and construct new one
    	root = null;
    	
    	for (CollisionAvoidanceManager agent : agents)
    	{
    		add(agent);
    	}
    }
    
    public Vector<CollisionAvoidanceManager> getAgents()
    {
    	return agents;
    }
    
    private KDNode 								root;
    private int 								nbDimension;
    private Vector<CollisionAvoidanceManager> 	agents;
}