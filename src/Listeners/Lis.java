package Listeners;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import SolarFavor.ElixirRealms.HubMenu;
import SolarFavor.ElixirRealms.Main;
import net.md_5.bungee.api.ChatColor;

public class Lis implements Listener {
	Main plugin;
	HubMenu menu = new HubMenu(this.plugin);

	public Lis(Main main) {
		this.plugin = main;
	}
	@EventHandler
	public void enclick(PlayerInteractEntityEvent e) {
		if ((e.getRightClicked() instanceof Player)) {
			Player p = e.getPlayer();
			try {

				if (e.getRightClicked().getCustomName().equals("§a§lFaction")) {

					Main.connect(p, "faction");

				}

				if (e.getRightClicked().getCustomName().equals("§b§lSkyblock")) {

					Main.connect(p, "skyblock");
				}
			} catch (Exception localException) {
				p.kickPlayer(ChatColor.RED + "[" + ChatColor.YELLOW + "MineGG" + ChatColor.RED + "]" + ChatColor.GREEN
						+ " Server bạn chọn đã đầy");

			}
			
		}
	}

	@EventHandler
	public void interact(PlayerInteractEvent e) {

		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR)
				return;
			if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta() == null)
				return;
			if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName() == null)
				return;
			if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()
					.equalsIgnoreCase("§a§lSelect Server")) {
				HubMenu.openMenu(e.getPlayer());

			}

		}

	}

	@EventHandler
	public void ClickEvent(InventoryClickEvent e) {
		if (e.getClickedInventory() == null)
			return;

		if (e.getClickedInventory().equals(e.getWhoClicked().getInventory()))
			return;

		if (e.getCurrentItem().getItemMeta() == null)
			return;
		if (e.getCurrentItem().getType() == Material.AIR)
			return;
		if (e.getInventory().getTitle().equals(Main.colorCode(Main.getCfg().getString("invName")))) {
			
			e.setCancelled(true);
			ItemStack is = e.getCurrentItem();
			
			if (is.getItemMeta().getLore() != null) {
				e.getWhoClicked().closeInventory();
				ArrayList<String> lore = (ArrayList<String>) is.getItemMeta().getLore();
				String server = lore.get(lore.size() - 1).replaceAll("§", "");

				try {
					Main.connect((Player) e.getWhoClicked(), server);
				} catch (Exception ex) {
					((Player) e.getWhoClicked()).kickPlayer(ChatColor.RED + "[" + ChatColor.YELLOW + "MineGG"
							+ ChatColor.RED + "]" + ChatColor.GREEN + " Server bạn chọn đã đầy");
				}
				

			} else {
				return;
			}

		}

	}

}
