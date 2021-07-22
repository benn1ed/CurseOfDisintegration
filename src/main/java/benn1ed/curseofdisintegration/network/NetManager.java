package benn1ed.curseofdisintegration.network;

import benn1ed.curseofdisintegration.ModData;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetManager
{
	private static final String PROTOCOL_VERSION = "1";
	
	public static final SimpleChannel NETWORK_CHANNEL
		= NetworkRegistry.newSimpleChannel
			(
				new ResourceLocation(ModData.MODID, "main"),
				() -> PROTOCOL_VERSION,
				PROTOCOL_VERSION::equals,
				PROTOCOL_VERSION::equals
			);
}