package benn1ed.curseofdisintegration.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;

public class EntityHelper
{
	public static boolean getEntityIsWithinRadiusOfAnother(Entity entity, Entity center, double radius)
	{
		return entity.position().subtract(center.position()).length() <= radius;
	}
	
	public static boolean getEntityIsAfterAnother(LivingEntity entity, LivingEntity after)
	{
		return entity.getLastHurtByMob() == after || after.getLastHurtByMob() == entity
				|| (entity instanceof MobEntity && ((MobEntity)entity).getTarget() == after);
	}
}