package com.feed_the_beast.ftbutilities.ranks;

import com.feed_the_beast.ftblib.FTBLibCommon;
import com.feed_the_beast.ftblib.lib.config.ConfigNull;
import com.feed_the_beast.ftblib.lib.config.ConfigValue;
import com.feed_the_beast.ftblib.lib.config.RankConfigValueInfo;

/**
 * @author LatvianModder
 */
public class BuiltinOPRank extends BuiltinRank
{
	BuiltinOPRank(Ranks r)
	{
		super(r, "builtin_op");
	}

	@Override
	public Rank getParent()
	{
		return ranks.builtinPlayerRank;
	}

	@Override
	public ConfigValue getConfig(String id)
	{
		RankConfigValueInfo config = FTBLibCommon.RANK_CONFIGS_MIRROR.get(id);
		return config == null ? ConfigNull.INSTANCE : config.defaultOPValue;
	}
}