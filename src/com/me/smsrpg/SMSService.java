package com.me.smsrpg;

import android.app.Service;
import android.content.Intent;
import android.app.NotificationManager;
import android.os.IBinder;
import android.os.Binder;
import android.util.Log;
import android.widget.Toast;
import android.app.Notification;
import android.app.PendingIntent;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import android.content.IntentFilter;
import java.util.Calendar;
import java.text.DateFormat;
import java.util.Date;

// got all this code from :
// http://developer.android.com/reference/android/app/Service.html

public class SMSService extends Service implements GetsMessages, HasDungeons, HasMessages
{
	private NotificationManager mNM;
	
	Map<String, SMSUser> users;
	List<Dungeon> dungeons;
	private String mainString;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.local_service_started;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        SMSService getService() {
            return SMSService.this;
        }
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		dungeons = new ArrayList<Dungeon>();
		users = new HashMap<String, SMSUser>();
		
		for (int i = 0; i < 15; ++i)
		{
			Dungeon d1 = new Dungeon();
			d1.setName("scaaary dungeon"+(i+1));
			Room startRoom = d1.addRoom("starterRoom");
				startRoom.setDescription("Please note: all scary dungeons are the same. You enter a very scaarry dungeon, the walls are very spooooky! There is a dusty haunted [tome] on the ground and a path [north] going further into the dungeon.\n");
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
		}
		
		SMSListener smsl = new SMSListener(this);
		IntentFilter filter1 = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(smsl, filter1);
		
		mainString = "";
		addComment("serviceStarted");

        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();
    }
	
	@Override
	public List<Dungeon> getDungeons()
	{
		return dungeons;
	}
	@Override
	public Dungeon getDungeon(String nme)
	{
		for (int i = 0; i < dungeons.size(); ++i)//Map.Entry<String, Dungeon> entry : dungeons.entrySet())
		{
			Dungeon dungeon = dungeons.get(i);
			if (dungeon.checkName(nme))
				return dungeon;
		}
		return null;
	}
	
	public void addDungeon(Dungeon d)
	{
		dungeons.add(d);
	}
	
	public void addComment(String s)
	{
		Calendar c = Calendar.getInstance(); 
		Date d = c.getTime();
		mainString += DateFormat.getDateTimeInstance().format(d) + ": " + s + "\n";
		//TextView tv = (TextView) findViewById(R.id.theText);
		//tv.setText(mainString);
	}
	
	public String getMessages()
	{
		return mainString;
	}
	
	@Override
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("SMSService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.local_service_started);

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.ic_launcher, text,
                System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, SMSRPG.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, getText(R.string.local_service_label),
                       text, contentIntent);

        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
    }
}