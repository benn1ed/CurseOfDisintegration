package benn1ed.curseofdisintegration.client;

import benn1ed.curseofdisintegration.ModData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientData
{
	private static DisintegrationBar dBar;
    private static ResourceLocation[] ICONS = {};
    private static ResourceLocation[] ICONS_FACES = {};
    
	public static void init()
	{
		dBar = new DisintegrationBar();
		String id = ModData.MODID;
		ICONS = new ResourceLocation[]
		{
			new ResourceLocation(id, "textures/di_icon_full.png"),
		    new ResourceLocation(id, "textures/di_icon_full_flash.png"),
		    new ResourceLocation(id, "textures/di_icon_halved.png"),
		    new ResourceLocation(id, "textures/di_icon_halved_flash.png"),
		    new ResourceLocation(id, "textures/di_icon_empty.png"),
		    new ResourceLocation(id, "textures/di_icon_empty_flash.png"),
		};
		ICONS_FACES = new ResourceLocation[]
		{
		    new ResourceLocation(id, "textures/di_faceicon_empty.png"),
		    new ResourceLocation(id, "textures/di_faceicon_empty_flash.png"),
		    new ResourceLocation(id, "textures/di_faceicon_type0_full.png"),
		    new ResourceLocation(id, "textures/di_faceicon_type0_full_flash.png"),
		    new ResourceLocation(id, "textures/di_faceicon_type0_halved.png"),
		    new ResourceLocation(id, "textures/di_faceicon_type0_halved_flash.png"),
		    new ResourceLocation(id, "textures/di_faceicon_type1_full.png"),
		    new ResourceLocation(id, "textures/di_faceicon_type1_full_flash.png"),
		    new ResourceLocation(id, "textures/di_faceicon_type1_halved.png"),
		    new ResourceLocation(id, "textures/di_faceicon_type1_halved_flash.png"),
		    new ResourceLocation(id, "textures/di_faceicon_type2_full.png"),
		    new ResourceLocation(id, "textures/di_faceicon_type2_full_flash.png"),
		    new ResourceLocation(id, "textures/di_faceicon_type2_halved.png"),
		   	new ResourceLocation(id, "textures/di_faceicon_type2_halved_flash.png"),
		   	new ResourceLocation(id, "textures/di_faceicon_type3_full.png"),
		   	new ResourceLocation(id, "textures/di_faceicon_type3_full_flash.png"),
		   	new ResourceLocation(id, "textures/di_faceicon_type3_halved.png"),
		    new ResourceLocation(id, "textures/di_faceicon_type3_halved_flash.png"),
		};
		
	}
	
	public static Minecraft getMinecraft()
	{
		return Minecraft.getInstance();
	}
	
	public static DisintegrationBar getBar()
	{
		return dBar;
	}
    
    public static ResourceLocation[] getIcons(boolean alternative)
    {
    	return !alternative ? ICONS : ICONS_FACES;
    }
}