package com.bioxx.tfc.Commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import com.bioxx.tfc.Chunkdata.ChunkData;
import com.bioxx.tfc.Chunkdata.ChunkDataManager;

public class GetSpawnProtectionCommand extends CommandBase{

	@Override
	public String getCommandName() {
		return "gsp";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] params) 
	{
		if(sender.canCommandSenderUseCommand(0, sender.getCommandSenderName()))
		{
			MinecraftServer var3 = MinecraftServer.getServer();
			EntityPlayerMP var4;

			var4 = getCommandSenderAsPlayer(sender);

			int x = (int)var4.posX >> 4;
			int z = (int)var4.posZ >> 4;

			ChunkData d = ChunkDataManager.getData(x, z);

			if(d != null)
				throw new PlayerNotFoundException("SP: " + d.getSpawnProtectionWithUpdate());
			else
				throw new PlayerNotFoundException("Unable to find ChunkData for "+x+","+z);
		}

	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

}
