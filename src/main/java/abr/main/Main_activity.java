package abr.main;

import ioio.lib.util.IOIOLooper;
import ioio.lib.util.IOIOLooperProvider;
import ioio.lib.util.android.IOIOAndroidApplicationHelper;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

public class Main_activity extends Activity implements IOIOLooperProvider 		// implements IOIOLooperProvider: from IOIOActivity
{
	private final IOIOAndroidApplicationHelper helper_ = new IOIOAndroidApplicationHelper(this, this);			// from IOIOActivity
	RelativeLayout layout_left_joystick, layout_right_joystick;
	JoyStickClass js_left, js_right;
	private final int PWM = 0;
	private final int SERIAL = 1;
	private final int ROVER_4WD = 2;
	private final int ROVER_TANK = 3;
	private int mode;
	private String selectedCurrentDisplay;
	IOIO_thread m_ioio_thread;
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		helper_.create();		// from IOIOActivity

		selectedCurrentDisplay = getIntent().getExtras().getString("selectedCurrentDisplay");

		if(selectedCurrentDisplay.equals("RC Mode"))
			mode = PWM;
		else if(selectedCurrentDisplay.equals("Serial Mode"))
			mode = SERIAL;
		else if(selectedCurrentDisplay.equals("Rover 4WD Mode"))
			mode = ROVER_4WD;
		else
			mode = ROVER_TANK;


		layout_left_joystick = (RelativeLayout)findViewById(R.id.layout_left_joystick);
		layout_right_joystick = (RelativeLayout)findViewById(R.id.layout_right_joystick);
	
		js_left = new JoyStickClass(getApplicationContext(), layout_left_joystick, R.drawable.image_button);
		js_left.setStickSize(150, 150);
		js_left.setLayoutAlpha(150);
		js_left.setStickAlpha(100);
		js_left.setOffset(90);
		js_left.setMinimumDistance(50);
		
		js_right = new JoyStickClass(getApplicationContext(), layout_right_joystick, R.drawable.image_button);
		js_right.setStickSize(150, 150);
		js_right.setLayoutAlpha(150);
		js_right.setStickAlpha(100);
		js_right.setOffset(90);
		js_right.setMinimumDistance(50);

		layout_left_joystick.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) 
			{
				js_left.drawStick(arg1);
				if(arg1.getAction() == MotionEvent.ACTION_DOWN	|| arg1.getAction() == MotionEvent.ACTION_MOVE) 
				{
					/*
					int direction = js_left.get4Direction();
					if(direction == JoyStickClass.STICK_UP) 				m_ioio_thread.move(1600);
					else if(direction == JoyStickClass.STICK_DOWN) 			m_ioio_thread.move(1400);
					else if(direction == JoyStickClass.STICK_NONE) 			m_ioio_thread.move(1500);
					*/
					int pwm_move = 1500 + (int)(500*js_left.getnormY());
					//Log.i("haha","normy:"+js_left.getnormY());
					//Log.i("haha","pwm_move:"+pwm_move);
					//Log.i("haha","positionx:"+js_left.getX());
					//Log.i("haha","positiony:"+js_left.getY());
					int direction = js_left.get4Direction();
					if(direction == JoyStickClass.STICK_UP){
						m_ioio_thread.move(pwm_move);
					} else if(direction == JoyStickClass.STICK_DOWN) {
						m_ioio_thread.move(pwm_move);
					} else if(direction == JoyStickClass.STICK_NONE) {
						m_ioio_thread.move(1500);
					}
				}
				else if(arg1.getAction() == MotionEvent.ACTION_UP) //user stopped touching screen on layout
				{
					m_ioio_thread.move(1500);
				}
				return true;
			}
		});
		
		layout_right_joystick.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) 
			{
				js_right.drawStick(arg1);
				if(arg1.getAction() == MotionEvent.ACTION_DOWN	|| arg1.getAction() == MotionEvent.ACTION_MOVE) 
				{
					int pwm_turn = 1500 + (int)(500*js_right.getnormX());
					//Log.i("haha","normx:"+js_right.getnormX());
					//Log.i("haha","pwm_turn:"+pwm_turn);
					//Log.i("haha","positionx:"+js_right.getX());
					//Log.i("haha","positiony:"+js_right.getY());
					/*
					int direction = js_right.get4Direction();
					if(direction == JoyStickClass.STICK_RIGHT) 				m_ioio_thread.turn(1600);
					else if(direction == JoyStickClass.STICK_LEFT) 			m_ioio_thread.turn(1400);
					else if(direction == JoyStickClass.STICK_NONE) 			m_ioio_thread.turn(1500);
					*/
					int direction = js_right.get4Direction();
					if(direction == JoyStickClass.STICK_RIGHT){
						m_ioio_thread.turn(pwm_turn);
					} else if(direction == JoyStickClass.STICK_LEFT) {
						m_ioio_thread.turn(pwm_turn);
					} else if(direction == JoyStickClass.STICK_NONE) {
						m_ioio_thread.turn(1500);
					}
				}
				else if(arg1.getAction() == MotionEvent.ACTION_UP) //user stopped touching screen on layout
				{
					m_ioio_thread.turn(1500);
				}
				return true;
			}
		});
	} 	

	/****************************************************** functions from IOIOActivity *********************************************************************************/

	/**
	 * Create the  {@link IOIO_thread_pwm}. Called by the {@link IOIOAndroidApplicationHelper}. <br>
	 * Function copied from original IOIOActivity.
	 * */
	@Override
	public IOIOLooper createIOIOLooper(String connectionType, Object extra) 
	{
		if(m_ioio_thread == null && connectionType.matches("ioio.lib.android.bluetooth.BluetoothIOIOConnection"))
		{
			if(mode == PWM)
				m_ioio_thread = new IOIO_thread_pwm();
			else if(mode == SERIAL)
				m_ioio_thread = new IOIO_thread_serial();
			else if(mode == ROVER_4WD)
				m_ioio_thread = new IOIO_thread_rover_4wd();
			else
				m_ioio_thread = new IOIO_thread_rover_tank();
			return m_ioio_thread;
		}
		else return null;
	}

	@Override
	protected void onDestroy() 
	{
		helper_.destroy();
		super.onDestroy();
	}

	@Override
	protected void onStart() 
	{
		super.onStart();
		helper_.start();
	}

	@Override
	protected void onStop() 
	{
		helper_.stop();
		super.onStop();
	}

	@Override
	protected void onNewIntent(Intent intent) 
	{
		super.onNewIntent(intent);
		if ((intent.getFlags() & Intent.FLAG_ACTIVITY_NEW_TASK) != 0) 
		{
			helper_.restart();
		}
	}
}
