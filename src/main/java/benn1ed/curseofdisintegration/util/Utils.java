package benn1ed.curseofdisintegration.util;

import java.util.*;
import java.util.function.*;

public class Utils
{
	public static final Random random = new Random();
	
	public static int getRandomIntInRange(int min, int max)
	{
		return (int)Math.round(random.nextDouble() * (max - min) + min);
	}
	
	public static short clamp(short value, short min, short max)
	{
		return (short)clamp((long)value, (long)min, (long)max);
	}
	
	public static int clamp(int value, int min, int max)
	{
		return (int)clamp((long)value, (long)min, (long)max);
	}
	
	public static long clamp(long value, long min, long max)
	{
		if (value < min)
		{
			return min;
		}
		if (value > max)
		{
			return max;
		}
		return value;
	}
	
	public static <T> boolean atLeastOne(T[] array, Predicate<T> predicate)
	{
		for(T t : array)
		{
			if (predicate.test(t))
			{
				return true;
			}
		}
		return false;
	}
	
	public static <T> boolean atLeastOne(Iterable<T> collection, Predicate<T> predicate)
	{
		for(T t : collection)
		{
			if (predicate.test(t))
			{
				return true;
			}
		}
		return false;
	}
	
	public static <T> boolean contains(Iterable<T> collection, T item)
	{
		return atLeastOne(collection, t -> item == t || item.equals(t));
	}
}