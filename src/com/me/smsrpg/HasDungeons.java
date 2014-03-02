package com.me.smsrpg;

import java.util.List;

public interface HasDungeons
{
	List<Dungeon> getDungeons();
	Dungeon getDungeon(String nme);
}