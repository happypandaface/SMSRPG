package com.me.smsrpg;

import java.util.Map;

public interface HasDungeons
{
	Map<String, Dungeon> getDungeons();
	Dungeon getDungeon(String nme);
}