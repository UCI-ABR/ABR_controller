package abr.main;

import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;

public class IOIO_thread_pwm extends IOIO_thread
{
	private PwmOutput pwm_left, pwm_right, servo_pan, servo_tilt;
	private final int MAX_PWM = 2000;
	private final int MIN_PWM = 1000;

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

		} 
		catch (ConnectionLostException e){throw e;}
	}

	/**
	 *
	 * @param value pulse width of move input
	 */
	public synchronized void move(int value)
	{
		try {
			if(value > MAX_PWM)
				pwm_left.setPulseWidth(MAX_PWM);
			else if(value < MIN_PWM)
				pwm_left.setPulseWidth(MIN_PWM);
			else if(pwm_left != null)
				pwm_left.setPulseWidth(value);
		} catch (ConnectionLostException e) {
			ioio_.disconnect();
		}
	}

	/**
	 * @param value pulse width of turn input
	 */
	public synchronized void turn(int value)
	{
		try {
			if(value > MAX_PWM)
				pwm_right.setPulseWidth(MAX_PWM);
			else if(value < MIN_PWM)
				pwm_right.setPulseWidth(MIN_PWM);
			else if(pwm_right != null)
				pwm_right.setPulseWidth(value);
		} catch (ConnectionLostException e) {
			ioio_.disconnect();
		}
	}

}
