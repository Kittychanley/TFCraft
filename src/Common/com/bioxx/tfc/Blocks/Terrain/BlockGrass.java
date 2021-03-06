package com.bioxx.tfc.Blocks.Terrain;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.bioxx.tfc.Reference;
import com.bioxx.tfc.TFCBlocks;
import com.bioxx.tfc.TerraFirmaCraft;
import com.bioxx.tfc.Core.ColorizerGrassTFC;
import com.bioxx.tfc.Core.TFCTabs;
import com.bioxx.tfc.Core.TFC_Climate;
import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.Core.TFC_Textures;
import com.bioxx.tfc.WorldGen.DataLayer;
import com.bioxx.tfc.WorldGen.Generators.WorldGenGrowTrees;
import com.bioxx.tfc.api.TFCOptions;
import com.bioxx.tfc.api.Constant.Global;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGrass extends net.minecraft.block.BlockGrass
{
	protected int textureOffset = 0;

	@SideOnly(Side.CLIENT)
	public IIcon GrassTopTexture;
	@SideOnly(Side.CLIENT)
	public IIcon iconSnowSide;
	@SideOnly(Side.CLIENT)
	public IIcon iconGrassSideOverlay;

	public BlockGrass()
	{
		super();
		this.setTickRandomly(true);
		this.setCreativeTab(TFCTabs.TFCBuilding);
	}

	public BlockGrass(int texOff)
	{
		this();
		textureOffset = texOff;
		this.setCreativeTab(TFCTabs.TFCBuilding);
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tabs, List list)
	{
		// Change to false if this block should not be added to the creative tab
		Boolean addToCreative = true;

		if(addToCreative)
		{
			int count;
			if(textureOffset == 0) count = 16;
			else count = Global.STONE_ALL.length - 16;

			for(int i = 0; i < count; i++)
				list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public int damageDropped(int dmg)
	{
		return dmg;
	}

	@Override
	public void registerBlockIcons(IIconRegister registerer)
	{
		GrassTopTexture = registerer.registerIcon(Reference.ModID + ":" + "GrassTop");

		iconSnowSide = registerer.registerIcon(Reference.ModID + ":" + "snow");
		iconGrassSideOverlay = registerer.registerIcon(Reference.ModID + ":" + "GrassSide");

		TFC_Textures.InvisibleTexture = registerer.registerIcon(Reference.ModID + ":" + "Invisible");
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
	 */
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return side == 1 ? GrassTopTexture : (side == 0 ? TFC_Textures.InvisibleTexture : iconGrassSideOverlay);
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		if(world.isAirBlock(x, y - 1, z))
		{
			int meta = world.getBlockMetadata(x, y, z);
			int yNew = y - 1;
			while(!world.getBlock(x, yNew--, z).isOpaqueCube()){}
			world.setBlock(x, yNew, z, this, meta, 0x2);
			world.setBlockToAir(x, y, z);
		}
	}

	/**
	 * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
	 */
	@Override
	public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side)
	{
		if (side == 1)
			return GrassTopTexture;
		else if (side == 0)
			return TFC_Textures.InvisibleTexture;
		else if (side == 2) //-Z
		{
			if(TFCOptions.enableBetterGrass == true && access.getBlock(x, y - 1, z - 1).getMaterial() == Material.grass)
				return isSnow(access, x, y - 1, z - 1) ? Blocks.snow.getBlockTextureFromSide(0) : GrassTopTexture;
		}
		else if (side == 3) //+Z
		{
			if(TFCOptions.enableBetterGrass == true && access.getBlock(x, y - 1, z + 1).getMaterial() == Material.grass)
				return isSnow(access, x, y - 1, z + 1) ? Blocks.snow.getBlockTextureFromSide(0) : GrassTopTexture;
		}
		else if (side == 4) //-X
		{
			if(TFCOptions.enableBetterGrass == true && access.getBlock(x - 1, y - 1, z).getMaterial() == Material.grass)
				return isSnow(access, x - 1, y - 1, z) ? Blocks.snow.getBlockTextureFromSide(0) : GrassTopTexture;
		}
		else if (side == 5) //+X
		{
			if(TFCOptions.enableBetterGrass == true && access.getBlock(x + 1, y - 1, z).getMaterial() == Material.grass)
				return isSnow(access, x + 1, y - 1, z) ? Blocks.snow.getBlockTextureFromSide(0) : GrassTopTexture;
		}

		return iconGrassSideOverlay;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side)
	{
		if(side == 0)
			return false;
		else
			return super.shouldSideBeRendered(access, x, y, z, side);
	}

	private boolean isSnow(IBlockAccess access, int x, int y, int z)
	{
		Material material = access.getBlock(x, y, z).getMaterial();
		return material == Material.snow || material == Material.craftedSnow;
	}


	/**
	 * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
	 * when first determining what to render.
	 */
	@Override
	public int colorMultiplier(IBlockAccess bAccess, int x, int y, int z)
	{
		return TerraFirmaCraft.proxy.grassColorMultiplier(bAccess, x, y, z);
	}

	@Override
	public int getRenderType()
	{
		return TFCBlocks.grassRenderId;
	}

	@Override
	public int getBlockColor()
	{
		double d0 = 0.5D;
		double d1 = 1.0D;
		return ColorizerGrassTFC.getGrassColor(d0, d1);
	}

	/**
	 * Returns the color this block should be rendered. Used by leaves.
	 */
	@Override
	public int getRenderColor(int par1)
	{
		return this.getBlockColor();
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick(World world, int i, int j, int k, Random rand)
	{
		if (!world.isRemote)
		{
			if(world.getBlock(i, j + 1, k) == Blocks.snow)
			{
				world.setBlock(i, j, k, TFC_Core.getTypeForDryGrassFromSoil(world.getBlock(i, j, k)), world.getBlockMetadata(i, j, k), 0x2);
			}
			else if (world.getBlockLightValue(i, j + 1, k) < 4 && world.getBlock(i, j + 1, k).getLightOpacity() > 2)
			{
				world.setBlock(i, j, k, TFC_Core.getTypeForDirt(world.getBlockMetadata(i, j, k) + textureOffset), world.getBlockMetadata(i, j, k), 0x2);
			}
			else if (world.getBlockLightValue(i, j + 1, k) >= 4)
			{
				for (int var6 = 0; var6 < 4; ++var6)
				{
					int x = i + rand.nextInt(3) - 1;
					int y = j + rand.nextInt(5) - 3;
					int z = k + rand.nextInt(3) - 1;

					float rain = TFC_Climate.getRainfall(world, x, y + 1, z);

					Block id = world.getBlock(x, y, z);
					int meta = world.getBlockMetadata(x, y, z);

					if (TFC_Core.isDirt(id) && rand.nextInt(10) == 0 && world.getBlock(x, y + 1, z).getMaterial() != Material.water)
						world.setBlock(x, y, z, TFC_Core.getTypeForGrassWithRainByBlock(id, rain), meta, 0x2);
					else if (TFC_Core.isClay(id) && rand.nextInt(10) == 0 && world.getBlock(x, y + 1, z).getMaterial() != Material.water)
						world.setBlock(x, y, z, TFC_Core.getTypeForClayGrass(meta), meta, 0x2);
					else if (TFC_Core.isPeat(id) && rand.nextInt(10) == 0 && world.getBlock(x, y + 1, z).getMaterial() != Material.water)
						world.setBlock(x, y, z, TFCBlocks.PeatGrass);
				}

				float rain = TFC_Climate.getRainfall(world, i, j + 1, k);
				float temp = TFC_Climate.getHeightAdjustedTemp(world, i, j+1, k);
				Block id = world.getBlock(i, j, k);

				if (TFC_Core.isGrass(id) && !TFC_Core.isDryGrass(id) && world.getBlock(i, j + 1, k).getMaterial() != Material.water && world.isAirBlock(i, j + 1, k))
				{
					if(rand.nextInt((int) ((16800-rain)/4)) == 0 && temp > 20)
						world.setBlock(i, j + 1, k, TFCBlocks.TallGrass, (world.rand.nextInt(30) == 0 ? 1 : 0), 0x2); // 1/30 chance to spawn fern
					else if (rand.nextInt(15000) == 0 && temp > 20 && world.canBlockSeeTheSky(i, j + 1, k))
						new WorldGenGrowTrees().generate(world, rand, i, j, k);
				}

				boolean nearWater = false;

				for(int y = 0; y < 2 && !nearWater; y++)
				{
					for(int x = -4; x < 5 && !nearWater; x++)
					{
						for(int z = -4; z < 5 && !nearWater; z++)
						{
							if(world.getBlock(i+x, j-y, k+z).getMaterial() == Material.water)
								nearWater = true;
						}
					}
				}

				DataLayer rock1 = TFC_Climate.getRockLayer(world, i, j, k, 0);
				if(TFC_Core.isGrass(id) && !TFC_Core.isDryGrass(id) && !nearWater && rain < 500)
				{
					int meta = TFC_Core.getSoilMetaFromStone(rock1.block, rock1.data2);
					world.setBlock(i, j, k, TFC_Core.getTypeForDryGrass(rock1.data1), meta, 2);
				}
				else if(TFC_Core.isGrass(id) && TFC_Core.isDryGrass(id) && (nearWater || rain>=500) && world.getBlock(i, j+1, k) != Blocks.snow)
				{
					int meta = TFC_Core.getSoilMetaFromStone(rock1.block, rock1.data2);
					world.setBlock(i, j, k, TFC_Core.getTypeForGrass(rock1.data1), meta, 2);
				}
			}

			world.markBlockForUpdate(i, j, k);
		}
	}

	@Override
	public void onEntityWalking(World world, int x, int y, int z, Entity entity)
	{
		if (!world.isRemote && this != TFCBlocks.ClayGrass2 && this != TFCBlocks.ClayGrass && this != TFCBlocks.PeatGrass)
		{
			Random R = new Random();
			if(!BlockCollapsable.isNearSupport(world, x, y, z, 4, 0) && BlockDirt.canFallBelow(world, x, y - 1, z) && R.nextInt(10) == 0)
			{
				int meta = world.getBlockMetadata(x, y, z);
				world.setBlock(x, y, z, TFC_Core.getTypeForDirtFromGrass(this), meta, 0x2);
			}
		}
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	@Override
	public Item getItemDropped(int metadata, Random rand, int fortune)
	{
		return TFC_Core.getTypeForDirtFromGrass(this).getItemDropped(metadata, rand, fortune);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block b)
	{
		if(!world.blockExists(x, y - 1, z))
		{
			int meta = world.getBlockMetadata(x, y, z);
			world.setBlock(x, y, z, TFC_Core.getTypeForDirtFromGrass(this), meta, 0x2);
		}
	}
}
