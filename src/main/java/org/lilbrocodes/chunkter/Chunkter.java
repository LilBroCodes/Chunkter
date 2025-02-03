package org.lilbrocodes.chunkter;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.lilbrocodes.chunkter.implementation.Metrics;
import org.lilbrocodes.chunkter.util.CommandParser;
import org.lilbrocodes.chunkter.util.LoggingSender;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public final class Chunkter extends JavaPlugin {
    public boolean enabled = false;
    public boolean running = false;

    // ---------------- CONFIG ---------------- //

    public boolean enforceGeneration = false;
    public boolean messageOnComplete = false;

    public int maxPlayers = 1;
    public int webhookId = 0;

    public Integer diameter = null;
    public Integer radius = null;
    public Integer centerX = null;
    public Integer centerY = null;

    public String message = "%world_name% finished generating!";
    public String world = "world";
    public String preset = "border";

    // ---------------------------------------- //

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

        Bukkit.getScheduler().runTaskTimer(this, this::tick, 0L, 1L);
    }

    public void tick() {
        if (enabled) {
            if (running) {
                CommandParser.resumeChunky(commandSender);
            } else {
                CommandParser.pauseChunky(commandSender);
            }
        }
    }

    private void fetchConfig() {
        config = this.getConfig();

        enforceGeneration = config.getBoolean("enforce-generation");
        messageOnComplete = config.getBoolean("message-on-complete");

        maxPlayers = config.getInt("max-players");
        webhookId = config.getInt("webhook-id");

        message = config.getString("message");

        ConfigurationSection section = config.getConfigurationSection("world");
        if (section == null) return;

        world = section.getString("name");
        preset = section.getString("preset");

        if (Objects.equals(preset, "manual")) {
            if (section.contains("diameter")) {
                diameter = section.getInt("diameter");
                radius = null;
            } else if (section.contains("radius")) {
                radius = section.getInt("radius");
                diameter = null;
            } else {
                radius = null;
                diameter = null;
            }

            if (section.contains("center-x") && section.contains("center-y")) {
                centerX = section.getInt("center-x");
                centerY = section.getInt("center-y");
            } else {
                centerX = null;
                centerY = null;
            }
        }
    }

    public void loadConfig() {
        this.saveDefaultConfig();
        fetchConfig();
    }

    public void reloadConfigC() {
        this.reloadConfig();
        fetchConfig();
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
}
