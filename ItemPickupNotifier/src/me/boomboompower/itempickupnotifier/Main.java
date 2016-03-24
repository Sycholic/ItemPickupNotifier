package me.boomboompower.itempickupnotifier;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;

import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.PacketPlayOutChat;
import net.minecraft.server.v1_9_R1.IChatBaseComponent.ChatSerializer;

public class Main extends JavaPlugin implements Listener {
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	private void onPickup(PlayerPickupItemEvent e) {
		ItemMeta item = e.getItem().getItemStack().getItemMeta();
		String name = getConfig().getString("DefaultName");
		
		if (item.hasDisplayName()) name = item.getDisplayName();
		
		if (getConfig().getBoolean("SendMessage")) {
			actionBar(e.getPlayer(), getConfig().getString("Message"), name);
		}
	}
	
	private void actionBar(Player p, String message, String replaceWith) {
		
		
		message = ChatColor.translateAlternateColorCodes('&', message).replace("{ITEM}", replaceWith);
		IChatBaseComponent chatBase = ChatSerializer.a("{\"text\": \"" + message + "\"}");
		
		PacketPlayOutChat playOutChat = new PacketPlayOutChat(chatBase, (byte) 2);
		
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(playOutChat);
	}
}
