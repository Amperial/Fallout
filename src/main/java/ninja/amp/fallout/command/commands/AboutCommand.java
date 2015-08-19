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
import ninja.amp.fallout.message.Messenger;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * A command that lists some information about the plugin.
 *
 * @author Austin Payne
 */
public class AboutCommand extends Command {

    private final String header;
    private final List<String> info = new ArrayList<>();

    public AboutCommand(Fallout plugin) {
        super(plugin, "");
        setDescription("Lists some information about fallout.");
        setCommandUsage("/fo");
        setPermission(new Permission("fallout.about", PermissionDefault.TRUE));
        setPlayerOnly(false);

        header = Messenger.HIGHLIGHT_COLOR + "<-------<| " + Messenger.PRIMARY_COLOR + "About Fallout " + Messenger.HIGHLIGHT_COLOR + "|>------->";
        info.add(Messenger.SECONDARY_COLOR + "Author: " + StringUtils.join(plugin.getDescription().getAuthors(), ", "));
        info.add(Messenger.SECONDARY_COLOR + "Version: " + plugin.getDescription().getVersion());
        info.add(Messenger.SECONDARY_COLOR + "Help: /fo help [page]");
    }

    @Override
    public void execute(String command, CommandSender sender, List<String> args) {
        sender.sendMessage(header);
        for (String message : info) {
            sender.sendMessage(message);
        }
    }

    /**
     * Adds more info to the about command.
     *
     * @param message The info message to add
     */
    public void addInfo(String message) {
        info.add(message);
    }

}
