package com.me.smsrpg;

public class DungeonState
{
	private String currentRoom;
	
	public DungeonState()
	{
		
	}
	
	public String getRoom()
	{
		return currentRoom;
	}
	
	public void setRoom(String s)
	{
		currentRoom = s;
	}
}