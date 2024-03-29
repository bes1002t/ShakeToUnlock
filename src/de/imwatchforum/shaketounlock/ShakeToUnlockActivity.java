package de.imwatchforum.shaketounlock;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.imwatchforum.shaketounlock.R;

public class ShakeToUnlockActivity extends Activity implements OnClickListener {

    private Button button;
    private EditText timeEdit;

    private boolean mBound = false;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity);
       
        timeEdit = (EditText) findViewById(R.id.timeEdit);
        button = (Button) findViewById(R.id.on_off_button);
        button.setOnClickListener(this);
        
        String buttonText = getResources().getString(R.string.button);
        if(isServiceRunning()) {
            buttonText += "\n" + getResources().getString(R.string.button_on);
        } else {
            buttonText += "\n" + getResources().getString(R.string.button_off);
        }
        
        button.setText(buttonText);
    }

    
    public void onClick(View v) {
		String buttonText = getResources().getString(R.string.button);
	
		Intent intent = new Intent(this, ShakeToUnlockService.class);
		
		if(!mBound && !isServiceRunning()) {
			Bundle bundle = new Bundle();
			int time = Integer.parseInt(timeEdit.getText().toString());
			
			if(time <= 0) {
				time = 4;
			}
			
			bundle.putInt("time", time);
			
			intent.putExtras(bundle);
		    
			buttonText += "\n" + getResources().getString(R.string.button_on);
		    startService(intent);
		    bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		} else {
		    buttonText += "\n" + getResources().getString(R.string.button_off);
		    
		    unbindService(mConnection);
		    stopService(intent);
		}
		
		button.setText(buttonText);
    }
    
    
    private boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
		    if (ShakeToUnlockService.class.getName().equals(service.service.getClassName())) {
		    	return true;
		    }
		}
		
		return false;
    }
    
    
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

}
