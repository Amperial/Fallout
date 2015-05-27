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
package ninja.amp.fallout.menus.items.groups;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Tracks which OptionItem is selected
 */
public class Option {
    private Map<UUID, OptionItem> selected = new HashMap<>();

    /**
     * Gets the OptionItem selected by a player.
     *
     * @param player The player.
     * @return The player's currently selected OptionItem.
     */
    public OptionItem getSelected(Player player) {
        return selected.get(player.getUniqueId());
    }

    /**
     * Sets the OptionItem selected by a player.
     *
     * @param player The player.
     * @param item The OptionItem.
     */
    public void setSelected(Player player, OptionItem item) {
        selected.put(player.getUniqueId(), item);
    }

    /**
     * Removes a player's current OptionItem selection.
     *
     * @param player The player.
     */
    public void removeSelected(Player player) {
        selected.remove(player.getUniqueId());
    }
}
