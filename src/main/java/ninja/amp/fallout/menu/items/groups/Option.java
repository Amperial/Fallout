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
package ninja.amp.fallout.menu.items.groups;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Tracks option items selected by players.
 *
 * @author Austin Payne
 */
public class Option {

    private Map<UUID, OptionItem> selected = new HashMap<>();
    private Map<String, OptionItem> options = new HashMap<>();

    /**
     * Gets the option item selected by a player.
     *
     * @param player The player
     * @return The player's currently selected option item
     */
    public OptionItem getSelected(Player player) {
        return selected.get(player.getUniqueId());
    }

    /**
     * Sets the option item selected by a player.
     *
     * @param player The player
     * @param item   The option item
     */
    public void setSelected(Player player, OptionItem item) {
        selected.put(player.getUniqueId(), item);
    }

    /**
     * Removes a player's current option item selection.
     *
     * @param player The player
     */
    public void removeSelected(Player player) {
        selected.remove(player.getUniqueId());
    }

    /**
     * Gets the option item represented by its display name.
     *
     * @param name The name
     * @return The option item
     */
    public OptionItem getOption(String name) {
        return options.get(name);
    }

    /**
     * Adds an option item to the group of possible options.
     *
     * @param option The option item
     */
    public void addOption(OptionItem option) {
        options.put(option.getDisplayName(), option);
    }

}
