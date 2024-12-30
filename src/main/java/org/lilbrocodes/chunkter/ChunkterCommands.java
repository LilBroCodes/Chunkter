package org.lilbrocodes.chunkter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.lilbrocodes.chunkter.util.CommandParser;
import org.lilbrocodes.chunkter.util.LoggingSender;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChunkterCommands implements CommandExecutor, TabCompleter {
    private final Chunkter plugin;

    public ChunkterCommands(Chunkter plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length >= 1 && args[0].equalsIgnoreCase("status")) {
            LoggingSender sender = new LoggingSender(commandSender);

            plugin.sendMessage(commandSender, "Misc:");
            plugin.sendMessage(commandSender, "    " + (Boolean.TRUE.equals(CommandParser.running(sender)) ? "Chunky is running." : "Chunky is not running."));
            plugin.sendMessage(commandSender, "    " + (plugin.running ? "Chunkter is running." : "Chunkter is not running."));
            plugin.sendMessage(commandSender, "    " + (plugin.enabled ? "Chunkter is enabled." : "Chunkter is not enabled."));

            plugin.sendMessage(commandSender, "Config:");
            plugin.sendMessage(commandSender, "    " + String.format("enforce-generation: %b", plugin.enforceGeneration));
            plugin.sendMessage(commandSender, "    " + String.format("max-players: %d", plugin.maxPlayers));
        }

        if (args.length >= 1 && CommandParser.equalsAnyLowercase(args[0], Arrays.asList("on", "off"))) {
            boolean enabled = args[0].equalsIgnoreCase("on");
            if (plugin.enabled != enabled) {
                plugin.enabled = enabled;
                plugin.sendMessage(commandSender, enabled ? "Enabled chunkter." : "Disabled chunkter.");
                CommandParser.tryStart(plugin.getServer().getOnlinePlayers().size(), plugin);
            } else {
                plugin.sendMessage(commandSender, enabled ? "Chunkter is already enabled." : "Chunkter is already disabled.");
            }
        }

        if (args.length >= 3 && args[0].equalsIgnoreCase("config")) {
            if (args[1].equalsIgnoreCase("max-players")) {
                try {
                    int playerAmount = Integer.parseInt(args[2]);
                    plugin.setPlayerCount(playerAmount);
                    plugin.sendMessage(commandSender, String.format("Set max player count to %d.", playerAmount));
                    CommandParser.tryStart(plugin.getServer().getOnlinePlayers().size(), plugin);
                } catch (NumberFormatException e) {
                    plugin.sendMessage(commandSender, "Player count not specified or not a valid integer.");
                } catch (IOException e) {
                    plugin.sendMessage(commandSender, "Failed to save config file.");
                }
            } else if (args[1].equalsIgnoreCase("enforce-generation")) {
                boolean enforceGeneration = args[2].equalsIgnoreCase("true");
                if (plugin.enforceGeneration == enforceGeneration) {
                    plugin.sendMessage(commandSender, enforceGeneration ? "Enforcing generation is already enabled." : "Enforcing generation is already disabled.");
                } else {
                    try {
                        plugin.setEnforceGeneration(enforceGeneration);
                        plugin.sendMessage(commandSender, enforceGeneration ? "Enforcing generation enabled." : "Enforcing generation disabled.");
                    } catch (IOException e) {
                        plugin.sendMessage(commandSender, "Failed to save config file.");
                    }
                }
            }
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfigC();
            plugin.sendMessage(commandSender, "Successfully reloaded the config.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("on", "off", "config", "status", "reload");
        } else if (args.length == 2) {
            return Arrays.asList("max-players", "enforce-generation");
        } else if (args.length == 3) {
            if (args[1].equalsIgnoreCase("enforce-generation")) {
                return Arrays.asList("true", "false");
            }
        }

        return Collections.emptyList();
    }
}
