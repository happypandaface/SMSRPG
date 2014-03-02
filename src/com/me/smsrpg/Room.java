package com.me.smsrpg;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;

public class Room extends OptionHolder
{
	private String description;
	private List<String> descOpts;// list of bracketed things in the description
	private List<Option> options;
	
	public Room(String s)
	{
		name = s;
		descOpts = new ArrayList<String>();
		options = new ArrayList<Option>();
	}
	
	@Override
	public String process(Processor p, DungeonState ds, String cmd)
	{
		String rmRtrnStr = "";
		boolean commandWorked = false;
		CommandUtil cu = new CommandUtil(cmd);
		for (int i = 0; i < options.size(); ++i)
		{
			Option item = options.get(i);
			if (cu.isCommand(item))
			{
				rmRtrnStr += item.process(p, ds, cmd);
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
				descOpts.add(itemStr);
		}
		description = s;
	}
	
	public <T extends Option> T define(String option, Option opt, Class<T> type)
	{
		boolean foundOption = false;
		for (int i = 0; i < descOpts.size(); ++i)
		{
			if (descOpts.get(i).equals(option))
				foundOption = true;
		}
		// it should be []'d in the description of the room (unless it's hidden which is not implemented yet)
		if (!foundOption)
			return null;
		opt.setName(option);
		options.add(opt);
		return type.cast(opt);
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