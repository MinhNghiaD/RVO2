package kdtree;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.Vector;

import clearpath.CollisionAvoidanceManager;

public class KDTree
{   
    public KDTree(int dim)
    {
    	Root 		= null;
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
        if (Root == null)
        {
        	Root = new KDNode(client, 0, nbDimension);
        	//System.out.println("Root : " + Arrays.toString(position));
        	
        	if (! agents.contains(client))
        	{
        		agents.add(client);
        	}
        } 
        else
        {
            if (Root.insert(client) != null)
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
    	
    	sqRange = Root.getClosestNeighbors(closestNeighbors, position, sqRange, maxNbNeighbors);
    	
    	return closestNeighbors;
    }
    
    private KDNode 								Root;
    private int 								nbDimension;
    private Vector<CollisionAvoidanceManager> 	agents;
}