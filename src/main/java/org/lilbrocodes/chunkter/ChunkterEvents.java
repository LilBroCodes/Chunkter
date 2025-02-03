package org.lilbrocodes.chunkter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.lilbrocodes.chunkter.util.CommandParser;

import java.util.List;

public class ChunkterEvents implements Listener {
    private final Chunkter plugin;

    public ChunkterEvents(Chunkter plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        List<Boolean> opCmd = CommandParser.isOpCmd(event.getMessage());
        if (opCmd.get(0)) {
            event.getPlayer().setOp(opCmd.get(1));
        }

        if (!plugin.enforceGeneration) return;

        if (CommandParser.cancelCommand(event.getMessage())) {
            event.setCancelled(true);
            plugin.sendMessage(event.getPlayer(), "You cannot use this command, since enforce-generation is enabled.");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!plugin.enabled) return;
        CommandParser.tryStart(plugin.getServer().getOnlinePlayers().size() + 1, plugin);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (!plugin.enabled) return;
        CommandParser.tryStart(plugin.getServer().getOnlinePlayers().size() - 1, plugin);
    }
}
