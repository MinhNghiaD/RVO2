package kdtree;

import java.util.ArrayList;
import java.util.List;

import clearpath.Obstacle;
import clearpath.ObstacleVertex;
import clearpath.RVO;

public class KDNodeObstacle
{
    KDNodeObstacle()
    {
        
    }
    
    private static ObstacleVertex optimalSplitPoint(List<Obstacle> obstacles)
    {
        ObstacleVertex optimalSplit = null;
        
        int minLeft  = obstacles.size();
        int minRight = obstacles.size();
        
        for (Obstacle obstacle : obstacles)
        {
            int leftSize  = 0;
            int rightSize = 0;
            
            for (ObstacleVertex vertex: obstacle.getVertices())
            {
                ObstacleVertex nextVertex = vertex.nextVertex;
                
                for (Obstacle otherObstacle : obstacles)
                {
                    for (ObstacleVertex vertexA: otherObstacle.getVertices())
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
                }
                
                int max = Math.max(leftSize, rightSize);
                int min = Math.min(leftSize, rightSize);
                
                int currentGreatestMin = Math.max(minLeft, minRight);
                int currentSmallestMin = Math.min(minLeft, minRight);
                
                if ( max < currentGreatestMin ||
                    (max == currentGreatestMin && min < currentSmallestMin) )
                {
                    minLeft = leftSize;
                    minRight = rightSize;
                    optimalSplit = vertex;
                }
            }
        }

        return optimalSplit;
    }
    
    private void splitObstacleVertices(List<Obstacle> obstacles, ObstacleVertex optimalSplit)
    {
        leftVertices  = new ArrayList<ObstacleVertex>();
        rightVertices = new ArrayList<ObstacleVertex>();
        
        final ObstacleVertex nextOfOptimalSplit = optimalSplit.nextVertex;

        for (Obstacle obstacle: obstacles)
        {
            for (ObstacleVertex vertexA: obstacle.getVertices())
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

                        ObstacleVertex newVertex = obstacle.splitEdge(vertexA, vertexB, splitRatio);

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
    }

    private List<ObstacleVertex> leftVertices;
    private List<ObstacleVertex> rightVertices;
}
