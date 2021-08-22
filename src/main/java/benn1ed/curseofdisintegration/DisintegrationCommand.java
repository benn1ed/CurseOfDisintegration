package benn1ed.curseofdisintegration;

import benn1ed.curseofdisintegration.capability.IDisintegration;
import net.minecraft.command.*;
import net.minecraft.entity.player.*;
import net.minecraft.server.*;

public class DisintegrationCommand extends CommandBase
{
	@Override
	public String getName()
	{
		return "codi";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "command.codi.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args == null || args.length < 3)
		{
			return;
		}
		
		String playerName = args[1];
		EntityPlayerMP p = server.getPlayerList().getPlayerByUsername(playerName);
		if (p == null)
		{
			return;
		}

		String value = args[2];
		short valueShort = 0;
		try
		{
			valueShort = Short.parseShort(value);
		}
		catch (Exception e)
		{
			return;
		}

		String action = args[0];
		if (action.equals("set") || action.equals("add"))
		{
			IDisintegration cap = IDisintegration.getFor(p);
			if (cap == null)
			{
				return;
			}
			if (action.equals("set"))
			{
				cap.setAndSync(valueShort, p);
				cap.resetMitigationCooldown();
			}
			else if (action.equals("add"))
			{
				cap.incrementAndSync(valueShort, p);
				cap.resetMitigationCooldown();
			}
		}
	}
}