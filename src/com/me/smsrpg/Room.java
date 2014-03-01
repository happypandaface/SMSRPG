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
	private List<Item> items;
	
	public Room(String s)
	{
		name = s;
		options = new ArrayList<String>();
	}
	
	public String process(DungeonState ds, String cmd)
	{
		String rmRtrnStr = "";
		boolean commandWorked = false;
		CommandUtil cu = new CommandUtil(cmd);
		for (int i = 0; i < options.size(); ++i)
		{
			if (cu.isCommand(options.get(i)))
			{
				rmRtrnStr += "You used the "+options.get(i)+"\n";
				
				commandWorked = true;
			}
		}
		if (!commandWorked)
			rmRtrnStr += description;
		return rmRtrnStr;
	}
	
	public void setDescription(String s)
	{
		List<String> rmOpts = new ArrayList<String>();
		// find [] enclosed words, these are options
		Matcher m = Pattern.compile("\\[\\w+\\]").matcher(s);
		while (m.find())
		{// this while step should be combined with the for step that's next
			rmOpts.add(m.group());
		}
		for (int i = 0; i < rmOpts.size(); ++i)
		{
			String itemStr = rmOpts.get(i);
			// remove the brackets that were caught from the regex
			itemStr = itemStr.substring(1, itemStr.length()-1);
			// make sure it's not a system command
			if (!CommandUtil.checkIsSystemCommand(itemStr))
				options.add(itemStr);
		}
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