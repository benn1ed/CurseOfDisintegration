package benn1ed.curseofdisintegration.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

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