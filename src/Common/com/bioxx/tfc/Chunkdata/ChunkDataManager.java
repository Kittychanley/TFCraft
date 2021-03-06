package com.bioxx.tfc.Chunkdata;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.bioxx.tfc.Core.TFC_Time;
import com.bioxx.tfc.api.TFCOptions;

public class ChunkDataManager 
{
	private static Map chunkmap = Collections.synchronizedMap(new HashMap());

	public static void removeData(int x, int z)
	{
		synchronized(chunkmap)
		{
			if(chunkmap.containsKey(x + "," + z))
				chunkmap.remove(x + "," + z);
			/*else
				System.out.println("Tried to unload chunkdata from the chunkmap that didnt exist at " + x + "," + z);*/
		}
		/*if(TFCOptions.enableDebugMode)
			System.out.println("ChunkDataManager chunkmap size: "+chunkmap.size());*/
	}

	public static void addData(int x, int z, ChunkData cd)
	{
		synchronized(chunkmap)
		{
			chunkmap.put(x + "," + z, cd);
		}
		/*if(TFCOptions.enableDebugMode)
			System.out.println("ChunkDataManager chunkmap size: "+chunkmap.size());*/
	}

	public static ChunkData getData(int x, int z)
	{
		synchronized(chunkmap)
		{
			if(chunkmap.containsKey(x + "," + z))
				return (ChunkData) chunkmap.get(x + "," + z);
			else return null;
		}
	}

	public static boolean addProtection(int x, int z, int amount)
	{
		synchronized(chunkmap)
		{
			ChunkData d = (ChunkData) chunkmap.get(x + "," + z);
			if(d != null)
			{
				if(d.spawnProtection < 24*TFC_Time.daysInMonth*TFCOptions.maxProtectionMonths)
					d.setSpawnProtectionWithUpdate(amount);
				return true;
			}
		}
		return false;
	}

	public static boolean setLastVisted(int x, int z)
	{
		synchronized(chunkmap)
		{
			ChunkData d = (ChunkData) chunkmap.get(x + "," + z);
			if(d != null)
			{
				d.lastVisited = TFC_Time.getTotalTicks();
				return true;
			}
		}
		return false;
	}
}
