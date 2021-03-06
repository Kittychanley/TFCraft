package com.bioxx.tfc.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.api.TFCOptions;

public abstract class BlockTerra extends Block
{
	protected BlockTerra()
	{
		super(Material.rock);
	}

	protected BlockTerra(Material material)
	{
		super(material);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityliving, ItemStack is)
	{
		//TODO: Debug Message should go here if debug is toggled on
		if(TFCOptions.enableDebugMode && world.isRemote)
		{
			int metadata = world.getBlockMetadata(x, y, z);
			System.out.println("Meta="+(new StringBuilder()).append(getUnlocalizedName()).append(":").append(metadata).toString());
		}
	}

	@Override
	public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z)
	{
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int side, float hitX, float hitY, float hitZ)  
	{
		if(TFCOptions.enableDebugMode && world.isRemote)
		{
			int metadata = world.getBlockMetadata(x, y, z);
			System.out.println("Meta = "+(new StringBuilder()).append(getUnlocalizedName()).append(":").append(metadata).toString());
		}
		return false;
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving)
	{
		onBlockPlacedBy(world, x, y, z, entityliving, null);
	}
	
	@Override
	public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta)
	{
		super.harvestBlock(world, player, x, y, z, meta);
		TFC_Core.addPlayerExhaustion(player, 0.001f);
	}
}
