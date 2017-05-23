package abr.main;

import android.util.Log;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;

public class IOIO_thread_rover_tank extends IOIO_thread
{
    private PwmOutput pwm_left1, pwm_left2, pwm_right1,pwm_right2;
    private DigitalOutput dir_left1, dir_left2, dir_right1, dir_right2;
    float speed_left, speed_right;
    boolean direction_left, direction_right;

    @Override
    public void setup() throws ConnectionLostException
    {
        try
        {
            pwm_left1 = ioio_.openPwmOutput(3, 490); //motor channel 1: front left
            pwm_left2 = ioio_.openPwmOutput(5, 490); //motor channel 2: back left
			pwm_right1 = ioio_.openPwmOutput(7, 490); //motor channel 3: front right
			pwm_right2 = ioio_.openPwmOutput(10, 490); //motor channel 4: back right

			dir_left1 = ioio_.openDigitalOutput(2, true);	//motor channel 1: front left
			dir_left2 = ioio_.openDigitalOutput(4, true);	//motor channel 2: back left
			dir_right1 = ioio_.openDigitalOutput(6, true); //motor channel 3: front right
			dir_right2 = ioio_.openDigitalOutput(8, true); //motor channel 4: back right

            direction_left = false;
            direction_right = false;
            speed_left = 0;
            speed_right = 0;
        }
        catch (ConnectionLostException e){throw e;}
    }

    @Override
    public void loop() throws ConnectionLostException
    {
        ioio_.beginBatch();

        try
        {
			pwm_left1.setDutyCycle(speed_left);
			pwm_left2.setDutyCycle(speed_left);
			pwm_right1.setDutyCycle(speed_right);
			pwm_right2.setDutyCycle(speed_right);

			dir_left1.write(direction_left);
			dir_left2.write(!direction_left);
			dir_right1.write(direction_right);
			dir_right2.write(!direction_right);

            Thread.sleep(10);
        }
        catch (InterruptedException e){ ioio_.disconnect();}
        finally{ ioio_.endBatch();}
    }

    public synchronized void move(int value)
    {
        if (value > 1500) {
            speed_left = (float).5;
            speed_right = (float).5;
            direction_left = true;
            direction_right = true;
        } else if (value < 1500) {
            speed_left = (float).5;
            speed_right = (float).5;
            direction_left = false;
            direction_right = false;
        } else {
            speed_left = 0;
            speed_right = 0;
        }
    }

    public synchronized void turn(int value)
    {
        if (value > 1500) {
            speed_left = (float)1.0;
            speed_right = (float)1.0;
            direction_left = true;
            direction_right = false;
        } else if (value < 1500) {
            speed_left = (float)1.0;
            speed_right = (float)1.0;
            direction_left = false;
            direction_right = true;
        } else {
            speed_left = 0;
            speed_right = 0;
        }
    }
}