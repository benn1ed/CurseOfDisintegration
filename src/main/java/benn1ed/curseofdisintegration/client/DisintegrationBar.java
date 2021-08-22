package benn1ed.curseofdisintegration.client;

import java.awt.*;
import java.util.*;
import org.lwjgl.opengl.*;
import benn1ed.curseofdisintegration.*;
import benn1ed.curseofdisintegration.capability.*;
import benn1ed.curseofdisintegration.config.*;
import benn1ed.curseofdisintegration.util.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;

public class DisintegrationBar
{
	public final Point position;
	public DisintegrationBarLocation location = ModConfig.client.barLocation;
	
	private final Minecraft _mc = Minecraft.getMinecraft();
	private final Random _random = new Random();
	private final long _flashTime = 800;
	private final long _flashPeriod = _flashTime / 5;
	private long _flashTimeSnapshot;

	public DisintegrationBar()
	{
		this(new Point());
	}

	public DisintegrationBar(int x, int y)
	{
		this(new Point(x, y));
	}

	public DisintegrationBar(Point pos)
	{
		if (pos == null)
		{
			pos = new Point();
		}
		position = pos;
	}

	public void draw(int scaledWidth, int scaledHeight)
	{
		EntityPlayer p = _mc.player;
		if (p.isCreative() || p.isSpectator())
		{
			return;
		}

		IDisintegration d = getDisintegration();
		if (d == null)
		{
			return;
		}
		short dis = d.getValue();
		if (dis <= 0 && ModConfig.client.hideBarAtZero)
		{
			return;
		}
		
		GL11.glEnable(GL11.GL_BLEND);

		long delta = Minecraft.getSystemTime() - _flashTimeSnapshot;
		double ratio = (double) dis / d.getMaxDisintegration();
		int count = (int) Math.floor(ratio * 20);
		boolean highRatio = ratio >= 0.55d;
		boolean veryHighRatio = ratio >= 0.75d;
		boolean flash = ModConfig.client.enableFlashing && delta <= _flashTime;
		if (flash)
		{
			long period = delta / _flashPeriod;
			flash = period == 0 || period % 2 == 0;
		}
		boolean twitch = ModConfig.client.enableTwitching && (flash || highRatio);
		int twitchingAmplitude = veryHighRatio ? 2 : 1;

		updatePosition(scaledWidth, scaledHeight);

		for (int i = 0; i < 10; i++, count -= 2)
		{
			drawOneIcon(i, getIdFromCount(count, ratio, p), flash, twitch, twitchingAmplitude, !ModConfig.client.useAlternativeDesign ? ModData.ICONS : ModData.ICONS_FACES);
		}
	}

	public void setJustChanged(short oldV, short newV)
	{
		if (newV > oldV)
		{
			resetFlashTimeSnapshot();
		}
	}

	private void resetFlashTimeSnapshot()
	{
		_flashTimeSnapshot = Minecraft.getSystemTime();
	}

	private void drawOneIcon(int num, int id, boolean flash, boolean twitch, int twitchingAmplitude, ResourceLocation[] icons)
	{
		if (icons == null || icons.length <= 0)
		{
			return;
		}
		int _id = Utils.clamp(id + (flash ? 1 : 0), 0, icons.length - 1);
		_mc.getTextureManager().bindTexture(icons[_id]);
		GuiIngame.drawModalRectWithCustomSizedTexture(position.x + 8 * num, position.y + (twitch ? (int) Math.round((_random.nextDouble() * (twitchingAmplitude * 2) - twitchingAmplitude)) : 0), 0, 0,
				9, 9, 9, 9);
	}

	private int getIdFromCount(int count, double ratio, EntityPlayer p)
	{
		if (!ModConfig.client.useAlternativeDesign)
		{
			return count > 0 ? (count == 1 ? 2 : 0) : 4;
		}
		else
		{
			return count > 0 ? (count == 1 ? 4 : 2) + 4 * Utils.clamp((int) Math.floor(ratio / 0.25d), 0, 3) : 0;
		}
	}

	private void updatePosition(int scaledWidth, int scaledHeight)
	{
		switch (location)
		{
		case ABOVE_HEALTH_BAR:
			position.x = scaledWidth / 2 - 91;
			position.y = scaledHeight - 49;
			int armor = _mc.player.getTotalArmorValue();
			position.y -= 10 * (armor / 20 + Utils.clamp(armor % 20, 0, 1));
			break;
		case ABOVE_HUNGER_BAR:
			position.x = scaledWidth / 2 + 10;
			position.y = scaledHeight - 49;
			break;
		case HOTBAR_LEFT:
			position.x = scaledWidth / 2 - 180;
			position.y = scaledHeight - 16;
			break;
		case HOTBAR_RIGHT:
			position.x = scaledWidth / 2 + 99;
			position.y = scaledHeight - 16;
			break;
		case TOP_LEFT:
			position.x = position.y = 10;
			break;
		case TOP_CENTER:
			position.x = scaledWidth / 2 - 40;
			position.y = 10;
			break;
		case TOP_RIGHT:
			position.x = scaledWidth - 90;
			position.y = 10;
			break;
		case MIDDLE_LEFT:
			position.x = 10;
			position.y = scaledHeight / 2 - 4;
			break;
		case MIDDLE_CENTER:
			position.x = scaledWidth / 2 - 40;
			position.y = scaledHeight / 2 - 4;
			break;
		case MIDDLE_RIGHT:
			position.x = scaledWidth - 90;
			position.y = scaledHeight / 2 - 4;
			break;
		case BOTTOM_LEFT:
			position.x = 10;
			position.y = scaledHeight - 18;
			break;
		case BOTTOM_RIGHT:
			position.x = scaledWidth - 90;
			position.y = scaledHeight - 18;
			break;
		case CUSTOM:
			position.x = ModConfig.client.customPosX;
			position.y = ModConfig.client.customPosY;
			break;
		case CUSTOM_SCALED:
			position.x = scaledWidth - ModConfig.client.scaledPosX;
			position.y = scaledHeight - ModConfig.client.scaledPosY;
			break;
		default:
			break;
		}
		position.x += ModConfig.client.additionalXValue;
		position.y += ModConfig.client.additionalYValue;
	}

	private IDisintegration getDisintegration()
	{
		return IDisintegration.getFor(_mc.player);
	}
}