package kdtree;

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
        nbDimension = dimension;

        assert splitAxis < dimension;
        this.splitAxis = splitAxis;

        Left = Right = Parent = null;

        checked = false;
        id = 0;
    }

    /**
     * {@summary get parent of a given position}
     * @param position0
     * @return
     */
    public KDNode findParent(double[] position0)
    {
        KDNode parent = null;

        KDNode currentNode = this;

        int split;

        while (currentNode != null)
        {
            split  = currentNode.splitAxis;
            parent = currentNode;

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

        KDNode newNode = new KDNode(nodePos, parent.splitAxis%nbDimension, nbDimension);

        newNode.Parent = parent;
 
        if (nodePos[parent.splitAxis] > parent.position[parent.splitAxis])
        {
            parent.Right = newNode;

            newNode.orientation = true; //
        } 
        else
        {
            parent.Left = newNode;
            newNode.orientation = false; //
        }
        
        return newNode;
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
        	sum += (position1[k] - position2[k]) * (position1[k] - position2[k]);
        }
            
        return sum;
    }
    
    private int splitAxis;
    private double[] position;
    private int nbDimension;
    
    // TODO verify these
    int id;
    boolean checked;
    boolean orientation;

    KDNode Parent;
    KDNode Left;
    KDNode Right;
}