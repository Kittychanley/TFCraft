package com.bioxx.tfc.Blocks.Terrain;

import com.bioxx.tfc.TFCBlocks;
import com.bioxx.tfc.api.Constant.Global;

import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;

public class BlockMM extends BlockStone
{
	public static boolean fallInstantly = false;

	public BlockMM(Material material)
	{
		super(material);
		this.dropBlock = TFCBlocks.StoneMMCobble;
		names = Global.STONE_MM;
		icons = new IIcon[names.length];
		looseStart = Global.STONE_MM_START;
		gemChance = 3;
	}
}
