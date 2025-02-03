package org.lilbrocodes.chunkter.util;

import org.bukkit.Bukkit;
import org.lilbrocodes.chunkter.Chunkter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CommandParser {
    public static Boolean running(LoggingSender sender) {
        Bukkit.dispatchCommand(sender, "cy progress");
        String response = sender.getLatestMessage();
        if (response != null) {
            return response.contains("Task running for");
        }

        return null;
    }

    public static void resumeChunky(LoggingSender sender) {
        Bukkit.dispatchCommand(sender, "cy start");
        String response = sender.getLatestMessage();
        if (response != null) {
            if (response.contains("already started")) {
                Bukkit.dispatchCommand(sender, "cy continue");
            }
        }
    }

    public static void pauseChunky(LoggingSender sender) {
        Bukkit.dispatchCommand(sender, "cy pause");
    }

    public static boolean cancelCommand(String command) {
        String[] args = command.split(" ");
        List<String> chunkyCommands = new ArrayList<>(Arrays.asList("/chunky", "/cy", "/chunky:chunky", "/chunky:cy"));
        if (args.length >= 2) {
            if (equalsAnyLowercase(args[0], chunkyCommands)) {
                return !equalsAnyLowercase(args[1], Arrays.asList("quiet", "progress"));
            }
        }

        return false;
    }

    public static boolean equalsAnyLowercase(String s, List<String> l) {
        for (String str : l) {
            if (s.equalsIgnoreCase(str)) return true;
        }
        return false;
    }

    public static void tryStart(int playerCount, Chunkter plugin) {
        plugin.running = playerCount <= plugin.maxPlayers;
    }

    public static void configure(LoggingSender sender, Chunkter ct) {
        sender.clearCommands();

        if (ct.world != null) {
            sender.addCommand(String.format("cy world %s", ct.world));
        }

        if (Objects.equals(ct.preset, "border")) {
            sender.addCommand("cy worldborder");
        } else if (Objects.equals(ct.preset, "manual")) {
            if (ct.centerX != null && ct.centerY != null) {
                sender.addCommand(String.format("cy center %d %d", ct.centerX, ct.centerY));
            }
            if (ct.radius != null) {
                sender.addCommand(String.format("cy radius %d", ct.radius));
            } else if (ct.diameter != null) {
                sender.addCommand(String.format("cy radius %d", ct.diameter / 2));
            }
        }

        sender.doCommands(true);
    }
}
