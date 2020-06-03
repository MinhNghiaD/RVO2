package model;

public final class Constants
{
    public static String SIM                = "Simulation";
    public static String ANA                = "Analyse";
    public static String ENV                = "Environnement";

    public static int    FRAME_SIZE         = 700;
    public static int    GRID_SIZE          = 70;
    public static int    DISCRETIZATION     = 10;

    public static double POS_HEAD_GRID      = GRID_SIZE * 0.1;
    public static double POS_MIDDLE_GRID    = GRID_SIZE * 0.5;
    public static double POS_TAIL_GRID      = GRID_SIZE * 0.9;

    public static int    NUM_AGENT          = 10;
    public static double SCALE_AGENT      	= 1;
    public static double PI                 = 3.14159265358979323846f;

    public static double TIME_STEP          = 0.25;
    public static double TIME_HORIZON       = 5;
    public static double MAX_SPEED          = 1;
    public static double[] VELOCITY         = {0, 0};
    
    public static double DIST_NEIGHBOR      = 15;
    public static int    MAX_NEIGHBOR       = 10;

    /**
     * Return a case in grid by the percentage
     */
    public static double pctToIdxGrid(double pct) {
        return GRID_SIZE * pct * 0.01;
    }
}
