package com.me.smsrpg;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;

public class Room
{
	private String name;
	private String description;
	private List<String> options;
	
	public Room(String s)
	{
		name = s;
	}
	
	public String process(DungeonState ds, String cmd)
	{
		String rmRtrnStr = "";
		boolean commandWorked = false;
		for (int i = 0; i < options.size(); ++i)
		{
			
		}
		if (!commandWorked)
			rmRtrnStr += description;
		return rmRtrnStr;
	}
	
	public void setDescription(String s)
	{
		List<String> rmOpts = new ArrayList<String>();
		Matcher m = Pattern.compile("\\{\\w+\\}").matcher(s);
		while (m.find())
		{
			rmOpts.add(m.group());
		}
		options = rmOpts;
		description = s;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public boolean checkName(String s)
	{
		return name.equals(s);
	}
	
	public String getName()
	{
		return name;
	}
}