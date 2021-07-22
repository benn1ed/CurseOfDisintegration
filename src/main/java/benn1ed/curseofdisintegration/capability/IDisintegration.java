package benn1ed.curseofdisintegration.capability;

import benn1ed.curseofdisintegration.CurseOfDisintegration;
import benn1ed.curseofdisintegration.DisintegrationDamageSource;
import benn1ed.curseofdisintegration.client.DisintegrationBar;
import benn1ed.curseofdisintegration.network.NetPacketDIValue;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IDisintegration
{
	short getValue();
	
	void setValue(short value);
	
	void incrementValue(short value);
	
	short getMaxDisintegration();
	
	void tick(EntityPlayer player);
	
	void onHurt(EntityPlayer player, LivingDamageEvent event);
	
	void onHit(EntityPlayer player, LivingDamageEvent event);
	
	void resetMitigationCooldown();
	
	void decrementMitigationCooldown();
	
	boolean mitigationCooldownIsUp();
	
	void sync(EntityPlayer player);
	
	void setAndSync(short value, EntityPlayer player);
	
	void incrementAndSync(short increment, EntityPlayer player);

	default boolean reachedLimit()
	{
		return getValue() >= getMaxDisintegration();
	}
	
	public static void disintegrate(EntityPlayer player)
	{
		player.attackEntityFrom(DisintegrationDamageSource.getRandom(), Float.MAX_VALUE);
	}
	
	public static IDisintegration getFor(EntityPlayer player)
	{
		return player.getCapability(DisintegrationProvider.DISINTEGRATION_CAPABILITY, null);
	}
	
	@SideOnly(Side.CLIENT)
	public static IDisintegration getLocal()
	{
		return getFor(Minecraft.getMinecraft().player);
	}

	@SideOnly(Side.CLIENT)
	public static void onPacketReceivedClient(NetPacketDIValue packet)
	{
		IDisintegration d = getLocal();
		DisintegrationBar bar = CurseOfDisintegration.bar;
		if (d != null && bar != null)
		{
			bar.setJustChanged(d.getValue(), packet.newValue);
			d.setValue(packet.newValue);
		}
	}
}