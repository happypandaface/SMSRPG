package com.me.smsrpg;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.content.IntentFilter;
import java.util.HashMap;
import java.util.Map;
import android.widget.TextView;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.Context;
import android.content.ComponentName;
import android.os.IBinder;
import com.me.smsrpg.SMSService.LocalBinder;

public class SMSRPG extends Activity// implements GetsMessages, HasDungeons
{
    /** Called when the activity is first created. */
	
	private Map<String, SMSUser> users;
	private Map<String, Dungeon> dungeons;
	private ServiceConnection mConnection;
	String mainString;
	SMSService smsService;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		

		mConnection = new ServiceConnection() {

			public void onServiceDisconnected(ComponentName name) {
				// TODO Auto-generated method stub
				
			}

			public void onServiceConnected(ComponentName name, IBinder service) {
				// TODO Auto-generated method stub
				
				LocalBinder binder = (LocalBinder) service;
				smsService = binder.getService();
				
				mainString = smsService.getMessages();
				TextView tv = (TextView) findViewById(R.id.theText);
				tv.setText(mainString);
			}
		};
		
		
        setContentView(R.layout.main);
		
		//SMSService smsServ = new SMSService();
		// use this to start and trigger a service
		Intent servIntent = new Intent(this, SMSService.class);
		// potentially add data to the intent
		//i.putExtra("KEY1", "Value to be used by the service");
		this.startService(servIntent); 
		//startService(new Intent(this, SMSService.class));
		
		Intent callService = new Intent(this, SMSService.class);
		bindService(callService, mConnection, Context.BIND_ABOVE_CLIENT);
		
		
		
		// the main string is the string that holds all the text on the screen
		// it should scroll eventually
		mainString = "";
		addComment("App started");
    }
	
	public void addComment(String s)
	{
		mainString += s + "\n";
		TextView tv = (TextView) findViewById(R.id.theText);
		tv.setText(mainString);
	}
	
}