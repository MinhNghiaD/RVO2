package agents;

import model.Model;
import clearpath.*;
import sim.engine.SimState;
import sim.util.Double2D;


public class AgentPeople extends AgentType
{
    public AgentPeople(double x, double y)
    {
        super(x, y);
    }

    public AgentPeople(double x, double y, double angle)
    {
        super(x, y);

        this.angle    = getRadianAngle(angle);

        Double2D direction2D = directionFromAngle(angle);

        double[] point     = { x, y };
        double[] direction = { direction2D.x, direction2D.y };
        this.line          = new Line(point, direction);
    }

    public AgentPeople(double sourceX, 
                       double sourceY, 
                       double directionX,
                       double directionY)
    {
        super(sourceX, sourceY);
        
        double[] point     = { sourceX, sourceY };
        double[] direction = { directionX, directionY };
        this.line          = new Line(point, direction);
    }
    
    public AgentPeople(CollisionAvoidanceManager controller)
    {
        super(controller.getPosition()[0], controller.getPosition()[1]);
        
        this.controller = controller;
        
    }

    @Override
    public void step(SimState state) 
    {
        Model beings = (Model) state;
        
        move(beings);
    }

    public void move(Model beings) 
    {
        double currX = this.x;
        double currY = this.y;

        x += this.line.getDirection()[0];
        y += this.line.getDirection()[1];

        x  = beings.getYard().stx(x);
        y  = beings.getYard().sty(y);

        beings.getYard().setObjectLocation(this, new Double2D(x, y));
        
        this.angle = angleFromDirection(y, x, currX, currY);
    }

    /**
     * Get radian angle from agent's position
     * 
     * @param y
     * @param x
     * @param currY
     * @param currX
     * @return
     */
    public static Double angleFromDirection(double y, double x, double oldX, double oldY) 
    {
        double res = Math.atan2(y - oldY, x - oldX);

        return res;
    }
    
    /**
     * Ensure angle is within 0-360 degrees
     * 
     * @param angle
     * @return modulated angle
     */
    private static double clampAngle(double angle) 
    {
        return angle % 360;
    }

    /**
     * Degrees to radians angle
     * 
     * @param angle
     * @return
     */
    private double getRadianAngle(double angle) 
    {
        return clampAngle(angle) / (2 * 3.14);
    }

    /**
     * Turn 360 degrees angle into discrete direction
     * 
     * @param angle in degrees
     * @return direction in (x,y)
     */
    private Double2D directionFromAngle(double angle) 
    {
        angle = clampAngle(angle);
        
        Double2D[] directions = { new Double2D(0, -1), new Double2D(1, -1), new Double2D(1, 0), new Double2D(1, 1), new Double2D(0, 1),  
                                  new Double2D(-1, 1), new Double2D(-1, 0), new Double2D(-1, -1), new Double2D(0, -1), };

        return directions[(int) Math.round(angle / 45)];
    }
    
    public double getAngle()
    {
        return angle;
    }
    
    private static final long serialVersionUID = 1L;
    private double            angle;
    private Line              line;
    private CollisionAvoidanceManager controller;
}
