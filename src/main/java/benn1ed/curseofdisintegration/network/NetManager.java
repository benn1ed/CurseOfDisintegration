package benn1ed.curseofdisintegration.network;

import benn1ed.curseofdisintegration.ModData;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class NetManager
{
	public static final SimpleNetworkWrapper NETWORK_WRAPPER_INSTANCE
		= NetworkRegistry.INSTANCE.newSimpleChannel(ModData.MODID.substring(0, Math.min(ModData.MODID.length(), 20)));
}