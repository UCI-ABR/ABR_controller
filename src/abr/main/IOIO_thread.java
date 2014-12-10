package abr.main;

import android.util.Log;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;

public class IOIO_thread extends BaseIOIOLooper 
{
//	private PwmOutput pwm_left1, pwm_left2, pwm_right1, pwm_right2, servo_pan, servo_tilt;
	private PwmOutput pwm_left, pwm_right, servo_pan, servo_tilt;
	private DigitalOutput dir_left1, dir_left2, dir_right1, dir_right2;
	Main_activity the_gui;					// reference to the main activity

	float speed_left, speed_right;
	boolean direction_left, direction_right;
	int pwm_pan, pwm_tilt;
	int left, right;

	static final boolean FORWARD = true;
	static final boolean BACKWARD = false;

	public IOIO_thread(Main_activity gui)
	{
		the_gui = gui;

		speed_left = 0;
		speed_right = 0;
		direction_left = FORWARD;
		direction_right = FORWARD;
		pwm_pan = 1500;
		pwm_tilt= 1500;
//		pwm_tilt= 2200;
		left = 1500;
		right = 1500;
		
		Log.e("abr controller", "IOIO thread creation");
	}

	@Override
	public void setup() throws ConnectionLostException 
	{
		try 
		{
			pwm_left = ioio_.openPwmOutput(3, 50); //motor channel 4: front left
			pwm_right = ioio_.openPwmOutput(4, 50); //motor channel 3: back left
//			pwm_right1 = ioio_.openPwmOutput(5, 50); //motor channel 2: front right
//			pwm_right2 = ioio_.openPwmOutput(7, 50); //motor channel 1: back right
			servo_pan = ioio_.openPwmOutput(5, 50); //motor channel 1: back right;
			servo_tilt = ioio_.openPwmOutput(6, 50); //motor channel 1: back right;

//			dir_left1 = ioio_.openDigitalOutput(2, true);	//motor channel 4: front left
//			dir_left2 = ioio_.openDigitalOutput(4, true);	//motor channel 3: back left
//			dir_right1 = ioio_.openDigitalOutput(6, true); //motor channel 2: front right
//			dir_right2 = ioio_.openDigitalOutput(8, true); //motor channel 1: back right

			//			servo_pan.setPulseWidth(1500);
			//			servo_tilt.setPulseWidth(1500);

			Log.e("abr controller", "IOIO setup done");

		} 
		catch (ConnectionLostException e){throw e;}
	}

	@Override
	public void loop() throws ConnectionLostException 
	{
//		pwm_pan = 1500;
//		pwm_tilt= 2200; 
//		
//		if(speed_left > 0 && direction_left == FORWARD)		{ pwm_pan += 100; pwm_tilt -= 100;}
//		if(speed_left > 0 && direction_left == BACKWARD) 	{ pwm_pan -= 100; pwm_tilt += 100;}
//		if(speed_right > 0 && direction_right == FORWARD)	{ pwm_pan -= 100; pwm_tilt -= 100;}
//		if(speed_right > 0 && direction_right == BACKWARD) 	{ pwm_pan += 100; pwm_tilt += 100;}
		
		if(speed_left == 0) left = 1500;
		else
		{
			if(direction_left) left = 1600;
			else left = 1400;
		}
		
		if(speed_right == 0) right = 1500;
		else
		{
			if(direction_right) right = 1600;
			else right = 1400;
		}
				
		ioio_.beginBatch();
		try 
		{
//			pwm_left1.setDutyCycle(speed_left);			
//			pwm_left2.setDutyCycle(speed_left);
//			pwm_right1.setDutyCycle(speed_right);
//			pwm_right2.setDutyCycle(speed_right);
									
			pwm_left.setPulseWidth(left);	
			pwm_right.setPulseWidth(right);

			servo_pan.setPulseWidth(left);
			servo_tilt.setPulseWidth(right); //vertical position phone for phone holder
//
//			dir_left1.write(direction_left);
//			dir_left2.write(!direction_left);	//this motor must be wired incorrectly
//			dir_right1.write(direction_right);
//			dir_right2.write(direction_right);			

			Thread.sleep(10);			
		} 
		catch (InterruptedException e){ ioio_.disconnect();}
		finally{ ioio_.endBatch();}
	}

	/**
	 * 
	 * @param left_spd: value between 0 and 1 (pwm duty cycle)
	 * @param left_dir: true forward, false backward
	 * @param right_spd: value between 0 and 1 (pwm duty cycle)
	 * @param right_dir: true forward, false backward
	 */
	public synchronized void set_motor(float left_spd, boolean left_dir, float right_spd, boolean right_dir)
	{
		speed_left 		= left_spd;
		speed_right 	= right_spd;
		direction_left 	= left_dir;
		direction_right = right_dir;
	}

	/**
	 * 
	 * @param left_spd value between 0 and 1 (pwm duty cycle)
	 * @param left_dir true forward, false backward
	 */
	public synchronized void set_left_motor(float left_spd, boolean left_dir)
	{
		speed_left 		= left_spd;
		direction_left 	= left_dir;	
	}

	/**
	 * 
	 * @param right_spd value between 0 and 1 (pwm duty cycle)
	 * @param right_dir true forward, false backward
	 */
	public synchronized void set_right_motor(float right_spd, boolean right_dir)
	{
		speed_right 	= right_spd;
		direction_right = right_dir;
	}
}
