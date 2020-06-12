package kdtree;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import clearpath.Obstacle;
import clearpath.ObstacleVertex;
import clearpath.RVO;

public class KDNodeObstacle
{
    KDNodeObstacle(List<ObstacleVertex> vertices)
    {
        vertex = optimalSplitPoint(vertices);
        
        splitObstacleVertices(vertices, vertex);
        
        Left  = null;
        Right = null;
        
        if (! leftVertices.isEmpty())
        {
            Left  = new KDNodeObstacle(leftVertices);
        }
        
        if (! rightVertices.isEmpty())
        {
            Right = new KDNodeObstacle(rightVertices);
        }
    }
    
    public ObstacleVertex getVertex()
    {
        return vertex;
    }
    
    public void getClosestNeighbors(TreeMap<Double, Vector<KDNodeObstacle>> neighborList, 
                                    double[] position0, 
                                    double   sqRange)
    {
        final ObstacleVertex nextVertex = vertex.nextVertex;

        final double distanceToLine = Obstacle.distanceToLine(vertex.position(), nextVertex.position(), position0);
        
        KDNodeObstacle firstVisit  = null;
        KDNodeObstacle secondVisit = null;
        
        if (distanceToLine >= 0)
        {
            firstVisit  = Left;
            secondVisit = Right;
        }
        else
        {
            firstVisit  = Right; 
            secondVisit = Left;
        }
        
        if (firstVisit != null)
        {
            firstVisit.getClosestNeighbors(neighborList, position0, sqRange);
        }
        
        double[] line = RVO.vectorSubstract(nextVertex.position(), vertex.position());
        
        final double distSqLine = Math.pow(distanceToLine, 2) / RVO.vectorProduct(line, line);
        
        if (distSqLine < sqRange)
        {
            if (distanceToLine < 0)
            {
                /*
                 * Try obstacle at this node only if agent is on right side of
                 * obstacle (and can see obstacle).
                 */
                final double sqDistance = sqrDistancePointLineSegment(vertex.position(), nextVertex.position(), position0);
                
                if (sqDistance < sqRange)
                {
                    // add to list of closest obstacles
                    if (!neighborList.containsKey(sqDistance))
                    {
                        Vector<KDNodeObstacle> nodes = new Vector<KDNodeObstacle>();
                        nodes.add(this);

                        neighborList.put(sqDistance, nodes);
                    } 
                    else
                    {
                        neighborList.get(sqDistance).add(this);
                    }
                }
            }

            if (secondVisit != null)
            {
                secondVisit.getClosestNeighbors(neighborList, position0, sqRange);
            }
        }
    }
    
    private static ObstacleVertex optimalSplitPoint(List<ObstacleVertex> vertices)
    {
        ObstacleVertex optimalSplit = null;

        int minLeft  = vertices.size();
        int minRight = vertices.size();

        for (ObstacleVertex vertex: vertices)
        {
            int leftSize  = 0;
            int rightSize = 0;

            ObstacleVertex nextVertex = vertex.nextVertex;

            for (ObstacleVertex vertexA: vertices)
            {
                if (vertex != vertexA)
                {
                    ObstacleVertex vertexB = vertexA.nextVertex;

                    final double AtoEdge = Obstacle.distanceToLine(vertex.position(), 
                                                                   nextVertex.position(), 
                                                                   vertexA.position());

                    final double BtoEdge = Obstacle.distanceToLine(vertex.position(), 
                                                                   nextVertex.position(),
                                                                   vertexB.position());

                    if (AtoEdge >= -RVO.EPSILON && BtoEdge >= -RVO.EPSILON)
                    {
                        ++leftSize;
                    }
                    else if (AtoEdge <= RVO.EPSILON  && BtoEdge <= RVO.EPSILON)
                    {
                        ++rightSize;
                    }
                    else
                    {
                        ++leftSize;
                        ++rightSize;
                    }

                    int max = Math.max(leftSize, rightSize);
                    int min = Math.min(leftSize, rightSize);

                    int currentGreatestMin = Math.max(minLeft, minRight);
                    int currentSmallestMin = Math.min(minLeft, minRight);

                    if (max > currentGreatestMin ||
                       (max == currentGreatestMin && min >= currentSmallestMin))
                    {
                        break;
                    }
                }
            }

            int max = Math.max(leftSize, rightSize);
            int min = Math.min(leftSize, rightSize);

            int currentGreatestMin = Math.max(minLeft, minRight);
            int currentSmallestMin = Math.min(minLeft, minRight);

            if ( max < currentGreatestMin ||
                (max == currentGreatestMin && min < currentSmallestMin) )
            {
                minLeft      = leftSize;
                minRight     = rightSize;
                optimalSplit = vertex;
            }
        }

        return optimalSplit;
    }
    
    private void splitObstacleVertices(List<ObstacleVertex> vertices, ObstacleVertex optimalSplit)
    {
        leftVertices  = new ArrayList<ObstacleVertex>();
        rightVertices = new ArrayList<ObstacleVertex>();

        final ObstacleVertex nextOfOptimalSplit = optimalSplit.nextVertex;

        for (ObstacleVertex vertexA: vertices)
        {
            if (optimalSplit != vertexA)
            {
                ObstacleVertex vertexB = vertexA.nextVertex;

                final double AtoEdge = Obstacle.distanceToLine(optimalSplit.position(), 
                                                               nextOfOptimalSplit.position(), 
                                                               vertexA.position());

                final double BtoEdge = Obstacle.distanceToLine(optimalSplit.position(), 
                                                               nextOfOptimalSplit.position(), 
                                                               vertexB.position());

                if (AtoEdge >= -RVO.EPSILON && BtoEdge >= -RVO.EPSILON)
                {
                    leftVertices.add(vertexA);
                }
                else if (AtoEdge <= RVO.EPSILON && BtoEdge <= RVO.EPSILON)
                {
                    rightVertices.add(vertexA);
                }
                else
                {
                    double[] splitLine       = RVO.vectorSubstract(nextOfOptimalSplit.position(), optimalSplit.position());
                    double[] AtoOptimalSplit = RVO.vectorSubstract(vertexA.position(), optimalSplit.position());
                    double[] ABLine          = RVO.vectorSubstract(vertexA.position(), vertexB.position());

                    // Split edge AB
                    final double splitRatio  = RVO.det2D(splitLine, AtoOptimalSplit) / RVO.det2D(splitLine, ABLine);

                    ObstacleVertex newVertex = Obstacle.splitEdge(vertexA, vertexB, splitRatio);

                    if (AtoEdge > 0)
                    {
                        leftVertices.add(vertexA);
                        rightVertices.add(newVertex);
                    }
                    else
                    {
                        rightVertices.add(vertexA);
                        leftVertices.add(newVertex);
                    }
                }

            }
        }
    }
    
    /**
     * Computes the squared distance from a line segment with the  specified end points to a specified point.
     * @param endpoint1 : The first end point of the line segment.
     * @param endpoint2 : The second end point of the line segment.
     * @param point : The point to which the squared distance is to be calculated.
     * @return The squared distance from the line segment to the point.
     */
    private static double sqrDistancePointLineSegment(double[] endpoint1, double[] endpoint2, double[] point)
    {
        double[] pointToEndpoint1 = RVO.vectorSubstract(point, endpoint1);
        double[] pointToEndpoint2 = RVO.vectorSubstract(point, endpoint2);
        double[] line             = RVO.vectorSubstract(endpoint2, endpoint1);
        
        final double ratio = RVO.vectorProduct(pointToEndpoint1, line) / RVO.vectorProduct(line, line);
        
        if (ratio < 0)
        {
            return RVO.vectorProduct(pointToEndpoint1, pointToEndpoint1);
        }
        else if (ratio > 1)
        {
            return RVO.vectorProduct(pointToEndpoint2, pointToEndpoint2);
        }

        double[] projectionOfPoint = endpoint1.clone();
        
        for (int i = 0; i < projectionOfPoint.length; ++i)
        {
            projectionOfPoint[i] += (ratio * line[i]);
        }
        
        return RVO.vectorProduct(RVO.vectorSubstract(point, projectionOfPoint), 
                                 RVO.vectorSubstract(point, projectionOfPoint));
    }
    
    private ObstacleVertex vertex;
    KDNodeObstacle Left;
    KDNodeObstacle Right;

    private List<ObstacleVertex> leftVertices;
    private List<ObstacleVertex> rightVertices;
}
