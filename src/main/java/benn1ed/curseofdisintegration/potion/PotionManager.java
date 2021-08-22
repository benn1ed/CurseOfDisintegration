package benn1ed.curseofdisintegration.potion;

import benn1ed.curseofdisintegration.ModData;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraftforge.common.brewing.*;
import net.minecraftforge.registries.*;

public class PotionManager
{
	public static final TranquilityPotion POTION;
	public static final WalkingCorpsePotion CORPSE_POTION;
	public static final PotionType TRANQUILITY;
	public static final PotionType LONG_TRANQUILITY;
	public static final PotionType STRONG_TRANQUILITY;
	public static final PotionType WALKING_CORPSE;
	
	static
	{
		POTION = new TranquilityPotion();
		CORPSE_POTION = new WalkingCorpsePotion();
		TRANQUILITY = new PotionType("tranquility", new PotionEffect[] { new PotionEffect(POTION, 3600) }).setRegistryName(new ResourceLocation(ModData.MODID, "tranquility"));
		LONG_TRANQUILITY = new PotionType("tranquility", new PotionEffect[] { new PotionEffect(POTION, 6000) }).setRegistryName(new ResourceLocation(ModData.MODID, "long_tranquility"));
		STRONG_TRANQUILITY = new PotionType("tranquility", new PotionEffect[] { new PotionEffect(POTION, 3600, 1) }).setRegistryName(new ResourceLocation(ModData.MODID, "strong_tranquility"));
		WALKING_CORPSE = new PotionType("walking_corpse", new PotionEffect[] { new PotionEffect(CORPSE_POTION, 1200) }).setRegistryName(new ResourceLocation(ModData.MODID, "walking_corpse"));
	}
	
	public static void registerPotion(IForgeRegistry<Potion> registry)
	{
		registry.register(POTION);
		registry.register(CORPSE_POTION);
	}
	
	public static void registerPotionType(IForgeRegistry<PotionType> registry)
	{
		registry.register(TRANQUILITY);
		registry.register(LONG_TRANQUILITY);
		registry.register(STRONG_TRANQUILITY);
		registry.register(WALKING_CORPSE);
	}
	
	public static void registerRecipes()
	{
    	BrewingRecipeRegistry.addRecipe(new TranquilityPotionBrewingRecipe());
    	BrewingRecipeRegistry.addRecipe(new LongTranquilityPotionBrewingRecipe());
    	BrewingRecipeRegistry.addRecipe(new StrongTranquilityPotionBrewingRecipe());
    	BrewingRecipeRegistry.addRecipe(new WalkingCorpsePotionBrewingRecipe());
	}
}