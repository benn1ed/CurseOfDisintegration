package benn1ed.curseofdisintegration.config;

import java.util.ArrayList;
import java.util.List;

import benn1ed.curseofdisintegration.client.DisintegrationBarLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Config
{
	public static final String CATEGORY_GENERAL = "GENERAL CONFIGURATION";
	public static final String CATEGORY_CLIENT = "CLIENT (GUI) CONFIGURATION";
	public static final String CATEGORY_CAPABILITY = "CAPABILITY LOGIC CONFIGURATION";
	public static final String CATEGORY_CAPABILITY_PLAYER = "CAPABILITY LOGIC (PLAYER ENTITIES)";
	
	public static final GeneralSettings GENERAL = new GeneralSettings();
	public static final ClientSettings CLIENT = new ClientSettings();
	public static final CapabilitySettings CAP = new CapabilitySettings();
	public static final CapabilityPlayerSettings CAP_P = new CapabilityPlayerSettings();
	
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec CONFIG;
	
	public static class GeneralSettings
	{
		public ForgeConfigSpec.BooleanValue enableMod;
	}
	
	public static class ClientSettings
	{
		public ForgeConfigSpec.BooleanValue useAlternativeDesign;
		public ForgeConfigSpec.BooleanValue enableFlashing;
		public ForgeConfigSpec.BooleanValue enableTwitching;
		
		public EnumValue<DisintegrationBarLocation> barLocation;
		
		public ForgeConfigSpec.IntValue additionalXValue;
		public ForgeConfigSpec.IntValue additionalYValue;
		public ForgeConfigSpec.IntValue customPosX;
		public ForgeConfigSpec.IntValue customPosY;
	}
	
	public static class CapabilitySettings
	{
		public ForgeConfigSpec.IntValue diFrames;
		public ForgeConfigSpec.IntValue max;
		public ForgeConfigSpec.DoubleValue passiveDist;
		public ForgeConfigSpec.IntValue passiveRate;
		public ForgeConfigSpec.IntValue mitRate;
		public ForgeConfigSpec.IntValue mitCd;
		public ForgeConfigSpec.IntValue creatureHurt;
		public ForgeConfigSpec.IntValue hurt;
		public ForgeConfigSpec.IntValue hit;
		public ForgeConfigSpec.BooleanValue processEveryHit;
		
		public ConfigValue<List<? extends String>> creatureWhitelist;
		public ConfigValue<List<? extends String>> creatureBlacklist;
	}
	
	public static class CapabilityPlayerSettings
	{
		public ForgeConfigSpec.BooleanValue processPlayers;
		public ForgeConfigSpec.DoubleValue passiveDist;
		public ForgeConfigSpec.IntValue passiveRate;
		public ForgeConfigSpec.BooleanValue passiveRecentlyHit;
		public ForgeConfigSpec.IntValue hurt;
		public ForgeConfigSpec.IntValue hit;
	}
	
	static
	{
		CONFIG = setup();
	}
	
	private static ForgeConfigSpec setup()
	{
		BUILDER.comment("General configuration").push(CATEGORY_GENERAL);
		GENERAL.enableMod = BUILDER.comment("Enable mod").define("enableMod", true);
		BUILDER.pop();
		
		
		BUILDER.comment("Client (GUI) configuration").push(CATEGORY_CLIENT);
		CLIENT.useAlternativeDesign = BUILDER
				.comment("The bar uses alternative design with face icons instead of skull ones")
				.define("useAlternativeDesign", false);
		CLIENT.enableFlashing = BUILDER
				.comment("Enable bar flashing upon getting disintegration")
				.define("enableFlashing", true);
		CLIENT.enableTwitching = BUILDER
				.comment("Enable bar twitching")
				.define("enableTwitching", true);
		
		CLIENT.barLocation = BUILDER
				.comment("Where on the GUI the bar will be drawn")
				.defineEnum("barLocation", DisintegrationBarLocation.BOTTOM_RIGHT);
		
		CLIENT.additionalXValue = BUILDER
				.comment("The bar's resulting X position will also be added this value")
				.defineInRange("additionalXValue", 0, 0, Integer.MAX_VALUE);
		CLIENT.additionalYValue = BUILDER
				.comment("The bar's resulting Y position will also be added this value")
				.defineInRange("additionalYValue", 0, 0, Integer.MAX_VALUE);
		CLIENT.customPosX = BUILDER
				.comment("The bar's custom X position for barLocation set to CUSTOM")
				.defineInRange("customPosX", 0, 0, Integer.MAX_VALUE);
		CLIENT.customPosY = BUILDER
				.comment("The bar's custom Y position for barLocation set to CUSTOM")
				.defineInRange("customPosY", 0, 0, Integer.MAX_VALUE);
		BUILDER.pop();
		
		
		BUILDER.comment("Capability logic configuration").push(CATEGORY_CAPABILITY);
		CAP.diFrames = BUILDER
				.comment("The amount of ticks that needs to pass before a player can gain passive disintegration again.")
				.defineInRange("diFrames", 25, 0, Integer.MAX_VALUE);
		CAP.max = BUILDER
				.comment("Maximum amount of disintegration a player can have. Upon reaching this value the player is killed")
				.defineInRange("max", 2500, 0, Short.MAX_VALUE);
		CAP.passiveDist = BUILDER
				.comment("If this is more than 0, creatures after a player within this radius will passively increase this player's disintegration level")
				.defineInRange("passiveDist", 7, 0, Double.MAX_VALUE);
		CAP.passiveRate = BUILDER
				.comment("The rate at which the passive disintegration occurs, considering passiveDisintegrationDistance is more than 0")
				.defineInRange("passiveRate", 5, Short.MIN_VALUE, Short.MAX_VALUE);
		CAP.mitRate = BUILDER
				.comment("The rate at which a player's disintegration level is decreased when there are no provoking factors")
				.defineInRange("mitRate", 300, Short.MIN_VALUE, Short.MAX_VALUE);
		CAP.mitCd = BUILDER
				.comment("The amount of ticks that needs to pass since the last time a player's disintegration level increased, before it can start decreasing at the rate of mitRate")
				.defineInRange("mitCd", 500, 0, Integer.MAX_VALUE);
		CAP.creatureHurt = BUILDER
				.comment("The amount of disintegration gained upon being hit by creature")
				.defineInRange("creatureHurt", 160, Short.MIN_VALUE, Short.MAX_VALUE);
		CAP.hurt = BUILDER
				.comment("The amount of disintegration gained upon being hit by other sources")
				.defineInRange("hurt", 60, Short.MIN_VALUE, Short.MAX_VALUE);
		CAP.hit = BUILDER
				.comment("The amount of disintegration gained upon hitting another creature")
				.defineInRange("hit", 30, Short.MIN_VALUE, Short.MAX_VALUE);
		
		List<String> l1 = new ArrayList<String>();
		l1.add("creatureWhitelist");
		CAP.creatureWhitelist = BUILDER
				.comment(
						"If not empty, only the creatures from this list will be counted as enemies",
						"Must contain ResourceLocation strings (<namespace>:<path>), regex compatible (java.util.regex)")
				.<String>defineListAllowEmpty(l1, ArrayList::new, obj -> obj instanceof String);
		List<String> l2 = new ArrayList<String>();
		l2.add("creatureBlacklist");
		CAP.creatureBlacklist = BUILDER
				.comment(
						"Creatures from this list won't be counted as enemies",
						"Must contain ResourceLocation strings (<namespace>:<path>), regex compatible (java.util.regex)")
				.<String>defineListAllowEmpty(l2, ArrayList::new, obj -> obj instanceof String);
		BUILDER.pop();
		
		
		BUILDER.comment("Capability logic configuration for player entities").push(CATEGORY_CAPABILITY_PLAYER);
		CAP_P.processPlayers = BUILDER
				.comment("If true, players will also be processed and counted as enemies")
				.define("processPlayers", true);
		CAP_P.passiveDist = BUILDER
				.comment("If this is more than 0, any other players within this radius will passively increase a player's disintegration level")
				.defineInRange("passiveDist", 10, 0, Double.MAX_VALUE);
		CAP_P.passiveRate = BUILDER
				.comment("The rate at which the passive disintegration occurs, considering passiveDisintegrationDistance is more than 0")
				.defineInRange("passiveRate", 6, Short.MIN_VALUE, Short.MAX_VALUE);
		CAP_P.passiveRecentlyHit = BUILDER
				.comment("If true, passive disintegration will only take place for some time after being hit, otherwise a player will gain disintegration anytime there's another player nearby")
				.define("passiveRecentlyHit", true);
		CAP_P.hurt = BUILDER
				.comment("The amount of disintegration gained upon being hit by other player")
				.defineInRange("hurt", 200, Short.MIN_VALUE, Short.MAX_VALUE);
		CAP_P.hit = BUILDER
				.comment("The amount of disintegration gained upon hitting another player")
				.defineInRange("hit", 50, Short.MIN_VALUE, Short.MAX_VALUE);
		BUILDER.pop();
		
		return BUILDER.build();
	}
}