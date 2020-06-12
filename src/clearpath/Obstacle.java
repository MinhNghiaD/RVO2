package clearpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Obstacle
{   
    public Obstacle(List<double[]> points)
    {
        assert(points.size() >= 2);
        
        vertices = new ArrayList<ObstacleVertex>();
        
        ObstacleVertex previousVertex = null;

        for (int i = 0; i < points.size(); ++i)
        {
            ObstacleVertex vertex = new ObstacleVertex(points.get(i));
            
            //System.out.println("create vertex: " + Arrays.toString(points.get(i)));

            if (i != 0)
            {
                vertex.previousVertex             = previousVertex;
                vertex.previousVertex.nextVertex  = vertex;
            }

            if (i == (points.size() - 1))
            {
                vertex.nextVertex                = vertices.get(0);
                vertex.nextVertex.previousVertex = vertex;
            }
            
            double[] nextVertexPosition = points.get((i + 1) % points.size());     
            double[] direction          = RVO.vectorSubstract(nextVertexPosition, points.get(i));
            
            vertex.unitDirection = RVO.normalize(direction);

            if (points.size() == 2)
            {
                vertex.isConvex = true;
            }
            else
            {
                double[] previousVertexPosition;
                
                if (i == 0)
                {
                    previousVertexPosition = points.get(points.size() - 1);
                }
                else
                {
                    previousVertexPosition = points.get(i - 1);
                }
                
                double distanceNextVertex = distanceToLine(points.get((i == 0 ? points.size() - 1 : i - 1)), 
                                                           points.get(i), 
                                                           points.get((i == points.size() - 1 ? 0 : i + 1)));
                
                vertex.isConvex = (distanceNextVertex >= 0.0);
                
                System.out.println(" distanceNext" + distanceNextVertex + "is Convex" + vertex.isConvex);
                System.out.println(" unit direction" + Arrays.toString(vertex.unitDirection));
            }
            
            previousVertex = vertex;
            
            vertices.add(vertex);
        }
    }
    
    static public ObstacleVertex splitEdge(ObstacleVertex vertexA, ObstacleVertex vertexB, double ratio)
    {
        double[] splitPoint = vertexA.position().clone();
        
        for (int i = 0; i < splitPoint.length; ++i)
        {
            splitPoint[i] += (vertexB.position()[i] - vertexA.position()[i]) * ratio;
        }

        ObstacleVertex newVertex  = new ObstacleVertex(splitPoint);

        newVertex.isConvex        = true;
        newVertex.unitDirection   = vertexA.unitDirection;
        newVertex.previousVertex  = vertexA;
        newVertex.nextVertex      = vertexB;
        vertexA.nextVertex        = newVertex;
        vertexB.previousVertex    = newVertex;
        
        return newVertex;
    }
    
    /**
     * 
     * @param vertex1
     * @param vertex2
     * @param point
     * @return  signed distance from a line connecting the specified points to a specified point.
     */
    public static double distanceToLine(double[] vertex1, double[] vertex2, double[] point)
    {
        return RVO.det2D(RVO.vectorSubstract(vertex1, point), 
                         RVO.vectorSubstract(vertex2, vertex1));
    }
    
    public List<ObstacleVertex> getVertices()
    {
        return vertices;
    }
    
    private List<ObstacleVertex> vertices;
}
