package clearpath;

import java.util.ArrayList;
import java.util.List;

public class Obstacle
{   
    public Obstacle(List<double[]> points)
    {
        assert(points.size() >= 2);
        
        vertices = new ArrayList<ObstacleVertex>();

        for (int i = 0; i < points.size(); ++i)
        {
            ObstacleVertex vertex = new ObstacleVertex(points.get(i));

            if (i != 0)
            {
                vertex.previousVertex             = vertices.get(vertices.size());
                vertex.previousVertex.nextVertex  = vertex;
            }

            if (i == (points.size() - 1))
            {
                vertex.nextVertex                = vertices.get(0);
                vertex.nextVertex.previousVertex = vertex;
            }
            
            double[] nextVertexPosition = points.get((i + 1) % points.size());
            
            double[] direction = RVO.vectorSubstract(nextVertexPosition, points.get(i));

            vertex.unitDirection = RVO.scalarProduct(direction, 
                                                     Math.sqrt(RVO.vectorProduct(direction, direction)));

            if (points.size() == 2)
            {
                vertex.isConvex = true;
            }
            else
            {
                double[] previousVertexPosition = vertex.previousVertex.position;
                
                double distanceNextVertex = distanceToLine(previousVertexPosition, points.get(i), nextVertexPosition);
                
                vertex.isConvex = (distanceNextVertex >= 0.0);
            }
            
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
     * @return the distance from point to a line of 2 vertices
     */
    public static double distanceToLine(double[] vertex1, double[] vertex2, double[] point)
    {
        return RVO.det2D(RVO.vectorSubstract(vertex1, point), 
                         RVO.vectorSubstract(vertex2, point));
    }
    
    public List<ObstacleVertex> getVertices()
    {
        return vertices;
    }
    
    private List<ObstacleVertex> vertices;
}
