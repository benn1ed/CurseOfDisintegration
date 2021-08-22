package benn1ed.curseofdisintegration.potion;

import java.lang.reflect.*;
import benn1ed.curseofdisintegration.*;
import benn1ed.curseofdisintegration.capability.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.*;

public class WalkingCorpsePotion extends Potion
{
	public WalkingCorpsePotion()
	{
		super(false, 589813);
		setPotionName("effect.walking_corpse");
		setRegistryName(new ResourceLocation(ModData.MODID, "walking_corpse"));
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
				cap.setDisintegrateOnMax(false);
				
				PotionEffect slowness = p.getActivePotionEffect(MobEffects.SLOWNESS);
				PotionEffect weakness = p.getActivePotionEffect(MobEffects.WEAKNESS);
				
				if (slowness == null)
				{
					slowness = new PotionEffect(MobEffects.SLOWNESS, 100, 2);
					p.addPotionEffect(slowness);
				}
				if (weakness == null)
				{
					weakness = new PotionEffect(MobEffects.WEAKNESS, 100, 2);
					p.addPotionEffect(weakness);
				}
				
				try
				{
					Field duration = PotionEffect.class.getDeclaredField("duration");
					duration.setAccessible(true);
					duration.set(slowness, 100);
					duration.set(weakness, 100);
				}
				catch (Exception e) {}
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
		mc.getTextureManager().bindTexture(ModData.WALKING_CORPSE_ICON);
		GuiIngame.drawModalRectWithCustomSizedTexture(x + 8, y + 7, 0f, 0f, 16, 16, 16f, 16f);
    }

	@SideOnly(Side.CLIENT)
	@Override
	public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha)
	{
		mc.getTextureManager().bindTexture(ModData.WALKING_CORPSE_ICON);
		GuiIngame.drawModalRectWithCustomSizedTexture(x + 5, y + 4, 0f, 0f, 16, 16, 16f, 16f);
	}
}