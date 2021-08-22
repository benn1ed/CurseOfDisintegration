package benn1ed.curseofdisintegration;

import benn1ed.curseofdisintegration.util.*;

public class Frames
{
	private int _maxValue;
	private int _framesPassed = 0;
	
	public Frames(int maxFrames)
	{
		setMaxFrames(maxFrames);
	}
	
	public int getMaxFrames()
	{
		return _maxValue;
	}
	
	public void setMaxFrames(int value)
	{
		_maxValue = value;
	}
	
	public int getFramesPassed()
	{
		return _framesPassed;
	}
	
	private void setFramesPassed(int value)
	{
		_framesPassed = Utils.clamp(value, 0, getMaxFrames());
	}
	
	private void incrementFramesPassed()
	{
		setFramesPassed(getFramesPassed() + 1);
	}
	
	public int getFramesLeft()
	{
		return getMaxFrames() - getFramesPassed();
	}
	
	public void tick()
	{
		incrementFramesPassed();
	}
	
	public void reset()
	{
		setFramesPassed(0);
	}
	
	public boolean done()
	{
		return getFramesPassed() >= getMaxFrames();
	}
	
	public void makeDone()
	{
		setFramesPassed(getMaxFrames());
	}
}