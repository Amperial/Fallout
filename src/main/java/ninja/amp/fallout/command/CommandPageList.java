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
import ninja.amp.fallout.message.Messenger;
import ninja.amp.fallout.message.PageList;

import java.util.ArrayList;
import java.util.List;

/**
 * A PageList that lists all of the commands and their description.
 *
 * @author Austin Payne
 */
public class CommandPageList extends PageList {

    private final List<String> pageNumbersList = new ArrayList<>();

    public CommandPageList(Fallout plugin) {
        super(plugin, "Commands", 8);
        List<String> strings = new ArrayList<>();
        for (CommandGroup command : plugin.getCommandController().getCommands()) {
            for (CommandGroup child : command.getChildren(true)) {
                if (((Command) child).getVisible()) {
                    strings.add(Messenger.PRIMARY_COLOR + ((Command) child).getCommandUsage());
                    strings.add(Messenger.SECONDARY_COLOR + "-" + ((Command) child).getDescription());
                }
            }
        }
        setStrings(strings);

        int pageAmount = getPageAmount();
        for (int i = 1; i <= pageAmount; i++) {
            pageNumbersList.add(String.valueOf(i));
        }
    }

    /**
     * Gets the page numbers list of the command page list.
     *
     * @return The list of page numbers.
     */
    public List<String> getPageNumbersList() {
        return pageNumbersList;
    }

}