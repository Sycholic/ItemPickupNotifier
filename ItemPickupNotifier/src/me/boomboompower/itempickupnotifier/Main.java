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
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;

import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.PacketPlayOutChat;
import net.minecraft.server.v1_9_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_9_R1.LocaleI18n;

public class Main extends JavaPlugin implements Listener {
	
	@Override
	public void onEnable() {
		broadcast("");
		broadcast("&aItemPickupNotifier &3has been enabled!");
		broadcast("&fCheers to &7@dannydog&f for the help!");
		broadcast("");
		saveDefaultConfig();
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	private void onPickup(final PlayerPickupItemEvent e) {
		final ItemMeta item = e.getItem().getItemStack().getItemMeta();
		String name = CraftItemStack.asNMSCopy(e.getItem().getItemStack()).getItem().j(CraftItemStack.asNMSCopy(e.getItem().getItemStack()));
		String fullname = name + ".name";
		String finalname = LocaleI18n.get(fullname);
		
		if (item.hasDisplayName()) finalname = item.getDisplayName();
		
		if (getConfig().getBoolean("SendMessage")) {
			actionBar(e.getPlayer(), getConfig().getString("Message"), finalname);
		}
	}
	
	private void broadcast(String message) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
	
	private void actionBar(final Player p, String message, final String replaceWith) {
		message = ChatColor.translateAlternateColorCodes('&', message).replace("{ITEM}", replaceWith);
		IChatBaseComponent chatBase = ChatSerializer.a("{\"text\": \"" + message + "\"}");
		
		PacketPlayOutChat playOutChat = new PacketPlayOutChat(chatBase, (byte) 2);
		
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(playOutChat);
	}
}
