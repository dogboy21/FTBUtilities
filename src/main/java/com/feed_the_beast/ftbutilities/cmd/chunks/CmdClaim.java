package com.feed_the_beast.ftbutilities.cmd.chunks;

import com.feed_the_beast.ftblib.FTBLibLang;
import com.feed_the_beast.ftblib.lib.cmd.CmdBase;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.math.ChunkDimPos;
import com.feed_the_beast.ftblib.lib.util.text_components.Notification;
import com.feed_the_beast.ftbutilities.FTBUtilities;
import com.feed_the_beast.ftbutilities.FTBUtilitiesConfig;
import com.feed_the_beast.ftbutilities.FTBUtilitiesNotifications;
import com.feed_the_beast.ftbutilities.FTBUtilitiesPermissions;
import com.feed_the_beast.ftbutilities.data.ClaimedChunks;
import com.feed_the_beast.ftbutilities.data.FTBUTeamData;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.TextComponentHelper;
import net.minecraftforge.server.permission.PermissionAPI;

/**
 * @author LatvianModder
 */
public class CmdClaim extends CmdBase
{
	public CmdClaim()
	{
		super("claim", Level.ALL);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (ClaimedChunks.instance == null)
		{
			throw FTBLibLang.FEATURE_DISABLED.commandError();
		}

		EntityPlayerMP player = getCommandSenderAsPlayer(sender);
		ForgePlayer p;

		if (args.length >= 1)
		{
			if (!PermissionAPI.hasPermission(player, FTBUtilitiesPermissions.CLAIMS_CHUNKS_MODIFY_OTHERS))
			{
				throw FTBLibLang.COMMAND_PERMISSION.commandError();
			}

			p = getForgePlayer(sender, args[0]);
		}
		else
		{
			p = getForgePlayer(player);
		}

		if (!p.hasTeam())
		{
			throw FTBLibLang.TEAM_NO_TEAM.commandError();
		}

		ChunkDimPos pos = new ChunkDimPos(player);

		if (!FTBUtilitiesConfig.world.allowDimension(pos.dim))
		{
			Notification.of(FTBUtilitiesNotifications.CHUNK_CANT_CLAIM, TextComponentHelper.createComponentTranslation(player, FTBUtilities.MOD_ID + ".lang.chunks.claiming_not_enabled_dim")).setError().send(server, player);
			return;
		}

		switch (ClaimedChunks.instance.claimChunk(FTBUTeamData.get(p.team), pos))
		{
			case SUCCESS:
				Notification.of(FTBUtilitiesNotifications.CHUNK_MODIFIED, TextComponentHelper.createComponentTranslation(player, FTBUtilities.MOD_ID + ".lang.chunks.chunk_claimed")).send(server, player);
				CmdChunks.updateChunk(player, pos);
				break;
			case DIMENSION_BLOCKED:
				break;
			case NO_POWER:
				break;
			case ALREADY_CLAIMED:
				FTBUtilitiesNotifications.sendCantModifyChunk(server, player);
				break;
		}
	}
}