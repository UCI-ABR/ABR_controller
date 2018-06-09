package LocationInfo;

import android.util.Log;

/**
 * Created by Joseph on 8/24/2017.
 * This module uses an external compass to calculate bearing to determine bearing-direction.
 * Setup Methods:
 *              -setDegreeCompass
 * Output Methods:
 *              -getReturnOrientation
 *              -getCompass
 */

class CompassFunctions extends Variables {
    void updateRadianCompass() {
        setStartOrientation(0);
        //setOffsetNorth();
        //measuredStateMatrix[2] = compassRadians;
        if (turnCounter1 < 0 && turnCounter2 < 0) {
            if (!(turnCounter1 > -unit_rotation && turnCounter2 > -unit_rotation)) {
                compassRadians = (compassRadians + (7 * unit_radians)) % (2 * Math.PI);
                Log.i("kf: Turning","LEFT");
            }
        } else {
            if (!(turnCounter1 < unit_rotation && turnCounter2 < unit_rotation)) {
                compassRadians = (compassRadians + unit_radians) % (2 * Math.PI);
                Log.i("kf: Turning","RIGHT");
            }
        }
        setDegreeCompass();
    }
    private void setStartOrientation(double external_compass) {
        this.startOrientation = this.startOrientation == -10 ? external_compass : this.startOrientation;
    }

    private void setOffsetNorth() {
        double offset = startOrientation - compassRadians;

        if (offset < 0) {
            compassRadians = Math.abs(offset);
        } else {
            compassRadians = Math.abs((2* Math.PI) - offset);
        }
    }

    private void setDegreeCompass() { compass = (float)(Math.toDegrees(compassRadians)+360)%360;
        //Log.i("kf: DEBUG: COMPASS", ""+compass);
    }

    //Uses start & current positions; returns [0,90]
    private double getOrientationRelativeToMap () {
        double startX = Math.round(startStateMatrix[0]);
        double startY = Math.round(startStateMatrix[1]);
        double currX = Math.round(measuredStateMatrix[0]);
        double currY = Math.round(measuredStateMatrix[1]);

        return Math.abs(Math.atan((currX - startX)/(currY - startY))) * (float) (180/ Math.PI);
    }

    //returns [180,270]
    public double getReturnOrientation() {
        double orientation = getOrientationRelativeToMap() + 180;

        switch ((int) Math.round(orientation/45)) {
            case 4:
                orientation = 180;
                break;
            case 5:
                orientation = 225;
                break;
            case 6:
                orientation = 270;
                break;
            default:
                orientation = 180;
                //Log.i("kf:","invalid orientation: " + (int) Math.round(orientation/45));
        }

        //Log.i("kf: DEBUG: Orientation",""+orientation);
        return orientation;
    }

    public double getCompass() {
        //Log.i("kf: COMPASS",""+compass);
        return compass; }
}
