package kdtree;

import java.util.List;

import clearpath.Obstacle;
import clearpath.ObstacleVertex;
import clearpath.RVO;

public class KDTreeObstacle
{

    
    ObstacleVertex optimalSplitPoint(List<Obstacle> obstacles, Integer minLeft, Integer minRight)
    {
        ObstacleVertex optimalSplit = null;

        for (Obstacle obstacle : obstacles)
        {
            int leftSize  = 0;
            int rightSize = 0;
            
            for (ObstacleVertex vertex: obstacle.getVertices())
            {
                ObstacleVertex nextVertex = vertex.nextVertex;
                
                for (Obstacle otherObstacle : obstacles)
                {
                    for (ObstacleVertex otherVertex: otherObstacle.getVertices())
                    {
                        if (vertex != otherVertex)
                        {
                            ObstacleVertex otherNextVertex = otherVertex.nextVertex;
                            
                            final double j1LeftOfI = Obstacle.distanceToLine(vertex.position(), 
                                                                             nextVertex.position(), 
                                                                             otherVertex.position());
                            
                            final double j2LeftOfI = Obstacle.distanceToLine(vertex.position(), 
                                                                             nextVertex.position(),
                                                                             otherNextVertex.position());
                            
                            if (j1LeftOfI >= -RVO.EPSILON && j2LeftOfI >= -RVO.EPSILON)
                            {
                                ++leftSize;
                            }
                            else if (j1LeftOfI <= RVO.EPSILON  && j2LeftOfI <= RVO.EPSILON)
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
                
                if (max < currentGreatestMin ||
                   (max == currentGreatestMin && min < currentSmallestMin))
                {
                    minLeft = leftSize;
                    minRight = rightSize;
                    optimalSplit = vertex;
                }
            }
        }

        return optimalSplit;
    }
    
    private List<Obstacle> obstacles;
}
