package benn1ed.curseofdisintegration.event;

import benn1ed.curseofdisintegration.*;
import benn1ed.curseofdisintegration.capability.*;
import benn1ed.curseofdisintegration.client.*;
import benn1ed.curseofdisintegration.config.*;
import benn1ed.curseofdisintegration.item.*;
import benn1ed.curseofdisintegration.potion.*;
import net.minecraft.client.gui.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.config.*;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.*;
import net.minecraftforge.fml.common.gameevent.TickEvent.*;
import net.minecraftforge.fml.relauncher.*;

public class EventHandler
{
	public static ResourceLocation res = new ResourceLocation(ModData.MODID, "textures/di_icon_full.png");
	
	public EventHandler()
	{
	}
	
	@SubscribeEvent
	public void onLootTableLoad(LootTableLoadEvent event)
	{
		if (ModConfig.general.registerPotionsAndItems)
		{
			ItemManager.modifyLootTables(event);
		}
	}
	
	@SubscribeEvent
	public void onRegisterItems(RegistryEvent.Register<Item> event)
	{
		if (ModConfig.general.registerPotionsAndItems)
		{
			ItemManager.registerItems(event.getRegistry());
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event)
	{
		if (ModConfig.general.registerPotionsAndItems)
		{
			ItemManager.addItemTooltips(event);
		}
	}
	
	@SubscribeEvent
	public void onModelRegistry(ModelRegistryEvent event)
	{
		if (ModConfig.general.registerPotionsAndItems)
		{
			ItemManager.registerModels(event);
		}
	}
	
	@SubscribeEvent
	public void onRegisterPotions(RegistryEvent.Register<Potion> event)
	{
		if (ModConfig.general.registerPotionsAndItems)
		{
			PotionManager.registerPotion(event.getRegistry());
		}
	}

	@SubscribeEvent
	public void onRegisterPotionTypes(RegistryEvent.Register<PotionType> event)
	{
		if (ModConfig.general.registerPotionsAndItems)
		{
			PotionManager.registerPotionType(event.getRegistry());
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
			EntityPlayer sourcePlayer = (EntityPlayer) source;
			IDisintegration d = IDisintegration.getFor(sourcePlayer);
			if (d != null)
			{
				d.onHit(sourcePlayer, event);
			}
		}
		if (entIsPlayer)
		{
			EntityPlayer entPlayer = (EntityPlayer) ent;
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