package benn1ed.curseofdisintegration;

import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraftforge.client.model.b3d.B3DModel.*;
import net.minecraftforge.common.*;
import net.minecraftforge.common.brewing.*;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraftforge.registries.*;

import java.util.*;
import org.apache.logging.log4j.*;
import benn1ed.curseofdisintegration.capability.*;
import benn1ed.curseofdisintegration.client.*;
import benn1ed.curseofdisintegration.config.*;
import benn1ed.curseofdisintegration.integration.*;
import benn1ed.curseofdisintegration.network.*;
import benn1ed.curseofdisintegration.potion.*;

@SuppressWarnings({"unused"})
@Mod(modid = ModData.MODID, name = ModData.NAME, version = ModData.VERSION)
public class CurseOfDisintegration
{
	public static IntegrationManager integrationManager;
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
    	MinecraftForge.EVENT_BUS.register(new benn1ed.curseofdisintegration.event.EventHandler());
    	NetManager.NETWORK_WRAPPER_INSTANCE.registerMessage(NetPacketDIValueHandler.class, NetPacketDIValue.class, packetDiscriminator++, Side.CLIENT);
        logger = event.getModLog();
    }
	
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	if (ModConfig.general.registerPotionsAndItems)
    	{
    		PotionManager.registerRecipes();
    	}
    	integrationManager = new IntegrationManager();
	}
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
    	logger.debug("Registering command 'codi'.");
    	event.registerServerCommand(new DisintegrationCommand());
    }
}