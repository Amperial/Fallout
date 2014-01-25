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
package me.ampayne2.fallout.command.commands;

import me.ampayne2.fallout.Fallout;
import me.ampayne2.fallout.command.FOCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that lists some information about fallout.
 */
public class About extends FOCommand {
    private final Fallout fallout;
    private final static String HEADER = ChatColor.RED + "<-------<| " + ChatColor.YELLOW + "About Fallout " + ChatColor.RED + "|>------->";
    private final static String URL = ChatColor.GRAY + "http://github.com/ampayne2/Fallout/";
    private final static String AUTHOR = ChatColor.GRAY + "Author: ampayne2";
    private final static String VERSION = ChatColor.GRAY + "Version: ";
    private final static String COMMANDS = ChatColor.GRAY + "Commands: /fo help [page]";

    public About(Fallout fallout) {
        super(fallout, "", "Lists some information about fallout.", "/fo", new Permission("fallout.about", PermissionDefault.TRUE), false);
        this.fallout = fallout;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        sender.sendMessage(HEADER);
        sender.sendMessage(URL);
        sender.sendMessage(AUTHOR);
        sender.sendMessage(VERSION + fallout.getDescription().getVersion());
        sender.sendMessage(COMMANDS);
    }
}
