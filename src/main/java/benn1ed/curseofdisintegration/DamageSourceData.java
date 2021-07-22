package benn1ed.curseofdisintegration;

public class DamageSourceData
{
	public static final String KEY = ModData.MODID.concat(".disintegration");
	public static final byte SOURCE_COUNT = 5;
	
	public static final DisintegrationDamageSource[] dds;
	
	static
	{
		dds = new DisintegrationDamageSource[SOURCE_COUNT];
		for(int i = 0; i < SOURCE_COUNT; i++)
		{
			dds[i] = new DisintegrationDamageSource(KEY.concat(".type" + Integer.toString(i)));
		}
	}
}