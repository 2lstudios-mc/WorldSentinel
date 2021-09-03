package dev._2lstudios.worldsentinel.commands;

import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;

public class RegionCommand implements CommandExecutor {
    private final Server server;
    private final RegionManager regionManager;

    public RegionCommand(final Server server, final RegionManager regionManager) {
        this.server = server;
        this.regionManager = regionManager;
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label,
            final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando no puede ser utilizado desde la consola!");
        } else if (!sender.hasPermission("worldsentinel.admin")) {
            sender.sendMessage(ChatColor.RED + "Permisos insuficientes.");
        } else {
            final Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage(ChatColor.RED
                        + "/rg <set/create/delete/list/here/addmember/removemember/addowner/removeowner/addtag/removetag>");
            } else if (args[0].equalsIgnoreCase("here")) {
                new HereCommand(this.regionManager, player);
            } else if (args[0].equalsIgnoreCase("list")) {
                new ListCommand(this.server, this.regionManager, player);
            } else if (args[0].equalsIgnoreCase("create")) {
                if (args.length == 1) {
                    player.sendMessage(ChatColor.RED + "/rg create <region>");
                } else {
                    final Region region = this.regionManager.createRegion(args[1]);
                    region.getFlags().set("world", player.getWorld().getName());
                    player.sendMessage(ChatColor.GREEN + "Region " + args[1] + " created succesfully!");
                }
            } else if (args[0].equalsIgnoreCase("delete")) {
                if (args.length == 1) {
                    player.sendMessage(ChatColor.RED + "/rg delete <region>");
                } else {
                    this.regionManager.deleteRegion(args[1]);
                    player.sendMessage(ChatColor.GREEN + "Region " + args[1] + " deleted succesfully!");
                }
            } else if (args[0].equalsIgnoreCase("info")) {
                new InfoCommand(this.regionManager, args, player);
            } else if (args[0].equalsIgnoreCase("flag")) {
                new FlagCommand(this.regionManager, args, player);
            } else {
                player.sendMessage(ChatColor.RED
                        + "/rg <flag/create/delete/list/here/addmember/removemember/addowner/removeowner/addtag/removetag>");
            }
        }
        return true;
    }
}
