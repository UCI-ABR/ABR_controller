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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import abr.main.R;

public class Main_activity extends Activity implements IOIOLooperProvider 		// implements IOIOLooperProvider: from IOIOActivity
{
	private final IOIOAndroidApplicationHelper helper_ = new IOIOAndroidApplicationHelper(this, this);			// from IOIOActivity

	RelativeLayout layout_left_joystick, layout_right_joystick;
	ImageView image_joystick, image_border;

	JoyStickClass js_left, js_right;
	
	IOIO_thread m_ioio_thread;

	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		helper_.create();		// from IOIOActivity

		layout_left_joystick = (RelativeLayout)findViewById(R.id.layout_left_joystick); //inverted for some reason
		layout_right_joystick = (RelativeLayout)findViewById(R.id.layout_right_joystick ); //inverted for some reason
	
		js_left = new JoyStickClass(getApplicationContext(), layout_left_joystick, R.drawable.image_button);
		js_left.setStickSize(150, 150);
//		js_left.setLayoutSize(300, 300);
		js_left.setLayoutAlpha(150);
		js_left.setStickAlpha(100);
		js_left.setOffset(90);
		js_left.setMinimumDistance(50);
		
		js_right = new JoyStickClass(getApplicationContext(), layout_right_joystick, R.drawable.image_button);
		js_right.setStickSize(150, 150);
//		js_left.setLayoutSize(300, 300);
		js_right.setLayoutAlpha(150);
		js_right.setStickAlpha(100);
		js_right.setOffset(90);
		js_right.setMinimumDistance(50);

		layout_left_joystick.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) 
			{
//				Log.e("abr controller", "left joystick");
				
				js_left.drawStick(arg1);
				if(arg1.getAction() == MotionEvent.ACTION_DOWN	|| arg1.getAction() == MotionEvent.ACTION_MOVE) 
				{
					int direction = js_left.get4Direction();
					if(direction == JoyStickClass.STICK_UP) 				m_ioio_thread.set_left_motor(1, IOIO_thread.FORWARD);
					else if(direction == JoyStickClass.STICK_DOWN) 			m_ioio_thread.set_left_motor(1, IOIO_thread.BACKWARD);
					else if(direction == JoyStickClass.STICK_NONE) 			m_ioio_thread.set_left_motor(0, IOIO_thread.FORWARD);
				}
				else if(arg1.getAction() == MotionEvent.ACTION_UP) //user stopped touching screen on layout
				{
					m_ioio_thread.set_left_motor(0, true);
				}
				return true;
			}
		});
		
		layout_right_joystick.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) 
			{
//				Log.e("abr controller", "right joystick");
				
				js_right.drawStick(arg1);
				if(arg1.getAction() == MotionEvent.ACTION_DOWN	|| arg1.getAction() == MotionEvent.ACTION_MOVE) 
				{
					int direction = js_right.get4Direction();
					if(direction == JoyStickClass.STICK_UP) 				m_ioio_thread.set_right_motor(1, IOIO_thread.FORWARD);
					else if(direction == JoyStickClass.STICK_DOWN) 			m_ioio_thread.set_right_motor(1, IOIO_thread.BACKWARD);
					else if(direction == JoyStickClass.STICK_NONE) 			m_ioio_thread.set_right_motor(0, IOIO_thread.FORWARD);
				}
				else if(arg1.getAction() == MotionEvent.ACTION_UP) //user stopped touching screen on layout
				{
					m_ioio_thread.set_right_motor(0, true);
				}
				return true;
			}
		});
	} 	

	/****************************************************** functions from IOIOActivity *********************************************************************************/

	/**
	 * Create the  {@link IOIO_thread}. Called by the {@link IOIOAndroidApplicationHelper}. <br>
	 * Function copied from original IOIOActivity.
	 * @see {@link #get_ioio_data()} {@link #start_IOIO()} 
	 * */
	@Override
	public IOIOLooper createIOIOLooper(String connectionType, Object extra) 
	{
		if(m_ioio_thread == null && connectionType.matches("ioio.lib.android.bluetooth.BluetoothIOIOConnection"))
		{
			m_ioio_thread = new IOIO_thread(this);
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
		Log.e("abr controller", "stopping...");
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
