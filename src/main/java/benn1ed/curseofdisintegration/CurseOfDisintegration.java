package benn1ed.curseofdisintegration;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.b3d.B3DModel.Texture;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import benn1ed.curseofdisintegration.capability.Disintegration;
import benn1ed.curseofdisintegration.capability.DisintegrationStorage;
import benn1ed.curseofdisintegration.capability.IDisintegration;
import benn1ed.curseofdisintegration.client.ClientData;
import benn1ed.curseofdisintegration.client.DisintegrationBar;
import benn1ed.curseofdisintegration.config.Config;
import benn1ed.curseofdisintegration.event.*;
import benn1ed.curseofdisintegration.network.NetManager;
import benn1ed.curseofdisintegration.network.NetPacketDIValue;
import benn1ed.curseofdisintegration.network.NetPacketDIValueHandler;

@SuppressWarnings({"unused"})
@Mod(value = ModData.MODID)
public class CurseOfDisintegration
{
    public static final Logger LOGGER = LogManager.getLogger();
    protected static int packetDiscriminator = 0;
    
    public CurseOfDisintegration()
    {
    	FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    	FMLJavaModLoadingContext.get().getModEventBus().addListener(this::client);
    	ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.CONFIG);
    	MinecraftForge.EVENT_BUS.register(new EventHandler());
    	NetManager.NETWORK_CHANNEL.registerMessage(
    			packetDiscriminator++,
    			NetPacketDIValue.class,
    			NetPacketDIValueHandler::encode,
    			NetPacketDIValueHandler::decode,
    			NetPacketDIValueHandler::handle);
    }
    
    private void setup(final FMLCommonSetupEvent event)
    {
    	CapabilityManager.INSTANCE.register(IDisintegration.class, new DisintegrationStorage(), Disintegration::new);
    }
    
    private void client(final FMLClientSetupEvent event)
    {
    	ClientData.init();
    }
}