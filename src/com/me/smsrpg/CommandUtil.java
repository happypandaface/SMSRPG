package com.me.smsrpg;

import java.util.Arrays;

public class CommandUtil
{
	private String cmd;
	private String[] args;
	
	public CommandUtil(String s)
	{
		cmd = s;
		args = cmd.split(" ");
		if (args[0].equals(""))
			args = Arrays.copyOfRange(args, 1, args.length);
	}
	
	public boolean isCommand(String s)
	{
		if (args == null)
			return false;
		if (!hasParameter(0))
			return false;
		return args[0].toLowerCase().equals("play");
	}
	
	public boolean hasParameter(int num)
	{
		if (args == null)
			return false;
		return args.length > num;
	}
	
	public String getParameter(int num)
	{
		if (args == null)
			return null;
		if (!hasParameter(num-1))
			return null;
		return args[num];
	}
}