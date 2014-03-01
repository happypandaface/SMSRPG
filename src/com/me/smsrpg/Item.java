package com.me.smsrpg;

public class Item
{
	private String name;
	private String description;
	
	public Item(String s)
	{
		
	}
	
	public void setDescription(String s)
	{
		description = s;
	}
	
	public String useItem(DungeonState ds, String cmd)
	{
		return description;
	}
}