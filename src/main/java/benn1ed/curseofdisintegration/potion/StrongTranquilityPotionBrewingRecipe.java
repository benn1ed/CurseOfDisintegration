package benn1ed.curseofdisintegration.potion;

import benn1ed.curseofdisintegration.item.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraftforge.common.brewing.*;

public class StrongTranquilityPotionBrewingRecipe extends BrewingRecipe
{
	public StrongTranquilityPotionBrewingRecipe()
	{
		super(
				PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionManager.TRANQUILITY),
				new ItemStack(ItemManager.BOTTLE),
				PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionManager.STRONG_TRANQUILITY));
	}
	
	@Override
	public boolean isInput(ItemStack input)
	{
		return PotionUtils.getPotionFromItem(input) == PotionManager.TRANQUILITY;
	}
	
	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient)
	{
		if (isInput(input) && isIngredient(ingredient))
		{
			return PotionUtils.addPotionToItemStack(new ItemStack(input.getItem()), PotionManager.STRONG_TRANQUILITY);
		}
		return ItemStack.EMPTY;
	}
}