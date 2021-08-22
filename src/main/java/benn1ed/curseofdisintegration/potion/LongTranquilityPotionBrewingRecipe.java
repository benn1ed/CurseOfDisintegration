package benn1ed.curseofdisintegration.potion;

import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraftforge.common.brewing.*;

public class LongTranquilityPotionBrewingRecipe extends BrewingRecipe
{
	public LongTranquilityPotionBrewingRecipe()
	{
		super(
				PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionManager.TRANQUILITY),
				new ItemStack(Items.REDSTONE),
				PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionManager.LONG_TRANQUILITY));
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
			return PotionUtils.addPotionToItemStack(new ItemStack(input.getItem()), PotionManager.LONG_TRANQUILITY);
		}
		return ItemStack.EMPTY;
	}
}