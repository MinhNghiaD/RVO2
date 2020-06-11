package kdtree;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.Vector;

import clearpath.CollisionAvoidanceManager;

public class KDNodeAgent
{
    /**
     * {@summary default constructor}
     * 
     * @param nodePos
     * @param splitAxis
     * @param dimension
     */
    public KDNodeAgent(CollisionAvoidanceManager client, int splitAxis, int dimension)
    {
        double[] nodePos = client.getPosition();
        position    = nodePos.clone();
        maxRange    = nodePos.clone();
        minRange    = nodePos.clone();
        agent       = client;

        nbDimension = dimension;

        assert splitAxis < dimension;
        assert nodePos.length == dimension;

        this.splitAxis = splitAxis;

        Left = Right = Parent = null;
    }

    public KDNodeAgent insert(CollisionAvoidanceManager client) 
    {
        double[] nodePos = client.getPosition();

        if (nodePos.length != nbDimension)
        {
            return null;
        }

        // position = new double[2];

        KDNodeAgent parent = findParent(nodePos);

        if (equal(nodePos, parent.position))
        {
            // Node for this position already exist
            // System.out.println("node " + Arrays.toString(nodePos) + " is already
            // exist.");
            return null;
        }

        KDNodeAgent newNode = new KDNodeAgent(client, ((parent.splitAxis + 1) % nbDimension), nbDimension);

        newNode.Parent = parent;

        if (nodePos[parent.splitAxis] >= parent.position[parent.splitAxis])
        {
            parent.Right = newNode;
            // System.out.println("add " + Arrays.toString(nodePos) + " to the right of" +
            // Arrays.toString(parent.position));
        } 
        else
        {
            parent.Left = newNode;
            // System.out.println("add " + Arrays.toString(nodePos) + " to the left of" +
            // Arrays.toString(parent.position));
        }

        return newNode;
    }

    public double[] getPosition() 
    {
        return position.clone();
    }

    public CollisionAvoidanceManager getAgent()
    {
        return agent;
    }

    /***
     * 
     * @param neighborList
     * @param position
     * @param sqRange
     * @param maxNbNeighbors
     * @return TreeMap of distance and Node, sorted by distance
     */
    public double getClosestNeighbors(TreeMap<Double, Vector<KDNodeAgent>> neighborList, 
                                      double[] position0, 
                                      double sqRange,
                                      int maxNbNeighbors)
    {
        // add current node to the list
        double distanceToCurrentNode = Math.sqrt(sqrDistance(position0, this.position));

        // Don't add self position to list of neighbors
        if (distanceToCurrentNode > 0)
        {
            if (!neighborList.containsKey(distanceToCurrentNode))
            {
                Vector<KDNodeAgent> nodes = new Vector<KDNodeAgent>();
                nodes.add(this);

                neighborList.put(distanceToCurrentNode, nodes);
            } 
            else
            {
                neighborList.get(distanceToCurrentNode).add(this);
            }

            /*
             * System.out.println("insert " + Arrays.toString(this.position) +
             * " to the closest neighbors of " + Arrays.toString(position0) +
             * " with distance " + distanceToCurrentNode);
             */
            // limit the size of the Map to maxNbNeighbors
            int size = 0;

            for (Double key : neighborList.keySet())
            {
                size += neighborList.get(key).size();
            }

            if (size > maxNbNeighbors)
            {
                double tailKey = neighborList.lastKey();

                Vector<KDNodeAgent> farthesNodes = neighborList.get(tailKey);

                if (farthesNodes.size() == 1)
                {
                    neighborList.remove(tailKey);
                } 
                else
                {
                    farthesNodes.remove(farthesNodes.size() - 1);
                }

                // update the searching range
                sqRange = Math.pow(neighborList.lastKey(), 2);
            }
        }

        // sub-trees Traversal
        double sqrDistanceLeftTree = 0;

        if (Left == null)
        {
            sqrDistanceLeftTree = Double.MAX_VALUE;
        } 
        else
        {
            for (int i = 0; i < nbDimension; i++)
            {
                sqrDistanceLeftTree += (Math.pow(Math.max(0, (Left.minRange[i] - position0[i])), 2)
                                      + Math.pow(Math.max(0, (position0[i] - Left.maxRange[i])), 2));
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
                sqrDistanceRightTree += (Math.pow(Math.max(0, (Right.minRange[i] - position0[i])), 2)
                                       + Math.pow(Math.max(0, (position0[i] - Right.maxRange[i])), 2));
            }
        }

        // traverse the closest area
        if (sqrDistanceLeftTree < sqrDistanceRightTree)
        {
            if (sqrDistanceLeftTree < sqRange)
            {
                /*
                 * System.out.println("left area square distance: " + sqrDistanceLeftTree +
                 * " <  square range " + sqRange);
                 */
                // traverse Left Tree
                sqRange = Left.getClosestNeighbors(neighborList, position0, sqRange, maxNbNeighbors);

                if (sqrDistanceRightTree < sqRange)
                {
                    /*
                     * System.out.println("right area square distance: " + sqrDistanceRightTree +
                     * " <  square range " + sqRange);
                     */
                    // traverse Right Tree
                    sqRange = Right.getClosestNeighbors(neighborList, position0, sqRange, maxNbNeighbors);
                }
            }
        } 
        else
        {
            if (sqrDistanceRightTree < sqRange)
            {
                /*
                 * System.out.println("right area square distance: " + sqrDistanceRightTree +
                 * " <  square range " + sqRange);
                 */
                // traverse right Tree
                sqRange = Right.getClosestNeighbors(neighborList, position0, sqRange, maxNbNeighbors);

                if (sqrDistanceLeftTree < sqRange)
                {
                    /*
                     * System.out.println("left area square distance: " + sqrDistanceLeftTree +
                     * " <  square range " + sqRange);
                     */
                    // traverse Left Tree
                    sqRange = Left.getClosestNeighbors(neighborList, position0, sqRange, maxNbNeighbors);
                }
            }
        }

        return sqRange;
    }

    /**
     * {@summary verify if 2 vectors are equal}
     * 
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
     * 
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
     * 
     * @param position0
     * @return
     */
    private KDNodeAgent findParent(double[] position0) 
    {
        KDNodeAgent parent       = null;
        KDNodeAgent currentNode  = this;

        while (currentNode != null)
        {
            currentNode.updateRange(position0);

            int split = currentNode.splitAxis;
            parent    = currentNode;

            if (position0[split] >= currentNode.position[split])
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

    private int                       splitAxis;
    private int                       nbDimension;

    private double[]                  position;

    // NOTE: limit area sub-tree
    private double[]                  maxRange;
    private double[]                  minRange;

    private CollisionAvoidanceManager agent;

    KDNodeAgent                       Parent;
    KDNodeAgent                       Left;
    KDNodeAgent                       Right;
}