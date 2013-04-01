package de.imwatchforum.shaketounlock;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.imwatchforum.shaketounlock.R;

public class ShakeToUnlockActivity extends Activity implements OnClickListener {

    private Button button;
    private EditText edit;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity);
        
        edit = (EditText) findViewById(R.id.s_edit);
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
		
		if(!isServiceRunning()) {
			Bundle bundle = new Bundle();
			int time = Integer.parseInt(edit.getText().toString());
			
			if(time <= 0) {
				time = 4;
			}
			
			bundle.putInt("time", time);
			
			intent.putExtras(bundle);
		    
			buttonText += "\n" + getResources().getString(R.string.button_on);
		    startService(intent);
		} else {
		    buttonText += "\n" + getResources().getString(R.string.button_off);
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
}
