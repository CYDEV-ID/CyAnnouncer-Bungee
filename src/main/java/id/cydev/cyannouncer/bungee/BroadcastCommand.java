package id.cydev.cyannouncer.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BroadcastCommand extends Command implements TabExecutor {

    private final BungeeAnnouncer plugin;

    public BroadcastCommand(BungeeAnnouncer plugin) {
        super("bcbroadcast", "announcer.broadcast");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Usage: /bcbroadcast <server1,server2,...|all> <message>"));
            return;
        }

        String targets = args[0];
        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        TextComponent componentMessage = new TextComponent(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + message));

        if (targets.equalsIgnoreCase("all")) {
            plugin.getProxy().broadcast(componentMessage);
        } else {
            List<String> targetServers = Arrays.asList(targets.split(","));
            for (ProxiedPlayer player : plugin.getProxy().getPlayers()) {
                if (player.getServer() != null && targetServers.contains(player.getServer().getInfo().getName())) {
                    player.sendMessage(componentMessage);
                }
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            suggestions.add("all");
            suggestions.addAll(plugin.getServers().keySet());
            return suggestions.stream()
                    .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}