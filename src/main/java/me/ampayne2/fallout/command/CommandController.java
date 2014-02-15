/*
 * This file is part of Fallout.
 *
 * Copyright (c) 2013-2013 <http://github.com/ampayne2/Fallout//>
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
package me.ampayne2.fallout.command;

import me.ampayne2.fallout.Fallout;
import me.ampayne2.fallout.command.commands.*;
import me.ampayne2.fallout.command.commands.character.*;
import me.ampayne2.fallout.message.PageList;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * The fallout command executor.
 */
public class CommandController implements TabExecutor {
    private final Fallout fallout;
    private final Command mainCommand;
    private final PageList pageList;

    /**
     * Creates a new command controller.
     *
     * @param fallout The {@link me.ampayne2.fallout.Fallout} instance.
     */
    public CommandController(Fallout fallout) {
        this.fallout = fallout;

        mainCommand = new Command(fallout, "fallout", new Permission("fallout.all", PermissionDefault.OP), false)
                .addChildCommand(new About(fallout))
                .addChildCommand(new Help(fallout))
                .addChildCommand(new Reload(fallout))
                .addChildCommand(new Whois(fallout))
                .addChildCommand(new Command(fallout, "character", new Permission("fallout.character.all", PermissionDefault.OP), false)
                        .addChildCommand(new Create(fallout))
                        .addChildCommand(new Delete(fallout))
                        .addChildCommand(new ListSpecial(fallout))
                        .addChildCommand(new SetSpecial(fallout))
                        .addChildCommand(new Teach(fallout))
                        .addChildCommand(new Unteach(fallout))
                        .addChildCommand(new ListSkills(fallout)))
                .addChildCommand(new Roll(fallout));

        fallout.getCommand(mainCommand.getName()).setExecutor(this);

        pageList = new CommandPageList(fallout, mainCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("fallout")) {
            String subCommand = args.length > 0 ? args[0] : "";
            if (mainCommand.hasChildCommand(subCommand)) {
                if (subCommand.equals("")) {
                    mainCommand.execute(subCommand, sender, args);
                } else {
                    String[] newArgs;
                    if (args.length == 1) {
                        newArgs = new String[0];
                    } else {
                        newArgs = new String[args.length - 1];
                        System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                    }
                    mainCommand.execute(subCommand, sender, newArgs);
                }
            } else {
                fallout.getMessenger().sendMessage(sender, "error.command.invalidsubcommand", "\"" + subCommand + "\"", "\"fallout\"");
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("fallout")) {
            Command command = mainCommand;
            if (args.length > 0) {
                int commandAmount = 1;
                for (String arg : args) {
                    if (command.hasChildCommand(arg)) {
                        command = command.getChildCommand(arg);
                        commandAmount++;
                    }
                }
                String[] newArgs;
                if (args.length == 1) {
                    newArgs = new String[0];
                } else {
                    newArgs = new String[args.length - commandAmount];
                    System.arraycopy(args, commandAmount, newArgs, 0, args.length - commandAmount);
                }
                return command.getTabCompleteList(newArgs);
            }
            return command.getTabCompleteList(args);
        }
        return new ArrayList<>();
    }

    /**
     * Gets the main fallout command.
     *
     * @return The main command.
     */
    public Command getMainCommand() {
        return mainCommand;
    }

    /**
     * Gets the PageList of the fallout commands.
     *
     * @return The PageList.
     */
    public PageList getPageList() {
        return pageList;
    }
}
