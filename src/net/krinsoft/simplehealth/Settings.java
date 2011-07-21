package net.krinsoft.simplehealth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.config.Configuration;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class Settings {
	public static PermissionHandler permissions;
	public static Configuration basic;
	public static HashMap<String, Configuration> locale = new HashMap<String, Configuration>();
	public static File dataFolder;

	private static File defaults(File file) {
		if (!file.exists()) {
			new File(file.getParent()).mkdirs();
			InputStream in = SimpleHealth.class.getResourceAsStream("/defaults/" + file.getName());
			if (in != null) {
				FileOutputStream out = null;
				try {
					out = new FileOutputStream(file);
					byte[] buffer = new byte[2048];
					int length = 0;
					while ((length = in.read(buffer)) > 0) {
						out.write(buffer, 0, length);
					}
					SimpleHealth.logAdd(file.getName() + " created.");
				} catch(IOException e) {
					SimpleHealth.logAdd("Error creating " + file.getName());
					e.printStackTrace();
				} finally {
					try {
						in.close();
						out.close();
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return file;
	}
	
	public static boolean configure() {
		File file1 = defaults(new File(dataFolder, "config.yml"));
		if (file1.exists()) {
			basic = new Configuration(file1);
			basic.load();
			List<String> list = basic.getStringList("plugin.locales", new ArrayList<String>());
			File file2;
			Configuration tmp;
			for (String loc : list) {
				file2 = defaults(new File(dataFolder + File.separator + "localizations", loc + ".yml"));
				if (file2.exists()) {
					tmp = new Configuration(file2);
					tmp.load();
					locale.put(loc, tmp);
					locale.get(loc).getString("plugin.loaded");
				}
			}
		}
		return true;
	}

	public static void permissions() {
	    Plugin permissionsPlugin = SimpleHealth.plugin.getServer().getPluginManager().getPlugin("Permissions");
	    if (permissions == null) {
	        if (permissionsPlugin != null) {
	            permissions = ((Permissions) permissionsPlugin).getHandler();
	            String pLoad = locale.get(basic.getString("plugin.default_locale")).getString("plugin.permissions.loaded");
	            pLoad = pLoad.replaceAll("<permissions_version>", permissionsPlugin.getDescription().getVersion());
	            SimpleHealth.logAdd(pLoad);
	        } else {
	            SimpleHealth.logAdd(locale.get(basic.getString("plugin.default_locale")).getString("plugin.permissions.failed"));
	            basic.setProperty("plugin.permissions", false);
	            basic.save();
	        }
	    }
	}

	public static SimplePlayer addNewUser(Player player) {
		String name = player.getName();
		String locale = basic.getString("plugin.default_locale");
		String group = null;
		if (basic.getBoolean("plugin.permissions", false)) {
			for (String key : basic.getKeys("groups")) {
				if (player.hasPermission("simplehealth." + key)) {
					group = key;
					break;
				} 				
			}
		}
		if (group == null) {
			group = (player.isOp()) ? "admins" : "users";
		}
		SimpleHealth.players.put(player, new SimplePlayer(player, group));
		String user = Settings.locale.get(locale).getString("plugin.user_created").replaceAll("<user>", name);
		user = user.replaceAll("<group>", group);
		SimpleHealth.logAdd(user);
		return SimpleHealth.players.get(player);
	}

}
