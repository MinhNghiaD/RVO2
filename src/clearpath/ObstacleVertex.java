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
    
    public boolean isConvex()
    {
        return isConvex;
    }
    
    public double[] position()
    {
        return position.clone();
    }
    
    public double[] unitDirection()
    {
        return unitDirection.clone();
    }
    
    
    boolean isConvex;
    double[] position;
    double[] unitDirection;
    
    public ObstacleVertex nextVertex;
    public ObstacleVertex previousVertex;
}
