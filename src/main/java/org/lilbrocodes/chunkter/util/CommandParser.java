package org.lilbrocodes.chunkter.util;

import org.bukkit.Bukkit;
import org.lilbrocodes.chunkter.Chunkter;

import java.util.ArrayList;
import java.util.List;

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
        List<String> chunkyCommands = new ArrayList<>(List.of("/chunky", "/cy", "/chunky:chunky", "/chunky:cy"));
        if (args.length >= 2) {
            if (equalsAnyLowercase(args[0], chunkyCommands)) {
                return !equalsAnyLowercase(args[1], List.of("quiet", "progress"));
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
}
