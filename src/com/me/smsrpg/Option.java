package com.me.smsrpg;

public class Option implements Processor
{
	protected String name;
	
	public Option()
	{
	}
	
	public Option(String nme)
	{
		this.name = nme;
	}
	
	public String process(Processor p, DungeonState ds, String cmd)
	{
		return null;
	}
	
	public void setName(String nme)
	{
		this.name = nme;
	}
	
	public String getName()
	{
		return this.name;
	}
}