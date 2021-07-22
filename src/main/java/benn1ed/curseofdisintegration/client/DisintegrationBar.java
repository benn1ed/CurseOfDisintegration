package benn1ed.curseofdisintegration.client;

import java.awt.Point;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;

import benn1ed.curseofdisintegration.capability.IDisintegration;
import benn1ed.curseofdisintegration.config.Config;
import benn1ed.curseofdisintegration.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DisintegrationBar
{
	public final Point position;
	public final FlashFrames flashFrames = new FlashFrames(10, 15);
	public DisintegrationBarLocation location = Config.CLIENT.barLocation.get();
	private final Random _random = new Random();
	private final Minecraft _mc;
	
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
		_mc = Minecraft.getInstance();
	}
	
	public void draw(MatrixStack matrixStack, int scaledWidth, int scaledHeight)
	{
		PlayerEntity p = _mc.player;
		if (p.isCreative() || p.isSpectator())
		{
			return;
		}
		
		IDisintegration d = getDisintegration();
		if (d == null)
		{
			return;
		}
		
		boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
		if (!blend)
		{
			GL11.glEnable(GL11.GL_BLEND);
		}
		
		short dis = d.getValue();
		double ratio = (double)dis / d.getMaxDisintegration();
		int count = (int)Math.floor(ratio * 20);
		boolean highRatio = ratio >= 0.55d;
		boolean veryHighRatio = ratio >= 0.75d;
		boolean flash = Config.CLIENT.enableFlashing.get() && flashFrames.flash();
		boolean twitch = Config.CLIENT.enableTwitching.get() && (flashFrames.hasFlashes() || highRatio);
		int twitchingAmplitude = veryHighRatio ? 2 : 1;

		updatePosition(scaledWidth, scaledHeight);
		flashFrames.tick();
		
		for (int i = 0; i < 10; i++, count -= 2)
		{
			drawOneIcon(matrixStack, i, getIdFromCount(count, ratio, p), flash, twitch, twitchingAmplitude,
					ClientData.getIcons(Config.CLIENT.useAlternativeDesign.get()));
		}
		
		if (!blend)
		{
			GL11.glDisable(GL11.GL_BLEND);
		}
	}
	
	public void setJustIncreased()
	{
		flashFrames.setFlashes(3);
	}
	
	public void setJustChanged(short oldV, short newV)
	{
		if (newV > oldV)
		{
			setJustIncreased();
		}
	}
	
	private void drawOneIcon(MatrixStack matrixStack, int num, int id, boolean flash, boolean twitch, int twitchingAmplitude, ResourceLocation[] icons)
	{
		if (icons == null || icons.length <= 0)
		{
			return;
		}
		int _id = Utils.clamp(id + (flash ? 1 : 0), 0, icons.length - 1);
		_mc.getTextureManager().bind(icons[_id]);
		IngameGui.blit(
				matrixStack,
				position.x + 8 * num,
				position.y + (twitch ? (int)Math.round((_random.nextDouble() * (twitchingAmplitude * 2) - twitchingAmplitude)) : 0),
				0, 0,
				9, 9,
				9, 9);
	}
	
	private int getIdFromCount(int count, double ratio, PlayerEntity p)
	{
		if (!Config.CLIENT.useAlternativeDesign.get())
		{
			return count > 0 ? (count == 1 ? 2 : 0) : 4;
		}
		else
		{
			return count > 0 ? (count == 1 ? 4 : 2) + 4 * Utils.clamp((int)Math.floor(ratio / 0.25d), 0, 3) : 0;
		}
	}
	
	private void updatePosition(int scaledWidth, int scaledHeight)
	{
		switch(location)
		{
		case ABOVE_HEALTH_BAR:
			position.x = scaledWidth / 2 - 91;
			position.y = scaledHeight - 49;
			int armor = _mc.player.getArmorValue();
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
			position.x = Config.CLIENT.customPosX.get();
			position.y = Config.CLIENT.customPosY.get();
			break;
		case CUSTOM_SCALED:
			break;
		default:
			break;
		}
		position.x += Config.CLIENT.additionalXValue.get();
		position.y += Config.CLIENT.additionalYValue.get();
	}
	
	private IDisintegration getDisintegration()
	{
		return IDisintegration.getLocal();
	}
}