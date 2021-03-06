package com.bioxx.tfc.WorldGen.Data;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class ChunkTFC extends Chunk
{
	private ExtendedBlockStorage[] EBS;

	public ChunkTFC(World world, Block[] blocks, byte[] metadata, int chunkX, int chunkZ)
	{
		super(world, chunkX, chunkZ);
		int height = blocks.length / 256;
		EBS = getBlockStorageArray();

		for (int x = 0; x < 16; ++x)
		{
			for (int z = 0; z < 16; ++z)
			{
				for (int y = 0; y < height; ++y)
				{
					int index = (x << 12 | z << 8 | y);
					Block b = blocks[index];

					if (b != null && b != Blocks.air)
					{
						int chunkY = (y >> 4);
						if (EBS[chunkY] == null)
						{
							EBS[chunkY] = new ExtendedBlockStorage(chunkY << 4, !world.provider.hasNoSky);
						}
						EBS[chunkY].func_150818_a(x, y & 15, z, b);//setExtBlock
						EBS[chunkY].setExtBlockMetadata(x, y & 15, z, metadata[index]);
					}
				}
			}
		}
		setStorageArrays(EBS);
	}
}
