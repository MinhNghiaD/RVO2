package kdtree;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import clearpath.Obstacle;
import clearpath.ObstacleVertex;

public class KDTreeObstacle
{
    public KDTreeObstacle(List<Obstacle> obstacles)
    {
        this.obstacles = obstacles;
        
        List<ObstacleVertex> vertices = new ArrayList<ObstacleVertex>();
        
        for (Obstacle obstacle: obstacles)
        {
            vertices.addAll(obstacle.getVertices());
        }
        
        if (vertices.isEmpty())
        {
            root = null;
        }
        else
        {
            root = new KDNodeObstacle(vertices);
        }
    }
    
    public List<Obstacle> getObstacles()
    {
        return obstacles;
    }
    
    public TreeMap<Double, Vector<KDNodeObstacle>> getClosestObstacles(double[] position, double sqRange)
    {
        TreeMap<Double, Vector<KDNodeObstacle>> closestObstacle = new TreeMap<Double, Vector<KDNodeObstacle>>();
        
        root.getClosestNeighbors(closestObstacle, position, sqRange);
    
        return closestObstacle;
    }
    
    
    private KDNodeObstacle root;
    private List<Obstacle> obstacles;
}
