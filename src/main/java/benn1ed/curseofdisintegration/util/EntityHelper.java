package benn1ed.curseofdisintegration.util;

import net.minecraft.entity.*;
import net.minecraft.entity.player.*;

public class EntityHelper
{
	public static boolean getEntityIsWithinRadiusOfAnother(Entity entity, Entity center, double radius)
	{
		return entity.getPositionVector().subtract(center.getPositionVector()).lengthVector() <= radius;
	}
	
	public static boolean getEntityIsAfterAnother(EntityLiving entity, EntityLivingBase after)
	{
		return entity.getAttackTarget() == after || entity.getRevengeTarget() == after;
	}
	
	public static boolean getPlayerIsAfterAnother(EntityPlayer player, EntityPlayer after)
	{
		return player.getRevengeTarget() == after;
	}
}