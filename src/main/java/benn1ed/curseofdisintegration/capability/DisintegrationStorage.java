package benn1ed.curseofdisintegration.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class DisintegrationStorage implements IStorage<IDisintegration>
{
	@Override
	public INBT writeNBT(Capability<IDisintegration> capability, IDisintegration instance, Direction side)
	{
		CompoundNBT nbt = new CompoundNBT();
		nbt.putShort(Disintegration.KEY, instance.getValue());
		return nbt;
	}

	@Override
	public void readNBT(Capability<IDisintegration> capability, IDisintegration instance, Direction side, INBT nbt)
	{
		if (!(nbt instanceof CompoundNBT))
		{
			return;
		}
		CompoundNBT compound = (CompoundNBT)nbt;
		instance.setValue(compound.getShort(Disintegration.KEY));
	}
}