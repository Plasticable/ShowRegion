package ru.Plasticable.ShowRegion;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Utils
{

	public static String getRegionName(Location l)
	{
		WorldGuardPlugin wgp = WorldGuardPlugin.inst();
		RegionManager regionManager = wgp.getRegionManager(l.getWorld());
		ApplicableRegionSet set = regionManager.getApplicableRegions(l);
		List<String> names = new ArrayList<String>();

		for (ProtectedRegion pr : set.getRegions())
		{  
			String name = ChatColor.translateAlternateColorCodes('&', Main.c.getString("rgnames." + pr.getId(), pr.getId()));
			names.add(name);
		}
		
		return names.isEmpty() ? Main.notregion : StringUtils.join(names, ", ");
	}

	public static void sendAction(Player p, String region)
	{
		if (Main.blacklist.contains(region)) { return; }

		if (region.equals(Main.notregion) && Main.hideDefaultRegion) { return; }

		PacketContainer chat = new PacketContainer(PacketType.Play.Server.CHAT);
		chat.getChatComponents().write(
				0,
				WrappedChatComponent.fromJson("{\"text\": \""
						+ Main.region.replace("{REGION}", region) + "\"}"));
		chat.getBytes().write(0, (byte) 2);
		try
		{
			ProtocolLibrary.getProtocolManager().sendServerPacket(p, chat);
		}
		catch (InvocationTargetException e)
		{
			throw new IllegalStateException("Unable to send packet " + chat, e);
		}
	}
}
