package com.me.smsrpg;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.content.IntentFilter;
import java.util.HashMap;
import java.util.Map;
import android.widget.TextView;

public class SMSRPG extends Activity implements GetsMessages, HasDungeons
{
    /** Called when the activity is first created. */
	
	Map<String, SMSUser> users;
	Map<String, Dungeon> dungeons;
	private String mainString;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		
		dungeons = new HashMap<String, Dungeon>();
		
		users = new HashMap<String, SMSUser>();
		
        setContentView(R.layout.main);
		
		SMSListener smsl = new SMSListener(this);
		
		IntentFilter filter1 = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(smsl, filter1);
		
		Dungeon d1 = new Dungeon();
		d1.setName("scaaary dungeon");
		Room startRoom = d1.addRoom("starterRoom");
		startRoom.setDescription("You enter a very scaarry dungeon, the walls are very spooooky! There is a dusty haunted [tome] on the ground\n");
		//startRoom.define("tome", "item");
		d1.setStartRoom("starterRoom");
		addDungeon(d1);
		
		mainString = "";
		addComment("App started");
    }
	
	public void addComment(String s)
	{
		mainString += s + "\n";
		TextView tv = (TextView) findViewById(R.id.theText);
		tv.setText(mainString);
	}
	
	public Map<String, Dungeon> getDungeons()
	{
		return dungeons;
	}
	public Dungeon getDungeon(String nme)
	{
		for (Map.Entry<String, Dungeon> entry : dungeons.entrySet())
		{
			String key = entry.getKey();
			if (key.equals(nme))
				return entry.getValue();
		}
		return null;
	}
	
	public void addDungeon(Dungeon d)
	{
		dungeons.put(d.getName(), d);
	}
	
	public void getMessage(String msg_from, String msgBody)
	{
		String startChars = msgBody.substring(0, 2);
		if (startChars.equals("#?"))
		{
			String gameMsgRecv = msgBody.substring(2, msgBody.length());
			SMSUser usr = users.get(msg_from);
			if (usr == null)
			{
				usr = new SMSUser(msg_from, this);
				users.put(msg_from, usr);
			}
			addComment("recieved data from: "+usr.getAddr());
			try
			{
				usr.process(gameMsgRecv);
			}catch(Exception e)
			{
				addComment(e.toString());
			}
			
		}
	}
}