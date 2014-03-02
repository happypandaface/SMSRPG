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
			startRoom.setDescription("You enter a very scaarry dungeon, the walls are very spooooky! There is a dusty haunted [tome] on the ground and a path [north] going further into the dungeon.\n");
			Item tome = startRoom.define("tome", new Item(), Item.class);
				tome.setDescription("You pick up the tome and blow the dust off the cover. Opening it up you find many words in an ancient text. You skim to the end, the last page has a few words in english:\nDon't look into the-\n the page is torn, cutting the warning short.");
			Direction north = startRoom.define("north", new Direction(), Direction.class);
				north.setRoom("entranceRoom");
				north.setDescription("You travel deeper into the dungeon. You come to an archway and step through\n");
		Room entrRoom = d1.addRoom("entranceRoom");
			entrRoom.setDescription("You are in an entrance hall. A tattered red carpet divides the room and leads to a grand [staircase]. A large archway marks the entrance to the [south]. There is also a door to the [east]\n");
			Direction stairs = entrRoom.define("staircase", new Direction(), Direction.class);
				stairs.setRoom("stairwell");
				stairs.setDescription("You cross the hall and climb to the top of the stairs\n");
			Direction backToStart = entrRoom.define("south", new Direction(), Direction.class);
				backToStart.setRoom("starterRoom");
				backToStart.setDescription("You cross back through the archway\n");
			Direction GH2DinRum = entrRoom.define("east", new Direction(), Direction.class);
				GH2DinRum.setRoom("dinningHall");
				GH2DinRum.setDescription("You cross to the side of the great hall into the dinning room.\n");
		Room stairRoom = d1.addRoom("stairwell");
			stairRoom.setDescription("Standing at the top of the stairs you can see the entirety of the grand hall. It obviously used to be very vibrant and majestic but has fallen into disrepair. The [staircase] leads up to a locked door.\n");
			Direction stairDown = stairRoom.define("staircase", new Direction(), Direction.class);
				stairDown.setRoom("entranceRoom");
				stairDown.setDescription("You descend the stairs\n");
		Room dinRum = d1.addRoom("dinningHall");
			dinRum.setDescription("You are in the dinning room. I wonder what's for dinner. You can go [west] to the grand hall or [east].\n");
			Direction backToGH = dinRum.define("west", new Direction(), Direction.class);
				backToGH.setRoom("entranceRoom");
				backToGH.setDescription("You leave the dinning room... still hungry.\n");
			Direction din2kitch = dinRum.define("east", new Direction(), Direction.class);
				din2kitch.setRoom("kitchen");
				din2kitch.setDescription("You leave the dinning room... still hungry.\n");
		Room servRum = d1.addRoom("kitchen");
			servRum.setDescription("You are in the kitchen, but it doesn't smell like fresh food. From the looks of it the last fresh food that was prepared here was probably a decades ago. Rusty pots and utensils are sprawled across the floor. Some tables are torn apart. There is an exit [west].\n");
			Direction serv2Din = servRum.define("west", new Direction(), Direction.class);
				serv2Din.setRoom("dinningHall");
				serv2Din.setDescription("You return to the dinning room.\n");
		d1.setStartRoom("starterRoom");
		addDungeon(d1);
		
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
		if (CommandUtil.checkIsStartChars(startChars))
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