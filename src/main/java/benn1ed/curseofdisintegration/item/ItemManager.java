package benn1ed.curseofdisintegration.item;

import benn1ed.curseofdisintegration.ModData;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.creativetab.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.*;
import net.minecraft.world.storage.loot.functions.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.model.*;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.registries.*;

public class ItemManager
{
	public static final Item POWDER;
	public static final Item BOTTLE;
	public static final Item FLASK;
	
	static
	{
		String powderName = "slightly_shiny_powder";
		String bottleName = "slightly_shiny_bottle";
		String flaskName = "shimmering_flask";
		POWDER = new Item().setUnlocalizedName(powderName).setRegistryName(new ResourceLocation(ModData.MODID, powderName)).setCreativeTab(CreativeTabs.BREWING);
		BOTTLE = new Item().setUnlocalizedName(bottleName).setRegistryName(new ResourceLocation(ModData.MODID, bottleName)).setCreativeTab(CreativeTabs.BREWING);
		FLASK = new ItemSimpleFoiled().setUnlocalizedName(flaskName).setRegistryName(new ResourceLocation(ModData.MODID, flaskName)).setCreativeTab(CreativeTabs.BREWING);
	}
	
	public static void registerItems(IForgeRegistry<Item> registry)
	{
		registry.register(POWDER);
		registry.register(BOTTLE);
		registry.register(FLASK);
	}
	
	public static void registerModels(ModelRegistryEvent event)
	{
		ModelLoader.setCustomModelResourceLocation(POWDER, 0, new ModelResourceLocation(POWDER.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(BOTTLE, 0, new ModelResourceLocation(BOTTLE.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(FLASK, 0, new ModelResourceLocation(FLASK.getRegistryName(), "inventory"));
	}
	
	public static void modifyLootTables(LootTableLoadEvent event)
	{
		if (checkTable(event.getName().toString()))
		{
			LootEntry powderEntry = new LootEntryItem(POWDER, 60, 0, new LootFunction[0], new LootCondition[0], "curseofdisintegration:slightly_shiny_powder");
			LootEntry bottleEntry = new LootEntryItem(BOTTLE, 25, 0, new LootFunction[0], new LootCondition[0], "curseofdisintegration:slightly_shiny_bottle");
			LootEntry flaskEntry = new LootEntryItem(FLASK, 8, 1, new LootFunction[0], new LootCondition[0], "curseofdisintegration:shimmering_flask");
			event.getTable().addPool(new LootPool(new LootEntry[] { powderEntry, bottleEntry, flaskEntry }, new LootCondition[0], new RandomValueRange(2), new RandomValueRange(0, 2), "curseofdisintegration:inject_pool"));
		}
	}
	
	public static void addItemTooltips(ItemTooltipEvent event)
	{
		String itemName = event.getItemStack().getItem().getRegistryName().toString();
		String tooltipKey = null;
		
		if (itemName.equals("curseofdisintegration:slightly_shiny_powder"))
		{
			tooltipKey = "item.slightly_shiny_powder.tooltip";
		}
		else if (itemName.equals("curseofdisintegration:slightly_shiny_bottle"))
		{
			tooltipKey = "item.slightly_shiny_bottle.tooltip";
		}
		else if (itemName.equals("curseofdisintegration:shimmering_flask"))
		{
			tooltipKey = "item.shimmering_flask.tooltip";
		}
		
		if (tooltipKey != null)
		{
			event.getToolTip().add(new TextComponentTranslation(tooltipKey).getFormattedText());
		}
	}
	
	private static boolean checkTable(String name)
	{
		return
				name.equals("minecraft:chests/simple_dungeon") ||
				name.equals("minecraft:chests/desert_pyramid") ||
				name.equals("minecraft:chests/abandoned_mineshaft") ||
				name.equals("minecraft:chests/jungle_temple") ||
				name.equals("minecraft:chests/igloo_chest");
	}
}