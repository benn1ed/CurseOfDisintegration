package benn1ed.curseofdisintegration.network;

import benn1ed.curseofdisintegration.capability.IDisintegration;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class NetPacketDIValueHandler implements IMessageHandler<NetPacketDIValue, IMessage>
{
	@Override
	public IMessage onMessage(NetPacketDIValue message, MessageContext ctx)
	{
		if (ctx.side != Side.CLIENT)
		{
			return null;
		}
		
		Minecraft.getMinecraft().addScheduledTask(() ->
		{
			IDisintegration.onPacketReceivedClient(message);
		});
		
		return null;
	}
}