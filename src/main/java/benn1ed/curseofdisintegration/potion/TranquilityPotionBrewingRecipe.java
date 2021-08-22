package benn1ed.curseofdisintegration.potion;

import benn1ed.curseofdisintegration.item.*;
import net.minecraftforge.common.brewing.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;

public class TranquilityPotionBrewingRecipe extends BrewingRecipe
{
	public TranquilityPotionBrewingRecipe()
	{
		super(
				PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD),
				new ItemStack(ItemManager.POWDER),
				PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionManager.TRANQUILITY));
	}

	@Override
	public boolean isInput(ItemStack input)
	{
		return PotionUtils.getPotionFromItem(input) == PotionTypes.AWKWARD;
	}

	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient)
	{
		if (isInput(input) && isIngredient(ingredient))
		{
			return PotionUtils.addPotionToItemStack(new ItemStack(input.getItem()), PotionManager.TRANQUILITY);
		}
		return ItemStack.EMPTY;
	}
}