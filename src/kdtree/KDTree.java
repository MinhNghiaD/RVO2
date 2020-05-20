package kdtree;

import java.util.Vector;

public class KDTree
{   
    public KDTree(int dim)
    {
        root = null;
        nbDimension = dim;
        nodeList = new Vector<KDNode>();
    }

    public boolean add(double[] x)
    {
        if (root == null)
        {
            root = new KDNode(x, 0, nbDimension);
            nodeList.add(root);
        } 
        else
        {
            KDNode pNode;
            
            if ((pNode = root.insert(x)) != null)
            {
                nodeList.add(pNode);
            }
        }
        
        return true;
    }
    
    private KDNode root;
    private int nbDimension;
    private Vector<KDNode> nodeList;
}