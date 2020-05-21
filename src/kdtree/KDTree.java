package kdtree;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.Vector;

public class KDTree
{   
    public KDTree(int dim)
    {
    	Root = null;
        nbDimension = dim;
        nodeList = new Vector<KDNode>();
    }

    /**
     * Add a new Node to the tree
     * @param position
     * @return
     */
    public boolean add(double[] position)
    {
        if (Root == null)
        {
        	Root = new KDNode(position, 0, nbDimension);
        	//System.out.println("Root : " + Arrays.toString(position));
        	
            nodeList.add(Root);
        } 
        else
        {
            KDNode pNode;
            
            if ((pNode = Root.insert(position)) != null)
            {
                nodeList.add(pNode);
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
    public TreeMap<Double, KDNode> getClosestNeighbors(double[] position, double sqRange, int maxNbNeighbors)
    {
    	TreeMap<Double, KDNode> closestNeighbors = new TreeMap<Double, KDNode>();
    	
    	sqRange = Root.getClosestNeighbors(closestNeighbors, position, sqRange, maxNbNeighbors);
    	
    	return closestNeighbors;
    }
    
    private KDNode Root;
    private int nbDimension;
    private Vector<KDNode> nodeList;
}