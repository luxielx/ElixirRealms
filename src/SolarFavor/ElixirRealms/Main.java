package SolarFavor.ElixirRealms;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import Listeners.Lis;

public class Main extends JavaPlugin implements PluginMessageListener {
	static FileConfiguration config;
	static Plugin main;
 	static int factioncount ;
 	static int skyblockcount ;
 	static	int lobbycount ;
	@Override
	public void onEnable() {
		main = this;
		saveDefaultConfig();
		config = getConfig();
		this.getServer().getPluginManager().registerEvents(new Lis(this), this);

		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

	}

	public static int getFactionCount(){
		return factioncount;
	}

	public static int getSkyblockCount(){
		return skyblockcount;
	}

	public static int getLobbyCount(){
		return lobbycount;
	}
	public boolean onCommand(org.bukkit.command.CommandSender sender, Command cmd, String a, String[] args) {

		if ((a.equalsIgnoreCase("sv")) || (a.equalsIgnoreCase("hub"))) {
			Player p = (Player) sender;

			if (args.length == 0) {
				HubMenu.openMenu(p);
			}
			if ((args.length == 1) && (args[0].equals("lobby")) && (p.isOp())) {
				for (Player pi : Bukkit.getOnlinePlayers()) {
					Main.connect(pi, "lobby");
				}
			}

			if ((args.length == 2) && (args[0].equals("send"))) {
				Main.connect(p, args[1]);
			}
			return true;
		} else if (cmd.getName().equalsIgnoreCase("exreload") && sender.hasPermission("solarcutevcl")) {
			reloadCfg();

			sender.sendMessage(ChatColor.RED + "Reloaded ElixirRealms");
			return true;
		}

		return false;
	}

	public void reloadCfg() {

		reloadConfig();

		Main.config = getConfig();

	}

	@Override
	public void onDisable() {

	}

	public static FileConfiguration getCfg() {

		return config;
	}

	public static String colorCode(String a) {
		String b = ChatColor.translateAlternateColorCodes('&', a);

		return b;

	}

	public static boolean connect(Player player, String server) {

		try {

			if (server.length() == 0) {
				player.sendMessage("§cTarget server was \"\" (empty string) cannot connect to it.");
				return false;
			}

			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(byteArray);

			out.writeUTF("Connect");
			out.writeUTF(server); // Target Server.

			player.sendPluginMessage(Main.getInstance(), "BungeeCord", byteArray.toByteArray());

		} catch (Exception ex) {
			player.sendMessage(ChatColor.RED
					+ "An unexpected exception has occurred. Please notify the server's staff about this. (They should look at the console).");
			ex.printStackTrace();
			return false;
		}

		return true;
	}

	public static Plugin getInstance() {

		return main;
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		if (subchannel.equals("SomeSubChannel")) {

		}
		if (subchannel.equals("PlayerCount")) {
			String server = in.readUTF();
			int playerCount = in.readInt();

			if(server.equalsIgnoreCase("faction")) factioncount = playerCount;
			
			if(server.equalsIgnoreCase("lobby")) lobbycount = playerCount;
			if(server.equalsIgnoreCase("skyblock")) skyblockcount = playerCount;

		}

	}

	public static void getCount(Player player, String server) {

		if (server == null) {
			server = "ALL";
		}

		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerCount");
		out.writeUTF(server);

	}
}
