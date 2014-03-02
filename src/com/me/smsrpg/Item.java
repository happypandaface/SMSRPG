package com.me.smsrpg;

public class Item extends Option
{
	private String description;
	
	public Item()
	{
		super();
	}
	
	public void setDescription(String s)
	{
		description = s;
	}
	
	@Override
	public String process(Processor p, DungeonState ds, String cmd)
	{
		return description;
	}
}