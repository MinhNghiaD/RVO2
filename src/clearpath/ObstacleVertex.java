package clearpath;

public class ObstacleVertex
{
    ObstacleVertex(double[] position)
    {
        this.position       = position.clone();
        this.unitDirection  = new double[2];
        this.nextVertex     = null;
        this.previousVertex = null;
        this.isConvex       = false;
    }
    
    
    boolean isConvex;
    double[] position;
    double[] unitDirection;
    
    ObstacleVertex nextVertex;
    ObstacleVertex previousVertex;
}
