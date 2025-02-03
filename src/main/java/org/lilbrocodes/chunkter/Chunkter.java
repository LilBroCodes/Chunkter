package org.lilbrocodes.chunkter;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.lilbrocodes.chunkter.implementation.Metrics;
import org.lilbrocodes.chunkter.util.CommandParser;
import org.lilbrocodes.chunkter.util.LoggingSender;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

public final class Chunkter extends JavaPlugin {
    public boolean defaultEnabled = false;
    public boolean enabled = false;
    public boolean running = false;

    public boolean enforceGeneration = false;
    public int maxPlayers = 1;

    private FileConfiguration config;

    private final LoggingSender commandSender = new LoggingSender(Bukkit.getConsoleSender());

    @Override
    public void onEnable() {
        loadConfig();

        Metrics metrics = new Metrics(this, 24277);
        metrics.addCustomChart(new Metrics.SimplePie("max-players", () -> String.valueOf(maxPlayers)));
        metrics.addCustomChart(new Metrics.SimplePie("enforce-generation", () -> enforceGeneration ? "True" : "False"));

        Plugin chunky = Bukkit.getPluginManager().getPlugin("Chunky");
        if (chunky == null) {
            getLogger().warning("Chunky not found, disabling");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        ChunkterCommands commands = new ChunkterCommands(this);
        Objects.requireNonNull(getCommand("chunkter")).setExecutor(commands);
        Objects.requireNonNull(getCommand("chunkter")).setTabCompleter(commands);

        Bukkit.getPluginManager().registerEvents(new ChunkterEvents(this), this);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            if (enabled) {
                if (running) {
                    CommandParser.resumeChunky(commandSender);
                } else {
                    CommandParser.pauseChunky(commandSender);
                }
            }
        }, 0L, 1L);
    }

    public void loadConfig() {
        this.saveDefaultConfig();
        config = this.getConfig();

        enforceGeneration = config.getBoolean("enforce-generation");
        maxPlayers = config.getInt("max-players");
        enabled = config.getBoolean("default-enabled");
        defaultEnabled = enabled;
    }

    public void reloadConfigC() {
        this.reloadConfig();
        config = this.getConfig();

        enforceGeneration = config.getBoolean("enforce-generation");
        maxPlayers = config.getInt("max-players");
        defaultEnabled = config.getBoolean("default-enabled");
    }

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage("§7[§6Chunkter§7]§r " + message);
    }

    public void sendMessage(Player sender, String message) {
        sender.sendMessage("§7[§6Chunkter§7]§r " + message);
    }

    public void writeConfig() throws IOException {
        config.save(Paths.get(getDataFolder().toString(), "config.yml").toString());
        reloadConfigC();
    }

    public void setPlayerCount(int count) throws IOException {
        config.set("max-players", count);
        writeConfig();
    }

    public void setEnforceGeneration(boolean value) throws IOException {
        config.set("enforce-generation", value);
        writeConfig();
    }

    public void setDefaultEnabled(boolean value) throws IOException {
        config.set("default-enabled", value);
        writeConfig();
    }

}
