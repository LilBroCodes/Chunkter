package org.lilbrocodes.chunkter.util;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class LoggingSender implements CommandSender {
    private final CommandSender wrappedSender;
    private String latestMessage = "";

    public LoggingSender(CommandSender wrappedSender) {
        this.wrappedSender = wrappedSender;
    }

    @Override
    public void sendMessage(String message) {
        latestMessage = message;
    }

    @Override
    public void sendMessage(String[] messages) {
        latestMessage = messages[messages.length - 1];
    }

    public String getLatestMessage() {
        return latestMessage;
    }

    // ---------------- Required Overrides ---------------- //

    @Override
    public Server getServer() {
        return wrappedSender.getServer();
    }

    @Override
    public String getName() {
        return wrappedSender.getName();
    }

    @Override
    public boolean isPermissionSet(String name) {
        return wrappedSender.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return wrappedSender.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return wrappedSender.hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return wrappedSender.hasPermission(perm);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return wrappedSender.addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return wrappedSender.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return wrappedSender.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return wrappedSender.addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        wrappedSender.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        wrappedSender.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return wrappedSender.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return wrappedSender.isOp();
    }

    @Override
    public void setOp(boolean value) {
        wrappedSender.setOp(value);
    }
}
