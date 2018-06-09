package LocationInfo;

import KalmanFilter.Equations;
import abr.main.IOIO_thread_rover_tank;


/**
 * Created by Joseph on 8/24/2017.
 * This module contains all of the variables used by each module in the LocationInfo package
 */

public class Variables {
    private static Equations m_pmts = new Equations();
    static int length = m_pmts.getStateLength();

    private IOIO_thread_rover_tank m_ioio_thread = new IOIO_thread_rover_tank();
    double ProximityThreshold = m_ioio_thread.ProximityThreshold;

    static double[] startStateMatrix = new double[length];
    static double[] measuredStateMatrix = new double[length];

    //Measured Speeds of Robot
    static boolean doMove, doTurn;
    final double speedOn = m_pmts.getForwardSpeed();
    final double speedOff = m_pmts.getForwardStop();
    final double rotateOn = m_pmts.getTurnSpeed();
    final double rotateOff = m_pmts.getTurnStop();

    //Map Variables
    public static final int map_size = 7;      //Size of NxN matrix map
    double step_size = 0.5;                    //Size of steps in map
    static int[][] map = new int[map_size][map_size];
    /**Map Legend**/
    static final int light_area = -1;
    static final int unexplored_area = 0;
    static final int free_space = 1;
    static final int obstacle = 2;

    //Compass Variables
    double compass = 0, compassRadians = 0;
    double startOrientation = -10;

    double unit_distance = 218;   //30 cm for odometers
    double unit_rotation = 124;   //45 degrees for odometers
    double unit_radians = 0.785;    //45 degrees in radians

    float irLeft = 0, irCenter = 0, irRight = 0;
    double stepCounter1, stepCounter2, turnCounter1, turnCounter2;

    public void setIrSensors(float leftIR, float centerIR, float rightIR) {
        irLeft = leftIR;
        irCenter = centerIR;
        irRight = rightIR;
    }

    public void setOdometrySensors(double sensor1, double sensor2, double sensor3, double sensor4) {
        stepCounter1 = sensor1;
        stepCounter2 = sensor2;
        turnCounter1 = sensor3;
        turnCounter2 = sensor4;
    }

    public double getUnit_distance() { return unit_distance; }
    public double getUnit_rotation() { return unit_rotation; }
}
