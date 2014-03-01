package com.me.smsrpg;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.content.BroadcastReceiver;
import android.util.Log;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;

public class SMSListener extends BroadcastReceiver{

    private SharedPreferences preferences;
	GetsMessages smsRPG;
	
	public SMSListener(GetsMessages smsRPG)
	{
		this.smsRPG = smsRPG;
	}

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
		Log.d("something happened", "great!");
		
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
						this.smsRPG.getMessage(msg_from, msgBody);
                    }
                }catch(Exception e){
					Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }
}