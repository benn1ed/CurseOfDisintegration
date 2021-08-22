package benn1ed.curseofdisintegration.potion;

import benn1ed.curseofdisintegration.item.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraftforge.common.brewing.*;

public class WalkingCorpsePotionBrewingRecipe extends BrewingRecipe
{

	public WalkingCorpsePotionBrewingRecipe()
	{
		super(
				PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD),
				new ItemStack(ItemManager.FLASK),
				PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionManager.WALKING_CORPSE));
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
			return PotionUtils.addPotionToItemStack(new ItemStack(input.getItem()), PotionManager.WALKING_CORPSE);
		}
		return ItemStack.EMPTY;
	}
}