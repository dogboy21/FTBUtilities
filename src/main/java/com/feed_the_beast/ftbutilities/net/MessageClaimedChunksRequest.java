package com.feed_the_beast.ftbutilities.net;

import com.feed_the_beast.ftblib.lib.gui.misc.ChunkSelectorMap;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.math.MathUtils;
import com.feed_the_beast.ftblib.lib.net.MessageToServer;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbutilities.data.ClaimedChunks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class MessageClaimedChunksRequest extends MessageToServer<MessageClaimedChunksRequest>
{
	private int startX, startZ;

	public MessageClaimedChunksRequest()
	{
	}

	public MessageClaimedChunksRequest(int sx, int sz)
	{
		startX = sx;
		startZ = sz;
	}

	public MessageClaimedChunksRequest(Entity entity)
	{
		this(MathUtils.chunk(entity.posX) - ChunkSelectorMap.TILES_GUI2, MathUtils.chunk(entity.posZ) - ChunkSelectorMap.TILES_GUI2);
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBUNetHandler.CLAIMS;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeInt(startX);
		data.writeInt(startZ);
	}

	@Override
	public void readData(DataIn data)
	{
		startX = data.readInt();
		startZ = data.readInt();
	}

	@Override
	public void onMessage(MessageClaimedChunksRequest m, EntityPlayer player)
	{
		if (ClaimedChunks.instance != null)
		{
			new MessageClaimedChunksUpdate(m.startX, m.startZ, player).sendTo(player);
		}
	}
}