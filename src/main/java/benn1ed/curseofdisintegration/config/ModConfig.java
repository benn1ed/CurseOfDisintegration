package benn1ed.curseofdisintegration.config;

import benn1ed.curseofdisintegration.ModData;
import benn1ed.curseofdisintegration.client.DisintegrationBarLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresMcRestart;

@Config(modid = ModData.MODID, name = ModData.MODID, category = "")
public class ModConfig
{
	@Name("General configuration")
	@Comment("General configuration")
	public static GeneralSettings general = new GeneralSettings();
	
	@Name("GUI (bar) configuration")
	@Comment("Client configuration")
	public static ClientSettings client = new ClientSettings();
	
	@Name("Logic congifuration")
	@Comment("Disintegration logic configuration")
	public static CapabilitySettings capability = new CapabilitySettings();
	
	@Name("Logic configuration for players entities")
	@Comment("Disintegration logic configuration for player entities")
	public static CapabilitySettingsForPlayerEntities capabilityPlayers = new CapabilitySettingsForPlayerEntities();
	
	public static class GeneralSettings
	{
		@RequiresMcRestart
		public boolean enableMod = true;
	}
	
	public static class ClientSettings
	{
		@Comment("Where on the GUI the bar will be drawn.")
		public DisintegrationBarLocation barLocation = DisintegrationBarLocation.BOTTOM_RIGHT;
		
		@Comment("The bar's custom X position for barLocation set to CUSTOM")
		public int customPosX = 0;
		
		@Comment("The bar's custom Y position for barLocation set to CUSTOM")
		public int customPosY = 0;
		
		@Comment("If barLocation is set to CUSTOM_SCALED, the bar's X position will be calculated by subtracting this value from the GUI's scaled width.")
		public int scaledPosX = 0;
		
		@Comment("If barLocation is set to CUSTOM_SCALED, the bar's Y position will be calculated by subtracting this value from the GUI's scaled height.")
		public int scaledPosY = 0;
		
		@Comment("The bar's resulting X position will also be added this value.")
		public int additionalXValue = 0;
		
		@Comment("The bar's resulting Y position will also be added this value.")
		public int additionalYValue = 0;
		
		@Comment("Enable bar flashing upon getting disintegration.")
		public boolean enableFlashing = true;
		
		@Comment("Enable bar twitching")
		public boolean enableTwitching = true;
		
		@Comment("The bar uses alternative design with face icons instead of skull ones.")
		public boolean useAlternativeDesign = false;
	}
	
	public static class CapabilitySettings
	{
		@Comment("The amount of ticks a player's disintegration level won't be able to change again.")
		@RangeInt(min = 0, max = Integer.MAX_VALUE)
		public int disintegrationFrames = 25;
		
		@Comment("Maximum amount of disintegration a player can have. Upon reaching this value the player is killed.")
		@RangeInt(min = 0, max = Short.MAX_VALUE)
		@RequiresMcRestart
		public int maxDisintegration = 2500;
		
		@Comment("If this is more than 0, creatures after a player within this radius will passively increase this player's disintegration level.")
		@RangeDouble(min = 0, max = Double.MAX_VALUE)
		public double passiveDisintegrationDistance = 7;
		
		@Comment("The rate at which the passive disintegration occurs, considering passiveDisintegrationDistance is more than 0.")
		@RangeInt(min = Short.MIN_VALUE, max = Short.MAX_VALUE)
		public int passiveDisintegrationRate = 5;
		
		@Comment("The rate at which a player's disintegration level is decreased when there are no provoking factors.")
		@RangeInt(min = Short.MIN_VALUE, max = Short.MAX_VALUE)
		public int disintegrationMitigationRate = 300;
		
		@Comment("The amount of ticks that needs to pass since the last time a player's disintegration level increased, before it can start decreasing at the rate of disintegrationMitigationRate.")
		@RangeInt(min = 0, max = Integer.MAX_VALUE)
		public int disintegrationMitigationCooldown = 500;
		
		@Comment("The amount of disintegration gained upon being hit by creature.")
		@RangeInt(min = Short.MIN_VALUE, max = Short.MAX_VALUE)
		public int onCreatureHurtValue = 160;
		
		@Comment("The amount of disintegration gained upon being hit by other sources.")
		@RangeInt(min = Short.MIN_VALUE, max = Short.MAX_VALUE)
		public int onHurtValue = 60;
		
		@Comment("The amount of disintegration gained upon hitting another creature.")
		@RangeInt(min = Short.MIN_VALUE, max = Short.MAX_VALUE)
		public int onHitValue = 30;
		
		@Comment({
			"If not empty, only the creatures from this list will be counted as enemies.",
			"Must contain ResourceLocation strings (<namespace>:<path>), regex compatible (java.util.regex)."
		})
		public String[] creatureWhitelist = {};
		
		@Comment({
			"Creatures from this list won't be counted as enemies.",
			"Must contain ResourceLocation strings (<namespace>:<path>), regex compatible (java.util.regex)."
		})
		public String[] creatureBlacklist = {};
	}
	
	public static class CapabilitySettingsForPlayerEntities
	{
		@Comment("If true, players will also be processed and counted as enemies.")
		public boolean processPlayers = true;
		
		@Comment("If this is more than 0, any other players within this radius will passively increase a player's disintegration level.")
		@RangeDouble(min = 0, max = Double.MAX_VALUE)
		public double passiveDisintegrationDistance = 10;
		
		@Comment("The rate at which the passive disintegration occurs, considering passiveDisintegrationDistance is more than 0.")
		@RangeInt(min = 0, max = Integer.MAX_VALUE)
		public int passiveDisintegrationRate = 6;
		
		@Comment("If true, passive disintegration will only take place for some time after being hit, otherwise a player will gain disintegration anytime there's another player nearby.")
		public boolean passiveRecentlyHit = true;

		@Comment("The amount of disintegration gained upon being hit by other player.")
		@RangeInt(min = 0, max = Short.MAX_VALUE)
		public int onHurtValue = 200;
		
		@Comment("The amount of disintegration gained upon hitting another player.")
		@RangeInt(min = 0, max = Short.MAX_VALUE)
		public int onHitValue = 50;
	}
}