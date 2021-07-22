package benn1ed.curseofdisintegration.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class NetPacketDIValue implements IMessage
{
	public short newValue = 0;
	
	public NetPacketDIValue()
	{
	}
	
	public NetPacketDIValue(short value)
	{
		newValue = value;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		newValue = buf.readShort();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeShort(newValue);
	}
}