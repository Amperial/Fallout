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
package ninja.amp.fallout.command.commands;

import ninja.amp.fallout.Fallout;
import ninja.amp.fallout.command.Command;
import ninja.amp.fallout.message.FOMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that reloads the plugin.
 *
 * @author Austin Payne
 */
public class ReloadCommand extends Command {

    public ReloadCommand(Fallout plugin) {
        super(plugin, "reload");
        setDescription("Reloads the fallout plugin");
        setCommandUsage("/fo reload");
        setPermission(new Permission("fallout.reload", PermissionDefault.OP));
        setPlayerOnly(false);
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        Bukkit.getPluginManager().disablePlugin(getPlugin());
        getPlugin().getPluginLoader().enablePlugin(getPlugin());
        fallout.getMessenger().sendMessage(sender, FOMessage.RELOAD);
    }

}
