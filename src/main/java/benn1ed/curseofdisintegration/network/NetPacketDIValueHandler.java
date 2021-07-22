package benn1ed.curseofdisintegration.network;

import java.util.function.Supplier;

import benn1ed.curseofdisintegration.capability.IDisintegration;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class NetPacketDIValueHandler
{
	public static void handle(NetPacketDIValue message, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleOnClient(message, ctx));
		});
		ctx.get().setPacketHandled(true);
	}
	
	private static void handleOnClient(NetPacketDIValue message, Supplier<NetworkEvent.Context> ctx)
	{
		IDisintegration.onPacketReceivedClient(message);
	}
	
	public static void encode(NetPacketDIValue message, PacketBuffer buffer)
	{
		if (message == null || buffer == null)
		{
			return;
		}
		buffer.writeShort(message.newValue);
	}
	
	public static NetPacketDIValue decode(PacketBuffer buffer)
	{
		if (buffer == null)
		{
			return null;
		}
		return new NetPacketDIValue(buffer.readShort());
	}
}