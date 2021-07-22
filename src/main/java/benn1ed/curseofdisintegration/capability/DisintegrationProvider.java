package benn1ed.curseofdisintegration.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class DisintegrationProvider implements ICapabilitySerializable<CompoundNBT>
{
	@CapabilityInject(IDisintegration.class)
	public static final Capability<IDisintegration> DISINTEGRATION_CAPABILITY = null;
	
	private final IDisintegration _disintegration = DISINTEGRATION_CAPABILITY.getDefaultInstance();
	
	@Override
	public CompoundNBT serializeNBT()
	{
		return (CompoundNBT)DISINTEGRATION_CAPABILITY.getStorage().writeNBT(DISINTEGRATION_CAPABILITY, _disintegration, null);
	}
	
	@Override
	public void deserializeNBT(CompoundNBT nbt)
	{
		DISINTEGRATION_CAPABILITY.getStorage().readNBT(DISINTEGRATION_CAPABILITY, _disintegration, null, nbt);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
	{
		return (LazyOptional<T>)LazyOptional.of(cap == DISINTEGRATION_CAPABILITY ? () -> _disintegration : null);
	}
}