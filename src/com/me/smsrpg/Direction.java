package com.me.smsrpg;

public class Direction extends Option
{
	private String room;
	private String description;
	
	public Direction()
	{
		super();
	}
	
	public void setRoom(String s)
	{
		room = s;
	}
	
	public void setDescription(String s)
	{
		description = s;
	}
	
	@Override
	public String process(Processor p, DungeonState ds, String cmd)
	{
		ds.setRoom(room);
		return description+p.process(p, ds, "");
	}
}