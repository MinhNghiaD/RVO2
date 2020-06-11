package kdtree;

import java.util.ArrayList;
import java.util.List;

import clearpath.Obstacle;
import clearpath.ObstacleVertex;
import clearpath.RVO;

public class KDNodeObstacle
{
    KDNodeObstacle(List<ObstacleVertex> vertices)
    {
        vertex = optimalSplitPoint(vertices);
        
        splitObstacleVertices(vertices, vertex);
        
        Left  = new KDNodeObstacle(leftVertices);
        Right = new KDNodeObstacle(rightVertices);
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
                    double[] splitLine = RVO.vectorSubstract(nextOfOptimalSplit.position(), optimalSplit.position());
                    double[] AtoOptimalSplit = RVO.vectorSubstract(vertexA.position(), optimalSplit.position());
                    double[] ABLine = RVO.vectorSubstract(vertexA.position(), vertexB.position());

                    // Split edge AB
                    final double splitRatio = RVO.det2D(splitLine, AtoOptimalSplit) / RVO.det2D(splitLine, ABLine);

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
    
    private ObstacleVertex vertex;
    KDNodeObstacle Left;
    KDNodeObstacle Right;

    private List<ObstacleVertex> leftVertices;
    private List<ObstacleVertex> rightVertices;
}
