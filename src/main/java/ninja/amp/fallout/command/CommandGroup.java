/*
 * This file is part of Fallout.
 *
 * Copyright (c) 2013-2015 <http://github.com/ampayne2/Fallout//>
 *
 * Fallout is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Fallout is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Fallout.  If not, see <http://www.gnu.org/licenses/>.
 */
package ninja.amp.fallout.command;

import ninja.amp.fallout.FalloutCore;
import ninja.amp.fallout.message.FOMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A command that only contains child commands.
 *
 * @author Austin Payne
 */
public class CommandGroup {

    protected final FalloutCore fallout;
    private final String name;
    private final Map<String, CommandGroup> children = new LinkedHashMap<>();
    private final List<String> tabCompleteList = new ArrayList<>();
    private Permission permission = null;
    private int minArgsLength = 0;
    private int maxArgsLength = -1;
    private boolean playerOnly = true;

    /**
     * Creates a new command group.
     *
     * @param fallout The fallout plugin core
     * @param name    The name of the command
     */
    public CommandGroup(FalloutCore fallout, String name) {
        this.fallout = fallout;
        this.name = name.toLowerCase();
    }

    /**
     * Gets the command group's name.
     *
     * @return The command group's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the plugin instance that created this command.
     *
     * @return The plugin instance
     */
    public JavaPlugin getPlugin() {
        return fallout.getPlugin();
    }

    /**
     * Gets the command group's permission node.
     *
     * @return The command group's permission node
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     * Sets the command group's permission node.
     *
     * @param permission The command group's permission node
     */
    public void setPermission(Permission permission) {
        this.permission = permission;
        if (Bukkit.getServer().getPluginManager().getPermission(permission.getName()) == null) {
            Bukkit.getServer().getPluginManager().addPermission(permission);
        }
    }

    /**
     * Gets the minimum required args length of the command group.
     *
     * @return The minimum required args length
     */
    public int getMinArgsLength() {
        return minArgsLength;
    }

    /**
     * Gets the maximum required args length of the command group.
     *
     * @return The maximum required args length
     */
    public int getMaxArgsLength() {
        return maxArgsLength;
    }

    /**
     * Sets the argument range of the command group.
     *
     * @param minArgsLength The minimum required args length
     * @param maxArgsLength The maximum required args length. -1 for no max
     */
    public void setArgumentRange(int minArgsLength, int maxArgsLength) {
        this.minArgsLength = minArgsLength;
        this.maxArgsLength = maxArgsLength;
    }

    /**
     * Checks to see if the command group can only be run by a player.
     *
     * @return {@code true} if the command group is player only
     */
    public boolean isPlayerOnly() {
        return playerOnly;
    }

    /**
     * Sets if the command group can only be run by a player.
     *
     * @param playerOnly If the command group can only be run by a player
     */
    public void setPlayerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
    }

    /**
     * Checks to see if the command group has the child command.
     *
     * @param name The name of the child command
     * @return {@code true} if the command group has the child command
     */
    public boolean hasChildCommand(String name) {
        return children.containsKey(name.toLowerCase());
    }

    /**
     * Gets a child command of the command group.
     *
     * @param name The name of the child command
     * @return The child command
     */
    public CommandGroup getChildCommand(String name) {
        return children.get(name.toLowerCase());
    }

    /**
     * Adds a child command to the command group.
     *
     * @param command The child command
     * @return The command group the child command was added to
     */
    public CommandGroup addChildCommand(CommandGroup command) {
        children.put(command.getName().toLowerCase(), command);
        if (command instanceof Command && ((Command) command).getVisible()) {
            tabCompleteList.add(command.getName().toLowerCase());
        }
        if (permission != null && command.getPermission() != null) {
            command.getPermission().addParent(permission, true);
        }
        return this;
    }

    /**
     * Gets the command group's children.
     *
     * @param deep If the method should return all children, or only the command group's immediate children
     * @return The command group's children
     */
    public List<CommandGroup> getChildren(boolean deep) {
        if (deep) {
            List<CommandGroup> deepChildren = new ArrayList<>();
            for (CommandGroup child : children.values()) {
                if (child instanceof Command && !deepChildren.contains(child)) {
                    deepChildren.add(child);
                }
                for (CommandGroup deepChild : child.getChildren(true)) {
                    if (!deepChildren.contains(deepChild)) {
                        deepChildren.add(deepChild);
                    }
                }
            }
            return deepChildren;
        } else {
            return new ArrayList<>(children.values());
        }
    }

    /**
     * Gets the tab completion list of the command group.
     *
     * @param args The args already entered
     * @return The tab completion list of the command group
     */
    public List<String> getTabCompleteList(String[] args) {
        if (args.length == 1) {
            if (args[0].isEmpty()) {
                return tabCompleteList;
            } else {
                String arg = args[0].toLowerCase();
                List<String> modifiedList = new ArrayList<>();
                for (String suggestion : tabCompleteList) {
                    if (suggestion.toLowerCase().startsWith(arg)) {
                        modifiedList.add(suggestion);
                    }
                }
                if (modifiedList.isEmpty()) {
                    return tabCompleteList;
                } else {
                    return modifiedList;
                }
            }
        } else {
            return CommandController.EMPTY_LIST;
        }
    }

    /**
     * The command executor
     *
     * @param command The command label
     * @param sender  The sender of the command
     * @param args    The arguments sent with the command
     */
    public void execute(String command, CommandSender sender, String[] args) {
        CommandGroup entry = children.get(command.toLowerCase());
        if (entry instanceof Command) {
            if ((entry.getMinArgsLength() <= args.length || entry.getMinArgsLength() == -1) && (entry.getMaxArgsLength() >= args.length || entry.getMaxArgsLength() == -1)) {
                if (entry.getPermission() == null || sender.hasPermission(entry.getPermission())) {
                    if (sender instanceof Player || !entry.isPlayerOnly()) {
                        entry.execute(command, sender, args);
                    } else {
                        fallout.getMessenger().sendMessage(sender, FOMessage.COMMAND_NOTAPLAYER);
                    }
                } else {
                    fallout.getMessenger().sendMessage(sender, FOMessage.COMMAND_NOPERMISSION, command);
                }
            } else {
                fallout.getMessenger().sendMessage(sender, FOMessage.COMMAND_USAGE, ((Command) entry).getCommandUsage());
            }
        } else {
            String subCommand = args.length == 0 ? "" : args[0];
            if (entry.hasChildCommand(subCommand)) {
                String[] newArgs;
                if (args.length == 0) {
                    newArgs = args;
                } else {
                    newArgs = new String[args.length - 1];
                    System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                }
                entry.execute(subCommand, sender, newArgs);
            } else {
                fallout.getMessenger().sendMessage(sender, FOMessage.COMMAND_INVALID, "\"" + subCommand + "\"", "\"" + command + "\"");
            }
        }
    }

}
