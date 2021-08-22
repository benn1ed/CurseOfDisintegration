package benn1ed.curseofdisintegration.potion;

import benn1ed.curseofdisintegration.*;
import benn1ed.curseofdisintegration.capability.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.*;

public class TranquilityPotion extends Potion
{
	public TranquilityPotion()
	{
		super(false, 16180339);
		setPotionName("effect.tranquility");
		setRegistryName(new ResourceLocation(ModData.MODID, "tranquility"));
	}

	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier)
	{
		if (entityLivingBaseIn instanceof EntityPlayer)
		{
			EntityPlayer p = (EntityPlayer)entityLivingBaseIn;
			IDisintegration cap = IDisintegration.getFor(p);
			if (cap != null)
			{
				if (amplifier == 1)
				{
					cap.setMultiplier(0.65f);
				}
				else
				{
					cap.setMultiplier(0.8f);
				}
			}
		}
	}
	
	@Override
	public boolean isReady(int duration, int amplifier)
	{
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc)
    {
		mc.getTextureManager().bindTexture(ModData.TRANQUILITY_ICON);
		GuiIngame.drawModalRectWithCustomSizedTexture(x + 7, y + 7, 0f, 0f, 16, 16, 16f, 16f);
    }

	@SideOnly(Side.CLIENT)
	@Override
	public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha)
	{
		mc.getTextureManager().bindTexture(ModData.TRANQUILITY_ICON);
		GuiIngame.drawModalRectWithCustomSizedTexture(x + 4, y + 4, 0f, 0f, 16, 16, 16f, 16f);
	}
}