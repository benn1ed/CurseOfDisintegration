package benn1ed.curseofdisintegration.capability;

import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.capabilities.Capability.*;

public class DisintegrationStorage implements IStorage<IDisintegration>
{
	@Override
	public NBTBase writeNBT(Capability<IDisintegration> capability, IDisintegration instance, EnumFacing side)
	{
		NBTTagCompound compound = new NBTTagCompound();
		compound.setShort(Disintegration.KEY, instance.getValue());
		return compound;
	}

	@Override
	public void readNBT(Capability<IDisintegration> capability, IDisintegration instance, EnumFacing side, NBTBase nbt)
	{
		if (!(nbt instanceof NBTTagCompound))
		{
			return;
		}
		NBTTagCompound compound = (NBTTagCompound)nbt;
		instance.setValue(compound.getShort(Disintegration.KEY));
	}
}