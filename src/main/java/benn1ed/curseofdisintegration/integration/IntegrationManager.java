package benn1ed.curseofdisintegration.integration;

import java.util.*;
import benn1ed.curseofdisintegration.capability.*;
import net.minecraft.entity.player.*;

public class IntegrationManager
{
	private final List<IIntegration> _integrations = new ArrayList<IIntegration>();
	
	public void addIntegration(IIntegration integration)
	{
		if (integration != null)
		{
			_integrations.add(integration);
		}
	}
	
	public boolean removeIntegration(IIntegration integration)
	{
		return _integrations.remove(integration);
	}
	
	public boolean diFramesDone(EntityPlayer player, IDisintegration diCap)
	{
		for (IIntegration i : _integrations)
		{
			if (i.diFramesDone(player, diCap))
			{
				return true;
			}
		}
		return false;
	}
}