package kdtree;

import java.util.TreeMap; 

public class KDNode
{
    /**
     * {@summary default constructor}
     * @param nodePos
     * @param splitAxis
     * @param dimension
     */
    public KDNode(double[] nodePos, int splitAxis, int dimension)
    {
        position = nodePos.clone();
        maxRange = nodePos.clone();
        minRange = nodePos.clone();
        
        nbDimension = dimension;

        assert splitAxis < dimension;
        assert nodePos.length == dimension;
        
        this.splitAxis = splitAxis;

        Left = Right = Parent = null;
    }

    public KDNode insert(double[] nodePos)
    {
    	if (nodePos.length != nbDimension)
    	{
    		return null;
    	}
    	
        //position = new double[2];

        KDNode parent = findParent(nodePos);

        if (equal(nodePos, parent.position))
        {
        	// Node for this position already exist
        	return null;
        }

        KDNode newNode = new KDNode(nodePos, ((parent.splitAxis + 1) % nbDimension), nbDimension);

        newNode.Parent = parent;
 
        if (nodePos[parent.splitAxis] > parent.position[parent.splitAxis])
        {
            parent.Right = newNode;
        } 
        else
        {
            parent.Left = newNode;
        }
        
        return newNode;
    }

    /***
     * 
     * @param neighborList
     * @param position
     * @param sqRange
     * @param maxNbNeighbors
     * @return TreeMap of distance and Node, sorted by distance
     */
    public double getClosestNeighbors(TreeMap<Double, KDNode> neighborList, double[] position0, double sqRange, int maxNbNeighbors)
    {
    	// add current node to the list
    	double distanceToCurrentNode = Math.sqrt(sqrDistance(position0, this.position));
    	
    	neighborList.put(distanceToCurrentNode, this);
    	
    	// limit the size of the Map to maxNbNeighbors
    	if (neighborList.size() > maxNbNeighbors)
    	{
    		neighborList.remove(neighborList.lastKey());
    		
    		// update the searching range
    		sqRange = neighborList.lastKey();
    	}
    	
    	// sub-trees Traversal
    	double sqrDistanceLeftTree  = 0;
    	
    	
    	if (Left == null)
    	{
    		sqrDistanceLeftTree = Double.MAX_VALUE;
    	}
    	else
    	{
    		for (int i = 0; i < nbDimension; i++)
        	{
        		sqrDistanceLeftTree += (Math.pow(Math.max(0, (Left.minRange[i] - position0[i])), 2) +
        								Math.pow(Math.max(0, (position0[i] - Left.maxRange[i])), 2));
        	}
    	}
    	
    	
    	double sqrDistanceRightTree = 0;
    	
    	if (Right == null)
    	{
    		sqrDistanceRightTree = Double.MAX_VALUE;
    	}
    	else
    	{
    		for (int i = 0; i < nbDimension; i++)
        	{
        		sqrDistanceRightTree += (Math.pow(Math.max(0, (Right.minRange[i] - position0[i])), 2) +
        								 Math.pow(Math.max(0, (position0[i] - Right.maxRange[i])), 2));
        	}
    	}
    	
    	// traverse the closest area
    	if (sqrDistanceLeftTree < sqrDistanceRightTree)
    	{
    		if (sqrDistanceLeftTree < sqRange)
    		{
    			// traverse Left Tree
    			sqRange = Left.getClosestNeighbors(neighborList, position0, sqRange, maxNbNeighbors);
    			
    			if (sqrDistanceRightTree < sqRange)
        		{
    				sqRange = Right.getClosestNeighbors(neighborList, position0, sqRange, maxNbNeighbors);
        		}
    		}
    	}
    	else
    	{
    		if (sqrDistanceRightTree < sqRange)
    		{
    			// traverse right Tree
    			sqRange = Right.getClosestNeighbors(neighborList, position0, sqRange, maxNbNeighbors);
    			
    			if (sqrDistanceLeftTree < sqRange)
        		{
    				sqRange = Left.getClosestNeighbors(neighborList, position0, sqRange, maxNbNeighbors);
        		}
    		}
    	}   	
    	
    	return sqRange;
    }
    
    
    
 
    /**
     * {@summary verify if 2 vectors are equal}
     * @param position1
     * @param position2
     * @return 
     */
    public static boolean equal(double[] position1, double[] position2)
    {
    	if (position1.length != position2.length)
    	{
    		return false;
    	}
    	
        for (int k = 0; k < position1.length; k++)
        {
            if (position1[k] != position2[k])
            {
                return false;
            }
        }

        return true;
    }

    /**
     * {@summary calculate the square of distance between 2 points}
     * @param position1
     * @param position2
     * @return
     */
    public static double sqrDistance(double[] position1, double[] position2)
    {
    	if (position1.length != position2.length)
    	{
    		return -1;
    	}
    	
        double sum = 0;
        
        for (int k = 0; k < position1.length; ++k)
        {
        	sum += Math.pow((position1[k] - position2[k]), 2);
        }
            
        return sum;
    }
    
    /**
     * {@summary get parent of a given position}
     * @param position0
     * @return
     */
    private KDNode findParent(double[] position0)
    {
        KDNode parent 	   = null;
        KDNode currentNode = this;

        while (currentNode != null)
        {
        	currentNode.updateRange(position0);
        	
        	int split  = currentNode.splitAxis;
            parent     = currentNode;

            if (position0[split] > currentNode.position[split])
            {
                currentNode = currentNode.Right;
            }
            else
            {
                currentNode = currentNode.Left;
            }
        }

        return parent;
    }
    
    private void updateRange(double[] position0)
    {
    	if (position0.length != nbDimension)
    	{
    		return;
    	}
    	
    	for (int i = 0; i < nbDimension; ++i)
    	{
    		maxRange[i] = Math.max(maxRange[i], position0[i]);
    		minRange[i] = Math.min(minRange[i], position0[i]);
    	}
    }
    
    private int splitAxis;
    private int nbDimension;
    
    private double[] position;
    
    // NOTE: limit area sub-tree
    private double[] maxRange;
    private double[] minRange;

    KDNode Parent;
    KDNode Left;
    KDNode Right;
}