package com.bioxx.tfc.WorldGen.GenLayers;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

import com.bioxx.tfc.Core.TFC_Core;

public class GenLayerRiverInitTFC extends GenLayerTFC
{
	public GenLayerRiverInitTFC(long par1, GenLayer par3GenLayer)
	{
		super(par1);
		this.parent = (GenLayerTFC) par3GenLayer;
	}

	/**
	 * Creates the random width of the river at the location
	 */
	@Override
	public int[] getInts(int xCoord, int zCoord, int xSize, int zSize)
	{
		int[] parentCache = this.parent.getInts(xCoord, zCoord, xSize, zSize);
		int[] outCache = IntCache.getIntCache(xSize * zSize);

		for (int z = 0; z < zSize; ++z)
		{
			for (int x = 0; x < xSize; ++x)
			{
				this.initChunkSeed(x + xCoord, z + zCoord);
				int index = x + z * xSize;
				int xn = index-1;
				int xp = index+1;
				int zn = index-zSize;
				int zp = index+zSize;
				int id = parentCache[index];
				outCache[index] = !TFC_Core.isOceanicBiome(id) && !TFC_Core.isMountainBiome(id) ? 1 : 0;
			}
		}
		return outCache;
	}
}
