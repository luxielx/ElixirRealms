package SolarFavor.ElixirRealms;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HubMenu {
	Main plugin;

	public HubMenu(Main plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("unchecked")
	public static void openMenu(Player player) {

		Inventory inv = null;
		inv = Bukkit.createInventory(null, Main.getCfg().getInt("invLarge"),
				Main.colorCode(Main.getCfg().getString("invName")));

		for (String key : Main.getCfg().getConfigurationSection("InvSlot").getKeys(false)) {

			int Slot = Integer.parseInt(key);
			Material material = Material.matchMaterial(Main.getCfg().getString("InvSlot." + key + ".Material"));
			String server = convertToInvisibleString(Main.getCfg().getString("InvSlot." + key + ".server"));
			String name = Main.colorCode(Main.getCfg().getString("InvSlot." + key + ".Name"));

			boolean Glow = Main.getCfg().getBoolean("InvSlot." + key + ".Glow");

			ArrayList<String> lore = (ArrayList<String>) Main.getCfg().getList("InvSlot." + key + ".Lore");
			ArrayList<String> ilore = new ArrayList<>();
			for (String lz : lore) {

				String zz = Main.colorCode(lz);

				ilore.add(zz);

			}
			ilore.add(server);
			inv.setItem(Slot, createItemStack(material, name, ilore, Glow));

		}

		player.openInventory(inv);

	}

	public static ItemStack createItemStack(Material material, String name, ArrayList<String> lore, boolean glow) {
		ItemStack is = new ItemStack(material);

		ItemMeta im = is.getItemMeta();
		if (glow = true) {
			Glow glowenchant = new Glow(100);
			im.addEnchant(glowenchant, 1, false);
		}

		im.setDisplayName(name);

		im.setLore(lore);

		is.setItemMeta(im);

		return is;
	}

	public static String convertToInvisibleString(String s) {
		String hidden = "";
		for (char c : s.toCharArray())
			hidden += ChatColor.COLOR_CHAR + "" + c;
		return hidden;
	}
}
