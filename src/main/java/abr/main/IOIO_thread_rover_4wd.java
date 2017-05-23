package abr.main;

import android.util.Log;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;

public class IOIO_thread_rover_4wd extends IOIO_thread
{
    private PwmOutput pwm_left1, pwm_left2, pwm_right1,pwm_right2;
    private DigitalOutput dir_left1, dir_left2, dir_right1, dir_right2;
    float speed_left, speed_right;
    int move_val=1500,turn_val=1500;
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
            //This steering method will maximize the speed of the robot.
            if(move_val != 1500){
                direction_left = ((move_val - 1500) > 0);
                direction_right = ((move_val - 1500) > 0);
                speed_left = .3f;
                speed_right = .3f;
                if(turn_val > 1500){
                    speed_right -= .2;
                } else {
                    speed_left -= .2;
                }
            } else if(turn_val !=1500) {
                speed_left = 0.3f;
                speed_right = 0.3f;
                direction_left = ((turn_val - 1500) > 0);
                direction_right = ((turn_val - 1500) < 0);
            } else {
                speed_left = 0.0f;
                speed_right = 0.0f;
            }

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
        move_val = value;
    }

    public synchronized void turn(int value)
    {
        turn_val = value;
    }
}