package benn1ed.curseofdisintegration;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.b3d.B3DModel.Texture;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Random;

import org.apache.logging.log4j.Logger;

import benn1ed.curseofdisintegration.capability.Disintegration;
import benn1ed.curseofdisintegration.capability.DisintegrationStorage;
import benn1ed.curseofdisintegration.capability.IDisintegration;
import benn1ed.curseofdisintegration.client.DisintegrationBar;
import benn1ed.curseofdisintegration.config.ModConfig;
import benn1ed.curseofdisintegration.event.*;
import benn1ed.curseofdisintegration.network.NetManager;
import benn1ed.curseofdisintegration.network.NetPacketDIValue;
import benn1ed.curseofdisintegration.network.NetPacketDIValueHandler;

@SuppressWarnings({"unused"})
@Mod(modid = ModData.MODID, name = ModData.NAME, version = ModData.VERSION)
public class CurseOfDisintegration
{
    public static Logger logger;
    public static DisintegrationBar bar;
    protected static int packetDiscriminator = 0;
    
	@EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
		if (!ModConfig.general.enableMod)
		{
			return;
		}
		if (event.getSide().equals(Side.CLIENT))
		{
			bar = new DisintegrationBar();
		}
    	CapabilityManager.INSTANCE.register(IDisintegration.class, new DisintegrationStorage(), Disintegration::new);
    	MinecraftForge.EVENT_BUS.register(new benn1ed.curseofdisintegration.event.EventHandler(event.getSide()));
    	NetManager.NETWORK_WRAPPER_INSTANCE.registerMessage(NetPacketDIValueHandler.class, NetPacketDIValue.class, packetDiscriminator++, Side.CLIENT);
        logger = event.getModLog();
    }
	
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    }
}