package benn1ed.curseofdisintegration.capability;

import java.util.regex.*;
import benn1ed.curseofdisintegration.*;
import benn1ed.curseofdisintegration.config.*;
import benn1ed.curseofdisintegration.network.*;
import benn1ed.curseofdisintegration.potion.*;
import benn1ed.curseofdisintegration.util.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.relauncher.*;

public class Disintegration implements IDisintegration
{
	public static final String KEY = ModData.MODID.concat(".disintegrationValue");
	public static final Pattern REGISTRY_NAME_PATTERN = Pattern.compile(".*:.*");

	public final Frames frames = new Frames(ModConfig.capability.disintegrationFrames);
	private final DisintegrationUnit _value = new DisintegrationUnit((short)0, (short)ModConfig.capability.maxDisintegration);
	private float _multiplier = 1f;
	private boolean _disintegrateOnMax = true;
	private int disMitCd = 0;
	
	public Disintegration()
	{
		
	}
	
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
	public float getMultiplier()
	{
		return _multiplier;
	}

	@Override
	public void setMultiplier(float value)
	{
		_multiplier = value;
	}
	
	@Override
	public boolean getDisintegrateOnMax()
	{
		return _disintegrateOnMax;
	}
	
	@Override
	public void setDisintegrateOnMax(boolean value)
	{
		_disintegrateOnMax = value;
	}
	
	@Override
	public void incrementValue(short value)
	{
		float mp = Math.abs(getMultiplier());
		if (value < 0 && mp < 1)
		{
			mp = 2 - mp;
		}
		_value.increment((short)(value * mp));
	}
	
	@Override
	public short getMaxDisintegration()
	{
		return _value.getMax();
	}
	
	@Override
	public void tick(EntityPlayer player)
	{
		if (player.world.isRemote || player.isDead || player.isCreative() || player.isSpectator())
		{
			return;
		}
		
		frames.tick();
		decrementMitigationCooldown();
		
		if (frames.done())
		{
			if (CurseOfDisintegration.integrationManager.diFramesDone(player, this))
			{
				frames.reset();
			}
			for(Entity ent : player.world.loadedEntityList)
			{
				if (provoke(checkEntity(ent, player), player))
				{
					frames.reset();
					break;
				}
			}
			if (mitigationCooldownIsUp())
			{
				incrementAndSync((short)-ModConfig.capability.disintegrationMitigationRate, player);
				frames.reset();
			}
		}
		if (reachedLimit() && getDisintegrateOnMax())
		{
			IDisintegration.disintegrate(player);
		}
		
		if (player.getActivePotionEffect(PotionManager.POTION) == null)
		{
			setMultiplier(1f);
		}
		if (player.getActivePotionEffect(PotionManager.CORPSE_POTION) == null)
		{
			setDisintegrateOnMax(true);
		}
	}
	
	@Override
	public void onHurt(EntityPlayer player, LivingDamageEvent event)
	{
		Entity sourceEnt = event.getSource().getTrueSource();
		short increment = 0;
		if (sourceEnt instanceof EntityPlayer)
		{
			if (ModConfig.capabilityPlayers.processPlayers)
			{
				increment = (short)ModConfig.capabilityPlayers.onHurtValue;
			}
		}
		else if (sourceEnt instanceof EntityLivingBase)
		{
			if (livingSatisfiesLists((EntityLivingBase)sourceEnt))
			{
				increment = (short)ModConfig.capability.onCreatureHurtValue;
			}
		}
		else
		{
			increment = (short)ModConfig.capability.onHurtValue;
		}
		provoke(increment, player);
	}
	
	@Override
	public void onHit(EntityPlayer player, LivingDamageEvent event)
	{
		short increment = 0;
		if (event.getEntity() instanceof EntityPlayer)
		{
			if (ModConfig.capabilityPlayers.processPlayers)
			{
				increment = (short)ModConfig.capabilityPlayers.onHitValue;
			}
		}
		else
		{
			increment = (short)ModConfig.capability.onHitValue;
		}
		provoke(increment, player);
	}
	
	@Override
	public void setAndSync(short value, EntityPlayer player)
	{
		if (value != getValue())
		{
			setValue(value);
			sync(player);
		}
	}
	
	@Override
	public void incrementAndSync(short increment, EntityPlayer player)
	{
		if (increment != 0)
		{
			incrementValue(increment);
			sync(player);
		}
	}
	
	@Override
	public void sync(EntityPlayer player)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() != Side.SERVER || !(player instanceof EntityPlayerMP))
		{
			return;
		}
		NetManager.NETWORK_WRAPPER_INSTANCE.sendTo(new NetPacketDIValue(getValue()), (EntityPlayerMP)player);
	}
	
	private short checkEntity(Entity ent, EntityPlayer player)
	{
		if (ent == null || player == null || ent == player)
		{
			return 0;
		}
		
		if (ent instanceof EntityPlayer)
		{
			if (!ModConfig.capabilityPlayers.processPlayers)
			{
				return 0;
			}
			EntityPlayer entPlayer = (EntityPlayer)ent;
			if (ModConfig.capabilityPlayers.passiveDisintegrationDistance > 0
					&& (!ModConfig.capabilityPlayers.passiveRecentlyHit || EntityHelper.getPlayerIsAfterAnother(entPlayer, player))
					&& EntityHelper.getEntityIsWithinRadiusOfAnother(entPlayer, player, ModConfig.capabilityPlayers.passiveDisintegrationDistance))
			{
				return (short)ModConfig.capabilityPlayers.passiveDisintegrationRate;
			}
		}
		else if (ent instanceof EntityLiving)
		{
			EntityLiving entLiving = (EntityLiving)ent;
			if (ModConfig.capability.passiveDisintegrationDistance > 0
					&& EntityHelper.getEntityIsAfterAnother(entLiving, player)
					&& EntityHelper.getEntityIsWithinRadiusOfAnother(entLiving, player, ModConfig.capability.passiveDisintegrationDistance)
					&& livingSatisfiesLists(entLiving))
			{
				return (short)ModConfig.capability.passiveDisintegrationRate;
			}
		}
		
		return 0;
	}
	
	@Override
	public void resetMitigationCooldown()
	{
		disMitCd = ModConfig.capability.disintegrationMitigationCooldown;
	}
	
	@Override
	public void decrementMitigationCooldown()
	{
		disMitCd = Utils.clamp(disMitCd - 1, 0, ModConfig.capability.disintegrationMitigationCooldown);
	}
	
	@Override
	public boolean mitigationCooldownIsUp()
	{
		return disMitCd <= 0;
	}
	
	private boolean provoke(short increment, EntityPlayer player)
	{
		if (increment > 0 || player == null)
		{
			incrementAndSync(increment, player);
			resetMitigationCooldown();
			return true;
		}
		return false;
	}
	
	private boolean livingSatisfiesLists(EntityLivingBase living)
	{
		return livingSatisfiesList(living, true) && livingSatisfiesList(living, false);
	}
	
	private boolean livingSatisfiesList(EntityLivingBase living, boolean whitelist)
	{
		ResourceLocation resource = EntityList.getKey(living);
		if (resource == null)
		{
			return false;
		}
		String[] list = whitelist ? ModConfig.capability.creatureWhitelist : ModConfig.capability.creatureBlacklist;
		if (list == null || list.length <= 0)
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
}