package com.me.smsrpg;

import java.util.List;
import java.util.ArrayList;

public class Dungeon extends OptionHolder
{
	private String name;
	private List<Room> rooms;
	private String starterRoom;
	
	public Dungeon()
	{
		rooms = new ArrayList<Room>();
	}
	
	public void setName(String s)
	{
		name = s;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Room addRoom(String nme)
	{
		for (int i = 0; i < rooms.size(); ++i)
		{
			Room r = rooms.get(i);
			if (r.checkName(nme))
				return null;
		}
		Room rm = new Room(nme);
		rooms.add(rm);
		return rm;
	}
	
	public Room getRoom(String nme)
	{
		if (nme == null)
			return null;
		for (int i = 0; i < rooms.size(); ++i)
		{
			Room r = rooms.get(i);
			if (r.checkName(nme))
				return r;
		}
		return null;
	}
	
	public void reset(DungeonState ds)
	{
		ds.setRoom(starterRoom);
	}
	
	@Override
	public String process(Processor p, DungeonState ds, String s)
	{
		Room sRoom = getRoom(ds.getRoom());
		if (sRoom == null)
			return null;
		return sRoom.process(this, ds, s);
	}
	
	public boolean setStartRoom(Room rm)
	{
		return setStartRoom(rm.getName());
	}
	
	public boolean setStartRoom(String nme)
	{
		boolean nameExists = false;
		for (int i = 0; i < rooms.size(); ++i)
		{
			Room r = rooms.get(i);
			if (r.checkName(nme))
			{
				nameExists = true;
				break;
			}
		}
		if (!nameExists)
			return false;
		starterRoom = nme;
		return true;
	}
}