package benn1ed.curseofdisintegration;

import benn1ed.curseofdisintegration.util.*;
import net.minecraft.util.*;

public class DisintegrationDamageSource extends DamageSource
{
	public DisintegrationDamageSource(String damageTypeIn)
	{
		super(damageTypeIn);
		setDamageBypassesArmor().setDamageIsAbsolute();
	}
	
	@Override
	public boolean isUnblockable()
	{
		return true;
	}
	
	public static DisintegrationDamageSource getRandom()
	{
		DisintegrationDamageSource[] dds = DamageSourceData.dds;
		return dds[Utils.getRandomIntInRange(0, DamageSourceData.SOURCE_COUNT - 1)];
	}
}