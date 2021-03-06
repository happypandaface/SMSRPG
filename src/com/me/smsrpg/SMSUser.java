package com.me.smsrpg;

import android.telephony.gsm.SmsManager;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class SMSUser implements Processor
{
	private String address;
	private boolean notFirstMessage;
	private HasDungeons dungeonKeeper;
	private List<String> lastDungeons;
	private Dungeon currentDungeon;
	private DungeonState userState;
	
	public SMSUser(String addr, HasDungeons hd)
	{
		address = addr;
		dungeonKeeper = hd;
	}
	
	public boolean checkName(String addr)
	{
		return address.equals(addr);
	}
	
	public String getAddr()
	{
		return address;
	}
	
	public String process(String cmd)
	{
		return process(this, userState, cmd);
	}
	
	public String process(Processor p, DungeonState ds, String s)
	{
		String strToSndBck = "";
		if (currentDungeon == null)
		{
			CommandUtil cu = new CommandUtil(s);
			if (cu.isCommand("play") && cu.hasParameter(1))
			{
				if (lastDungeons == null)
				{
					List<Dungeon> dungs = dungeonKeeper.getDungeons();
					lastDungeons = new ArrayList<String>();
					for (int dungNum = 0; dungNum < dungs.size(); ++dungNum)
					{
						Dungeon dungeon = dungs.get(dungNum);
						String dungeonName = dungeon.getName();
						lastDungeons.add(dungeonName);
					}
				}
				int dungNum = Integer.parseInt(cu.getParameter(1));
				if (dungNum-1 < lastDungeons.size())
				{
					strToSndBck += "You are now playing "+lastDungeons.get(dungNum-1)+" type \"leave\" to leave the dungeon\n";
					strToSndBck += "Remember that it's #?leave because all commands start with #? Okay now I'm not going to give you this hint again!\n";
					currentDungeon = dungeonKeeper.getDungeon(lastDungeons.get(dungNum-1));
					// disregard the parameter "ds"
					userState = new DungeonState();
					currentDungeon.reset(userState);
					String dungeonIntro = currentDungeon.process(p, userState, "");
					if (dungeonIntro == null)
					{
						currentDungeon = null;
						strToSndBck += "This dungeon is broken! Sorry, choose another";
					}else
						strToSndBck += dungeonIntro;
				}else
				{
					strToSndBck += "That dungeon wasn't on the list!\n";
					strToSndBck += "Send me a blank command (which would be a text with just #?)\n";
				}
			}else
			{
				if (!notFirstMessage)
					strToSndBck += "Welcome to SMSRPG\n";
				else
					strToSndBck += "Welcome back to SMSRPG!\n";
				strToSndBck += "Here are the playable dungeons:\n";
				
				int page = 0;
				if (cu.isCommand("page") && cu.hasParameter(1))
				{
					page = Integer.parseInt(cu.getParameter(1))-1;
				}
				
				strToSndBck += "Dungeons page "+(page+1)+"\n";
				List<Dungeon> dungs = dungeonKeeper.getDungeons();
				lastDungeons = new ArrayList<String>();
				for (int dungNum = 0; dungNum < dungs.size(); ++dungNum)
				{
					Dungeon dungeon = dungs.get(dungNum);
					String dungeonName = dungeon.getName();
					lastDungeons.add(dungeonName);
					if (dungNum >= page*10 && dungNum < page*10+10)
						strToSndBck += (dungNum+1)+". "+dungeonName+"\n";
				}
				strToSndBck += "Type \"play \" and then the number of the dungeon you want to play\n";
				strToSndBck += "Example: #?play 1\n";
				strToSndBck += "Or type \"page\" and then the number of the page you want to goto\n";
				strToSndBck += "also remember all commands start with a \"#?\"\n";
			}
		}else
		{
			String dungeonStr = currentDungeon.process(p, userState, s);
			if (dungeonStr == null)
			{
				strToSndBck += "Thank you for playing "+currentDungeon.getName()+"\n";
				currentDungeon = null;
			}else
			{
				strToSndBck += dungeonStr;
			}
		}
		
		sendMessage(strToSndBck);
		notFirstMessage = true;
		return null;
	}
	
	private void sendMessage(String s)
	{
		ArrayList<String> strs = SmsManager.getDefault().divideMessage(s);
		
		for (String str : strs)
			SmsManager.getDefault().sendTextMessage(address, null, str, null, null);
	}
}