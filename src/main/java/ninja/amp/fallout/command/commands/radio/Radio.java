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
package ninja.amp.fallout.command.commands.radio;

import ninja.amp.fallout.FalloutCore;
import ninja.amp.fallout.command.Command;
import ninja.amp.fallout.menu.ItemMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * A command that lets the sender view and play various minecraft records.
 *
 * @author Austin Payne
 */
public class Radio extends Command {

    private ItemMenu menu;

    public Radio(FalloutCore fallout) {
        super(fallout, "radio");
        setDescription("Opens the breach radio menu.");
        setCommandUsage("/bl radio");
        setPermission(new Permission("breach.radio", PermissionDefault.TRUE));

        this.menu = new RadioMenu(fallout);
    }

    @Override
    public void execute(String command, CommandSender sender, List<String> args) {
        menu.open((Player) sender);
    }

}
