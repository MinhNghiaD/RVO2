package clearpath;

public class Obstacle
{
    Obstacle()
    {
        isConvex = false;
        
        //nextObstacle = null;
        //prevObstacle = null;
    }
    
    Obstacle(double[] point, double[] unitDir)
    {
        this.isConvex = false;
        this.point    = point.clone();
        this.unitDir  = unitDir.clone();
    }
    
    
    
    //Obstacle nextObstacle;
    //Obstacle prevObstacle;
    
    boolean isConvex;
    double[] point;
    double[] unitDir;
}
