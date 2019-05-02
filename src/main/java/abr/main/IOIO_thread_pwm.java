package abr.main;

import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import android.util.Log;

public class IOIO_thread_pwm extends IOIO_thread
{
	private PwmOutput pwm_left, pwm_right, servo_pan, servo_tilt;
	private final int MAX_PWM = 2000;
	private final int MIN_PWM = 1000;
	private int left_val, right_val;

	@Override
	public void setup() throws ConnectionLostException 
	{
		try 
		{
			pwm_left = ioio_.openPwmOutput(3, 50); //move input, S1 on motor controller
			pwm_right = ioio_.openPwmOutput(4, 50); //turn input, S2 on motor controller

			servo_pan = ioio_.openPwmOutput(5, 50); //pan input
			servo_tilt = ioio_.openPwmOutput(6, 50); //tilt input
			servo_pan.setPulseWidth(1500); //fix pan to default position
			servo_tilt.setPulseWidth(1500); //fix tilt to default position

            left_val = 1500;
            right_val = 1500;
		} 
		catch (ConnectionLostException e){throw e;}
	}

	/**
	 *
	 * @param value pulse width of move input
	 */
	public synchronized void move(int value)
	{
        if(value > MAX_PWM)
            left_val = MAX_PWM;
        else if(value < MIN_PWM)
            left_val = MIN_PWM;
        else if(pwm_left != null)
            left_val = value;
	}

	/**
	 * @param value pulse width of turn input
	 */
	public synchronized void turn(int value)
	{
        if(value > MAX_PWM)
            right_val = MAX_PWM;
        else if(value < MIN_PWM)
            right_val = MIN_PWM;
        else if(pwm_right != null)
            right_val = value;
	}
    @Override
    public void loop() throws ConnectionLostException
    {
        ioio_.beginBatch();
        try
        {
            servo_pan.setPulseWidth(1500); //fix pan to default position
            servo_tilt.setPulseWidth(1500); //fix tilt to default position
            pwm_left.setPulseWidth(left_val);
            pwm_right.setPulseWidth(right_val);
			Log.i("fug",left_val+","+right_val);
            Thread.sleep(10);
        }
        catch (InterruptedException e){ ioio_.disconnect();}
        finally{ ioio_.endBatch();}
    }
}
