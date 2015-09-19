package ru.Plasticable.ShowRegion;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{

	static String notregion;
	static String region;
	static boolean hideDefaultRegion;
	static List<String> blacklist;
	static FileConfiguration c;

	public void onLoad()
	{
		saveDefaultConfig();
		c = getConfig();
		blacklist = c.getStringList("blacklist");
		notregion = c.getString("notregion").replace('&', '§');
		region = c.getString("region").replace('&', '§');
		hideDefaultRegion = c.getBoolean("hideDefaultRegion");
	}

	public void onEnable()
	{

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			@Override
			public void run()
			{
				for (Player p : Bukkit.getOnlinePlayers())
					Utils.sendAction(p, Utils.getRegionName(p.getLocation()));
			}
		}, 20, 20);

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args)
	{

		if (args.length < 1) return false;

		if (args[0].equalsIgnoreCase("reload"))
		{
			c = getConfig();
			sender.sendMessage("§6Конфиг перезагружен.");
			return true;
		}

		if (args.length < 2) return false;

		if (args[0].equalsIgnoreCase("add"))
		{
			blacklist.add(args[1]);
			sender.sendMessage("§6Регион добавлен.");
			c.set("blacklist", blacklist);
			saveConfig();
			onLoad();
			
			return true;
		}

		if (args[0].equalsIgnoreCase("remove"))
		{
			blacklist.remove(args[1]);
			sender.sendMessage("§6Регион удалён.");
			c.set("blacklist", blacklist);
			saveConfig();
			onLoad();
			
			return true;
		}

		return false;
	}
}
