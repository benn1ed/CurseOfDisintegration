package benn1ed.curseofdisintegration.capability;

import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.*;

public class DisintegrationProvider implements ICapabilitySerializable<NBTTagCompound>
{
	@CapabilityInject(IDisintegration.class)
	public static final Capability<IDisintegration> DISINTEGRATION_CAPABILITY = null;
	
	private final IDisintegration _disintegration = new Disintegration();
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == DISINTEGRATION_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability == DISINTEGRATION_CAPABILITY)
		{
			return DISINTEGRATION_CAPABILITY.cast(_disintegration);
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return (NBTTagCompound)DISINTEGRATION_CAPABILITY.getStorage().writeNBT(DISINTEGRATION_CAPABILITY, _disintegration, null);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		DISINTEGRATION_CAPABILITY.getStorage().readNBT(DISINTEGRATION_CAPABILITY, _disintegration, null, nbt);
	}
}