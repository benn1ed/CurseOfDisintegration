package benn1ed.curseofdisintegration.capability;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import benn1ed.curseofdisintegration.DisintegrationUnit;
import benn1ed.curseofdisintegration.Frames;
import benn1ed.curseofdisintegration.ModData;
import benn1ed.curseofdisintegration.config.Config;
import benn1ed.curseofdisintegration.network.NetManager;
import benn1ed.curseofdisintegration.network.NetPacketDIValue;
import benn1ed.curseofdisintegration.util.EntityHelper;
import benn1ed.curseofdisintegration.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;
import net.minecraftforge.fml.network.PacketDistributor;

public class Disintegration implements IDisintegration
{
	public static final String KEY = ModData.MODID.concat(".disintegrationValue");

	public final Frames frames = new Frames(Config.CAP.diFrames.get());
	private final DisintegrationUnit _value = new DisintegrationUnit((short)0, Config.CAP.max.get().shortValue());
	private int _mitCd = 0;
	
	@Override
	public short getValue()
	{
		return _value.get();
	}
	
	@Override
	public void setValue(short value)
	{
		_value.set(value);
	}
	
	@Override
	public void incrementValue(short value)
	{
		_value.increment(value);
	}
	
	@Override
	public short getMaxDisintegration()
	{
		return _value.getMax();
	}
	
	@Override
	public void tick(PlayerEntity player)
	{
		if (player.isDeadOrDying() || player.isCreative() || player.isSpectator())
		{
			return;
		}
		
		frames.tick();
		decrementMitigationCooldown();
		
		if (frames.done())
		{
			for(Entity ent : player.level.<LivingEntity>getEntitiesOfClass(LivingEntity.class, aabb(player)))
			{
				if (provoke(checkEntity(ent, player), player))
				{
					frames.reset();
					break;
				}
			}
			if (mitigationCooldownIsUp() && getValue() > 0)
			{
				incrementAndSync((short)-Config.CAP.mitRate.get().shortValue(), player);
				frames.reset();
			}
		}
		if (reachedLimit())
		{
			IDisintegration.disintegrate(player);
		}
	}
	
	@Override
	public void onHurt(PlayerEntity player, LivingDamageEvent event)
	{
		Entity sourceEnt = event.getSource().getEntity();
		if (!Config.CAP.processEveryHit.get() && sourceEnt == null)
		{
			return;
		}
		short increment = 0;
		if (sourceEnt instanceof PlayerEntity)
		{
			if (Config.CAP_P.processPlayers.get())
			{
				increment = Config.CAP_P.hurt.get().shortValue();
			}
		}
		else
		{
			boolean living = sourceEnt instanceof LivingEntity;
			if (!Config.CAP.processEveryHit.get() && !living)
			{
				return;
			}
			if (living && !livingSatisfiesLists((LivingEntity)sourceEnt))
			{
				return;
			}
			increment = (short)(living ? Config.CAP.creatureHurt.get().shortValue() : Config.CAP.hurt.get().shortValue());
		}
		provoke(increment, player);
	}
	
	@Override
	public void onHit(PlayerEntity player, LivingDamageEvent event)
	{
		short increment = 0;
		if (event.getEntity() instanceof PlayerEntity)
		{
			if (Config.CAP_P.processPlayers.get())
			{
				increment = Config.CAP_P.hit.get().shortValue();
			}
		}
		else
		{
			increment = (short)Config.CAP.hit.get().shortValue();
		}
		provoke(increment, player);
	}
	
	@Override
	public void setAndSync(short value, PlayerEntity player)
	{
		setValue(value);
		sync(player);
	}
	
	@Override
	public void incrementAndSync(short increment, PlayerEntity player)
	{
		setAndSync((short)(getValue() + increment), player);
	}
	
	@Override
	public void sync(PlayerEntity player)
	{
		if (Thread.currentThread().getThreadGroup() != SidedThreadGroups.SERVER || !(player instanceof ServerPlayerEntity))
		{
			return;
		}
		NetManager.NETWORK_CHANNEL.send(
				PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), new NetPacketDIValue(getValue()));
	}
	
	private short checkEntity(Entity ent, PlayerEntity player)
	{
		if (ent == null || player == null || ent == player)
		{
			return 0;
		}
		
		if (ent instanceof PlayerEntity)
		{
			if (!Config.CAP_P.processPlayers.get())
			{
				return 0;
			}
			PlayerEntity entPlayer = (PlayerEntity)ent;
			if (Config.CAP_P.passiveDist.get() > 0
					&& (!Config.CAP_P.passiveRecentlyHit.get() || EntityHelper.getEntityIsAfterAnother(entPlayer, player))
					&& EntityHelper.getEntityIsWithinRadiusOfAnother(entPlayer, player, Config.CAP_P.passiveDist.get()))
			{
				return Config.CAP_P.passiveRate.get().shortValue();
			}
		}
		else if (ent instanceof LivingEntity)
		{
			LivingEntity entLiving = (LivingEntity)ent;
			if (Config.CAP.passiveDist.get() > 0
					&& EntityHelper.getEntityIsAfterAnother(entLiving, player)
					&& EntityHelper.getEntityIsWithinRadiusOfAnother(entLiving, player, Config.CAP.passiveDist.get())
					&& livingSatisfiesLists(entLiving))
			{
				return Config.CAP.passiveRate.get().shortValue();
			}
		}
		
		return 0;
	}
	
	@Override
	public void resetMitigationCooldown()
	{
		_mitCd = Config.CAP.mitCd.get();
	}
	
	@Override
	public void decrementMitigationCooldown()
	{
		_mitCd--;
	}
	
	@Override
	public boolean mitigationCooldownIsUp()
	{
		return _mitCd <= 0;
	}
	
	private boolean provoke(short increment, PlayerEntity player)
	{
		if (increment > 0 && player != null)
		{
			incrementAndSync(increment, player);
			resetMitigationCooldown();
			return true;
		}
		return false;
	}
	
	private boolean livingSatisfiesLists(LivingEntity living)
	{
		return livingSatisfiesList(living, true) && livingSatisfiesList(living, false);
	}
	
	private boolean livingSatisfiesList(LivingEntity living, boolean whitelist)
	{
		ResourceLocation resource = living.getType().getRegistryName();
		if (resource == null)
		{
			return false;
		}
		List<? extends String> list = whitelist ? Config.CAP.creatureWhitelist.get() : Config.CAP.creatureBlacklist.get();
		if (list == null || list.size() <= 0)
		{
			return true;
		}
		String registryName = resource.toString();
		boolean atLeastOne = Utils.<String>atLeastOne(list, s ->
		{
			boolean itemMatches = false;
			try
			{
				itemMatches = Pattern.matches(s, registryName);
			}
			catch(PatternSyntaxException e) {};
			return itemMatches;
		});
		return whitelist ? atLeastOne : !atLeastOne;
	}
	
	private AxisAlignedBB aabb(PlayerEntity player)
	{
		Vector3d playerPos = player.position();
		double dist = Math.max(Config.CAP.passiveDist.get(), Config.CAP_P.passiveDist.get());
		Vector3d plus = new Vector3d(playerPos.x + dist, playerPos.y + dist, playerPos.z + dist);
		Vector3d minus = new Vector3d(playerPos.x - dist, playerPos.y - dist, playerPos.z - dist);
		AxisAlignedBB aabb = new AxisAlignedBB(plus, minus);
		return aabb;
	}
}