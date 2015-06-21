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

import ninja.amp.fallout.Fallout;
import ninja.amp.fallout.message.FOMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Contains and controls commands.
 *
 * @author Austin Payne
 */
public class CommandController implements TabExecutor {

    private Fallout plugin;
    private Set<CommandGroup> commands = new HashSet<>();
    private CommandPageList pageList = null;

    /**
     * An empty list of strings for use in command tab completion.
     */
    public static final List<String> EMPTY_LIST = new ArrayList<>();

    /**
     * Creates a new command controller.
     *
     * @param plugin The fallout plugin instance
     */
    public CommandController(Fallout plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        for (CommandGroup command : commands) {
            if (command.getName().equalsIgnoreCase(cmd.getName())) {
                String subCommand = args.length > 0 ? args[0] : "";
                if (command.hasChildCommand(subCommand)) {
                    if (subCommand.equals("")) {
                        command.execute(subCommand, sender, args);
                    } else {
                        String[] newArgs;
                        if (args.length == 1) {
                            newArgs = new String[0];
                        } else {
                            newArgs = new String[args.length - 1];
                            System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                        }
                        command.execute(subCommand, sender, newArgs);
                    }
                } else {
                    plugin.getMessenger().sendMessage(sender, FOMessage.COMMAND_INVALID, "\"" + subCommand + "\"", "\"" + command.getName() + "\"");
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        for (CommandGroup command : commands) {
            if (command.getName().equalsIgnoreCase(cmd.getName())) {
                if (args.length > 0) {
                    int commandAmount = 0;
                    for (String arg : args) {
                        if (!arg.isEmpty() && command.hasChildCommand(arg)) {
                            command = command.getChildCommand(arg);
                            commandAmount++;
                        }
                    }
                    if (command instanceof Command && args.length - commandAmount > command.getMaxArgsLength()) {
                        return EMPTY_LIST;
                    } else {
                        String[] actualArgs;
                        if (args.length > commandAmount) {
                            actualArgs = new String[args.length - commandAmount];
                            System.arraycopy(args, commandAmount, actualArgs, 0, actualArgs.length);
                        } else {
                            actualArgs = new String[0];
                        }
                        return command.getTabCompleteList(actualArgs);
                    }
                } else {
                    return command.getTabCompleteList(args);
                }
            }
        }
        return EMPTY_LIST;
    }

    /**
     * Gets the commands in the command controller.
     *
     * @return The commands
     */
    public Set<CommandGroup> getCommands() {
        return commands;
    }

    /**
     * Adds a command to the command controller.
     *
     * @param command The command to add
     */
    public void addCommand(CommandGroup command) {
        commands.add(command);
        plugin.getCommand(command.getName()).setExecutor(this);
        pageList = new CommandPageList(plugin);
    }

    /**
     * Gets the page list of commands in the command controller.
     *
     * @return The command page list
     */
    public CommandPageList getPageList() {
        return pageList;
    }

    /**
     * Destroys the command controller.
     */
    public void destroy() {
        for (CommandGroup command : commands) {
            plugin.getCommand(command.getName()).setExecutor(null);
        }
    }

}
