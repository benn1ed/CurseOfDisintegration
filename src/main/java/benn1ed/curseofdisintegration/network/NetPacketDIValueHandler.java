package benn1ed.curseofdisintegration.network;

import benn1ed.curseofdisintegration.capability.*;
import net.minecraft.client.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

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