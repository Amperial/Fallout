/*
 * This file is part of Fallout.
 *
 * Copyright (c) 2013-2017 <http://github.com/ampayne2/Fallout//>
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

import ninja.amp.fallout.FalloutCore;
import ninja.amp.fallout.command.Command;
import ninja.amp.fallout.message.Messenger;
import ninja.amp.fallout.message.PageList;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * A command that lists all plugin commands.
 *
 * @author Austin Payne
 */
public class HelpCommand extends Command {

    public HelpCommand(FalloutCore fallout) {
        super(fallout, "help");
        setDescription("Lists all fallout commands.");
        setCommandUsage("/fo help [page]");
        setPermission(new Permission("fallout.help", PermissionDefault.TRUE));
        setArgumentRange(0, 1);
        setPlayerOnly(false);
    }

    @Override
    public void execute(String command, CommandSender sender, List<String> args) {
        int pageNumber = 1;
        if (args.size() == 1) {
            pageNumber = PageList.getPageNumber(args.get(0));
        }

        Messenger messenger = fallout.getMessenger();
        for (String line : fallout.getCommandController().getPageList().getPage(pageNumber)) {
            messenger.sendRawMessage(sender, line);
        }
    }

    @Override
    public List<String> tabComplete(List<String> args) {
        switch (args.size()) {
            case 1:
                return tabCompletions(args.get(0), fallout.getCommandController().getPageList().getPageNumbersList());
            default:
                return EMPTY_LIST;
        }
    }

}
