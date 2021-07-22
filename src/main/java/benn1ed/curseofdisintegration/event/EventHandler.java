package benn1ed.curseofdisintegration.event;

import benn1ed.curseofdisintegration.CurseOfDisintegration;
import benn1ed.curseofdisintegration.ModData;
import benn1ed.curseofdisintegration.capability.DisintegrationProvider;
import benn1ed.curseofdisintegration.capability.IDisintegration;
import benn1ed.curseofdisintegration.client.DisintegrationBar;
import benn1ed.curseofdisintegration.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class EventHandler
{
	Minecraft mc;
    public static ResourceLocation res = new ResourceLocation(ModData.MODID, "textures/di_icon_full.png");
	
    public EventHandler(Side side)
    {
    	if (side.equals(Side.CLIENT))
    	{
    		mc = Minecraft.getMinecraft();
    	}
    }
    
	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<Entity> event)
	{
		Entity ent = event.getObject();
		if (ent instanceof EntityPlayer)
		{
			event.addCapability(new ResourceLocation(ModData.MODID, "disintegration"), new DisintegrationProvider());
		}
	}
	
	@SubscribeEvent
	public void livingDamage(LivingDamageEvent event)
	{
		Entity ent = event.getEntity();
		if (event.getAmount() <= 0 && ent instanceof EntityPlayer)
		{
			return;
		}
		Entity source = event.getSource().getTrueSource();
		boolean sourceIsPlayer = source instanceof EntityPlayer;
		boolean entIsPlayer = ent instanceof EntityPlayer;
		if (!(sourceIsPlayer || entIsPlayer))
		{
			return;
		}
		if (sourceIsPlayer)
		{
			EntityPlayer sourcePlayer = (EntityPlayer)source;
			IDisintegration d = IDisintegration.getFor(sourcePlayer);
			if (d != null)
			{
				d.onHit(sourcePlayer, event);
			}
		}
		if (entIsPlayer)
		{
			EntityPlayer entPlayer = (EntityPlayer)ent;
			IDisintegration d = IDisintegration.getFor(entPlayer);
			if (d != null)
			{
				d.onHurt(entPlayer, event);
			}
		}
	}
	
	@SubscribeEvent
	public void worldTick(WorldTickEvent event)
	{
		if (event.side.equals(Side.CLIENT))
		{
			return;
		}
		tickDisintegrationCapabilities(event);
	}
	
	@SubscribeEvent
	public void renderGameOverlay(RenderGameOverlayEvent.Pre event)
	{
		if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
		{
			ScaledResolution res = event.getResolution();
			CurseOfDisintegration.bar.draw(res.getScaledWidth(), res.getScaledHeight());
		}
	}
	
	@SubscribeEvent
	public void playerLoggedIn(PlayerLoggedInEvent event)
	{
		EntityPlayer p = event.player;
		if (!(p instanceof EntityPlayerMP))
		{
			return;
		}
		IDisintegration d = IDisintegration.getFor(p);
		d.setAndSync(d.getValue(), p);
		d.resetMitigationCooldown();
	}
	
	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event)
	{
		if (event.getModID().equals(ModData.MODID))
		{
			ConfigManager.sync(event.getModID(), Config.Type.INSTANCE);
			DisintegrationBar bar = CurseOfDisintegration.bar;
			if (bar != null)
			{
				bar.location = ModConfig.client.barLocation;
			}
		}
	}
	
	private void tickDisintegrationCapabilities(WorldTickEvent event)
	{
		event.world.playerEntities.forEach(ent -> tickDisintegration(ent));
	}
	
	private void tickDisintegration(EntityPlayer player)
	{
		IDisintegration d = IDisintegration.getFor(player);
		if (d != null)
		{
			d.tick(player);
		}
	}
}