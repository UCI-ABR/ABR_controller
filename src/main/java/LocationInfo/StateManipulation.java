package LocationInfo;

/**
 * Created by Joseph on 8/24/2017.
 * This modules takes inputs from sensors (i.e. compass, odometers, IRs) to update the measured state matrix.
 * Setup Methods:
 *          -setStartLocationInfo
 *          -setLocationInfo
 * Output Methods:
 *          -setLocationInfo
 *          -setMeasuredVelocity
 *          -getLocationInfo
 */

public class StateManipulation extends Mapping {
    public void setStartLocationInfo(double[] stateMatrix){
        startStateMatrix = stateMatrix;
        createMap();
    }

    public void setLocationInfo(double[] stateMatrix) {
        measuredStateMatrix = stateMatrix;
        updateRadianCompass();
        updateMap();
    }

    public void isMoving(boolean move, boolean turn) {
        doMove = move;
        doTurn = turn;
    }

    public double[] getLocationInfo() {
        calculatePosition();
//        calculateOrientation();
        calculateSpeed();
//        calculateAngularSpeed();
        return measuredStateMatrix; }

    //update location in map
    private void calculatePosition() {
        if (stepCounter1 >= unit_distance || stepCounter2 >= unit_distance) {
            //heading north
            if (compass >= 330 || compass < 30) {
                measuredStateMatrix[0] = measuredStateMatrix[0];
                measuredStateMatrix[1] = measuredStateMatrix[1] + step_size;
            }
            //heading north east
            else if (compass >= 30 && compass < 60) {
                measuredStateMatrix[0] = measuredStateMatrix[0] + step_size;
                measuredStateMatrix[1] = measuredStateMatrix[1] + step_size;
            }
            //heading east
            else if (compass >= 60 && compass < 120) {
                measuredStateMatrix[0] = measuredStateMatrix[0] + step_size;
                measuredStateMatrix[1] = measuredStateMatrix[1];
            }
            //heading south east
            else if (compass >= 120 && compass < 150) {
                measuredStateMatrix[0] = measuredStateMatrix[0] + step_size;
                measuredStateMatrix[1] = measuredStateMatrix[1] - step_size;
            }
            //heading south
            else if (compass >= 150 && compass < 210) {
                measuredStateMatrix[0] = measuredStateMatrix[0];
                measuredStateMatrix[1] = measuredStateMatrix[1] - step_size;
            }
            //heading south west
            else if (compass >= 210 && compass < 240) {
                measuredStateMatrix[0] = measuredStateMatrix[0] - step_size;
                measuredStateMatrix[1] = measuredStateMatrix[1] - step_size;
            }
            //heading west
            else if (compass >= 240 && compass < 300) {
                measuredStateMatrix[0] = measuredStateMatrix[0] - step_size;
                measuredStateMatrix[1] = measuredStateMatrix[1];
            }
            //heading north west
            else if (compass >= 300 && compass < 330) {
                measuredStateMatrix[0] = measuredStateMatrix[0] - step_size;
                measuredStateMatrix[1] = measuredStateMatrix[1] + step_size;
            }
        } else {
            measuredStateMatrix[0] = measuredStateMatrix[0];
            measuredStateMatrix[1] = measuredStateMatrix[1];
        }
    }

    //get direction of speed
    private void calculateSpeed() {
        int vx = 2, vy = 3;

        if (length == 6) {
            vx = 3;
            vy = 4;
        }

        if (doMove) {
            //facing north
            if (compass >= 330 || compass < 30) {
                measuredStateMatrix[vx] = speedOff;
                measuredStateMatrix[vy] = speedOn;
            }
            //facing north east
            else if (compass >= 30 && compass < 60) {
                measuredStateMatrix[vx] = speedOn;
                measuredStateMatrix[vy] = speedOn;
            }
            //facing east
            else if (compass >= 60 && compass < 120) {
                measuredStateMatrix[vx] = speedOn;
                measuredStateMatrix[vy] = speedOff;
            }
            //facing south east
            else if (compass >= 120 && compass < 150) {
                measuredStateMatrix[vx] = speedOn;
                measuredStateMatrix[vy] = -speedOn;
            }
            //facing south
            else if (compass >= 150 && compass < 210) {
                measuredStateMatrix[vx] = speedOff;
                measuredStateMatrix[vy] = -speedOn;
            }
            //facing south west
            else if (compass >= 210 && compass < 240) {
                measuredStateMatrix[vx] = -speedOn;
                measuredStateMatrix[vy] = -speedOn;
            }
            //facing west
            else if (compass >= 240 && compass < 300) {
                measuredStateMatrix[vx] = -speedOn;
                measuredStateMatrix[vy] = speedOff;
            }
            //facing north west
            else if (compass >= 300 && compass < 330) {
                measuredStateMatrix[vx] = -speedOn;
                measuredStateMatrix[vy] = speedOn;
            }
        } else {
            measuredStateMatrix[vx] = speedOff;
            measuredStateMatrix[vy] = speedOff;
        }
    }

    //update compass orientation
    private void calculateOrientation() {
        if (turnCounter1 < 0 && turnCounter2 < 0) {
            if (!(turnCounter1 > -unit_rotation && turnCounter2 > -unit_rotation)) {
                measuredStateMatrix[2] = measuredStateMatrix[2] - unit_radians;
            } else {
                measuredStateMatrix[2] = measuredStateMatrix[2];
            }
        } else {
            if (!(turnCounter1 < unit_rotation && turnCounter2 < unit_rotation)) {
                measuredStateMatrix[2] = measuredStateMatrix[2] + unit_radians;
            } else {
                measuredStateMatrix[2] = measuredStateMatrix[2];
            }
        }
    }

    private void calculateAngularSpeed() {
        if (doTurn) {
            measuredStateMatrix[5] = rotateOn;
        } else {
            measuredStateMatrix[5] = rotateOff;
        }
    }
}
