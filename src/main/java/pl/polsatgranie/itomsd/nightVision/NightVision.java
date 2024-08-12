package pl.polsatgranie.itomsd.nightVision;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.List;

public class NightVision extends JavaPlugin implements TabExecutor {

    @Override
    public void onEnable() {
        Metrics metrics = new Metrics(this, 22936);
        this.getLogger().info("""
                
                ------------------------------------------------------------
                |                                                          |
                |      _  _______        __     __    _____   ____         |
                |     | ||___ ___|      |  \\   /  |  / ____| |  _ \\        |
                |     | |   | |   ___   | |\\\\ //| | | (___   | | \\ \\       |
                |     | |   | |  / _ \\  | | \\_/ | |  \\___ \\  | |  ) )      |
                |     | |   | | | (_) | | |     | |  ____) | | |_/ /       |
                |     |_|   |_|  \\___/  |_|     |_| |_____/  |____/        |
                |                                                          |
                |                                                          |
                ------------------------------------------------------------
                |                 +==================+                     |
                |                 |    NightVision   |                     |
                |                 |------------------|                     |
                |                 |        1.0       |                     |
                |                 |------------------|                     |
                |                 |  PolsatGraniePL  |                     |
                |                 +==================+                     |
                ------------------------------------------------------------
                """);
        this.saveDefaultConfig();
        this.getCommand("nv").setExecutor(this);
        this.getCommand("nv").setAliases(java.util.List.of("nightvision"));
        this.getCommand("nvreload").setExecutor(this);
        this.getCommand("nvreload").setAliases(java.util.List.of("nightvisionreload"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("nv")) {
            if (sender.hasPermission("itomsd.nightvision")) {
                if (args.length == 0) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage("This command can only be run by a player.");
                        return true;
                    }
                    Player player = (Player) sender;
                    toggleNightVision(player);
                    return true;
                } else if (args.length == 1) {
                    if (!sender.hasPermission("itomsd.nightvision.others")) {
                        sender.sendMessage(ChatColor.RED + "You do not have permission to use this command for others.");
                        return true;
                    }
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        sender.sendMessage(ChatColor.RED + "Player not found.");
                        return true;
                    }
                    toggleNightVision(target);
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.no_permission")));
                return true;
            }
        } else if (command.getName().equalsIgnoreCase("nvreload")) {
            if (!sender.hasPermission("itomsd.nightvision.reload")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        getConfig().getString("messages.no_permission_reload", "&cYou do not have permission to reload the configuration.")));
                return true;
            }
            reloadConfig();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    getConfig().getString("messages.plugin_reloaded", "&aPlugin reloaded.")));
            return true;
        }
        return false;
    }

    private void toggleNightVision(Player player) {
        if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    getConfig().getString("messages.nightvision_disabled", "&cNight vision disabled.")));
        } else {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    getConfig().getString("messages.nightvision_enabled", "&aNight vision enabled.")));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("nv") && args.length == 1) {
            return null;
        }
        return Collections.emptyList();
    }
}