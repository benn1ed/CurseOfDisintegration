package benn1ed.curseofdisintegration;

import benn1ed.curseofdisintegration.util.Utils;
import net.minecraft.util.DamageSource;

public class DisintegrationDamageSource extends DamageSource
{
	public DisintegrationDamageSource(String damageTypeIn)
	{
		super(damageTypeIn);
		bypassArmor();
		bypassMagic();
		bypassInvul();
	}
	
	public static DisintegrationDamageSource getRandom()
	{
		DisintegrationDamageSource[] dds = DamageSourceData.dds;
		return dds[Utils.getRandomIntInRange(0, DamageSourceData.SOURCE_COUNT - 1)];
	}
}