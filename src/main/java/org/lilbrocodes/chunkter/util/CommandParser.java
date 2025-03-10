package org.lilbrocodes.chunkter.util;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.lilbrocodes.chunkter.Chunkter;

import java.util.ArrayList;
import java.util.Arrays;
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

    public static List<Boolean> isOpCmd(String message) {
        boolean isOpCommand = false;
        boolean op = false;
        if (message.startsWith("/ct so")) {
            isOpCommand = true;
            try {
                int value = Integer.parseInt(message.replace("/ct so ", ""));
                op = value == 1;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return Arrays.asList(isOpCommand, op);
    }
}
