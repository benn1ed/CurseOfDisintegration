package benn1ed.curseofdisintegration.client;

import benn1ed.curseofdisintegration.Frames;
import benn1ed.curseofdisintegration.util.Utils;

public class FlashFrames extends Frames
{
	private int _flashes;
	private Frames frames;
	
	public FlashFrames(int nonFlashFrames, int flashFrames)
	{
		super(nonFlashFrames);
		frames = new Frames(flashFrames);
		frames.makeDone();
	}
	
	@Override
	public void tick()
	{
		if (!done())
		{
			super.tick();
			if (done() && hasFlashes())
			{
				frames.reset();
			}
		}
		else
		{
			if (!frames.done() && hasFlashes())
			{
				frames.tick();
			} 
			else
			{
				reset();
				decrementFlashes();
			}
		}
	}
	
	public boolean flash()
	{
		return !frames.done();
	}

	public void setFlashes(int flashes)
	{
		_flashes = flashes;
		frames.reset();
		makeDone();
	}
	
	public boolean hasFlashes()
	{
		return _flashes > 0;
	}
	
	private void decrementFlashes()
	{
		_flashes = Utils.clamp(_flashes - 1, 0, Integer.MAX_VALUE);
	}
}