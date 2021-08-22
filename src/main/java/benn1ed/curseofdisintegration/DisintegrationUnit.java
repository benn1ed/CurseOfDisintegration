package benn1ed.curseofdisintegration;

import benn1ed.curseofdisintegration.config.*;
import benn1ed.curseofdisintegration.util.*;

public class DisintegrationUnit
{
	private short _value = 0;
	private short _max = (short)ModConfig.capability.maxDisintegration;
	
	public DisintegrationUnit()
	{
	}
	
	public DisintegrationUnit(short value)
	{
		set(value);
	}
	
	public DisintegrationUnit(short value, short maxValue)
	{
		this(value);
		setMax(maxValue);
	}
	
	public short get()
	{
		return _value;
	}
	
	public void set(short value)
	{
		_value = Utils.clamp(value, getMin(), getMax());
	}
	
	public void increment(short value)
	{
		set((short)(get() + value));
	}
	
	public void setMax(short value)
	{
		_max = value;
	}
	
	public short getMax()
	{
		return _max;
	}
	
	public static short getMin()
	{
		return 0;
	}
}