package benn1ed.curseofdisintegration.event;

import benn1ed.curseofdisintegration.ModData;
import benn1ed.curseofdisintegration.capability.DisintegrationProvider;
import benn1ed.curseofdisintegration.capability.IDisintegration;
import benn1ed.curseofdisintegration.client.ClientData;
import net.minecraft.client.MainWindow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

public class EventHandler
{
    public static ResourceLocation res = new ResourceLocation(ModData.MODID, "textures/di_icon_full.png");
	
    public EventHandler()
    {
    }
    
	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<Entity> event)
	{
		Entity ent = event.getObject();
		if (ent instanceof PlayerEntity)
		{
			event.addCapability(new ResourceLocation(ModData.MODID, "disintegration"), new DisintegrationProvider());
		}
	}
	
	@SubscribeEvent
	public void livingHurt(LivingDamageEvent event)
	{
		Entity ent = event.getEntity();
		if (event.getAmount() <= 0 && ent instanceof PlayerEntity)
		{
			return;
		}
		Entity source = event.getSource().getEntity();
		boolean sourceIsPlayer = source instanceof PlayerEntity;
		boolean entIsPlayer = ent instanceof PlayerEntity;
		if (!(sourceIsPlayer || entIsPlayer))
		{
			return;
		}
		if (sourceIsPlayer)
		{
			PlayerEntity sourcePlayer = (PlayerEntity)source;
			IDisintegration d = IDisintegration.getFor(sourcePlayer);
			if (d != null)
			{
				d.onHit(sourcePlayer, event);
			}
		}
		if (entIsPlayer)
		{
			PlayerEntity entPlayer = (PlayerEntity)ent;
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
		if (!event.side.equals(LogicalSide.SERVER))
		{
			return;
		}
		tickDisintegrationCapabilities(event);
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void renderGameOverlay(RenderGameOverlayEvent.Pre event)
	{
		MainWindow window = event.getWindow();
		if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
		{
			ClientData.getBar().draw(event.getMatrixStack(), window.getGuiScaledWidth(), window.getGuiScaledHeight());
		}
	}
	
	@SubscribeEvent
	public void playerLoggedIn(PlayerLoggedInEvent event)
	{
		PlayerEntity p = event.getPlayer();
		if (!(p instanceof ServerPlayerEntity))
		{
			return;
		}
		IDisintegration d = IDisintegration.getFor(p);
		d.setAndSync(d.getValue(), p);
		d.resetMitigationCooldown();
	}
	
	private void tickDisintegrationCapabilities(WorldTickEvent event)
	{
		event.world.players().forEach(ent -> tickDisintegration(ent));
	}
	
	private void tickDisintegration(PlayerEntity player)
	{
		IDisintegration d = IDisintegration.getFor(player);
		if (d != null)
		{
			d.tick(player);
		}
	}
}