package benn1ed.curseofdisintegration.capability;

import benn1ed.curseofdisintegration.DisintegrationDamageSource;
import benn1ed.curseofdisintegration.client.ClientData;
import benn1ed.curseofdisintegration.client.DisintegrationBar;
import benn1ed.curseofdisintegration.network.NetPacketDIValue;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public interface IDisintegration
{
	short getValue();
	
	void setValue(short value);
	
	void incrementValue(short value);
	
	short getMaxDisintegration();
	
	void tick(PlayerEntity player);
	
	void onHurt(PlayerEntity player, LivingDamageEvent event);
	
	void onHit(PlayerEntity player, LivingDamageEvent event);
	
	void resetMitigationCooldown();
	
	void decrementMitigationCooldown();
	
	boolean mitigationCooldownIsUp();
	
	void sync(PlayerEntity player);
	
	void setAndSync(short value, PlayerEntity player);
	
	void incrementAndSync(short increment, PlayerEntity player);

	default boolean reachedLimit()
	{
		return getValue() >= getMaxDisintegration();
	}
	
	public static void disintegrate(PlayerEntity player)
	{
		player.hurt(DisintegrationDamageSource.getRandom(), Float.MAX_VALUE);
	}
	
	public static IDisintegration getFor(PlayerEntity player)
	{
		return player.getCapability(DisintegrationProvider.DISINTEGRATION_CAPABILITY, null).orElse(null);
	}
	
	@SuppressWarnings("resource")
	@OnlyIn(Dist.CLIENT)
	public static IDisintegration getLocal()
	{
		return getFor(Minecraft.getInstance().player);
	}

	@OnlyIn(Dist.CLIENT)
	public static void onPacketReceivedClient(NetPacketDIValue packet)
	{
		IDisintegration d = getLocal();
		DisintegrationBar bar = ClientData.getBar();
		if (d != null && bar != null)
		{
			bar.setJustChanged(d.getValue(), packet.newValue);
			d.setValue(packet.newValue);
		}
	}
}