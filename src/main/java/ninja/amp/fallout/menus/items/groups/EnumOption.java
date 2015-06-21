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
 * Tracks enum option items selected by players.
 *
 * @author Austin Payne
 */
public class EnumOption<E extends Enum<E>> {

    private Map<UUID, EnumOptionItem<E>> selected = new HashMap<>();
    private Map<E, EnumOptionItem<E>> options = new HashMap<>();

    /**
     * Gets the enum option item selected by a player.
     *
     * @param player The player
     * @return The player's currently selected enum option item
     */
    public EnumOptionItem<E> getSelected(Player player) {
        return selected.get(player.getUniqueId());
    }

    /**
     * Sets the enum option item selected by a player.
     *
     * @param player The player
     * @param item   The enum option item
     */
    public void setSelected(Player player, EnumOptionItem<E> item) {
        selected.put(player.getUniqueId(), item);
    }

    /**
     * Removes a player's current enum option item selection.
     *
     * @param player The player
     */
    public void removeSelected(Player player) {
        selected.remove(player.getUniqueId());
    }

    /**
     * Gets the enum option item represented by a certain enum constant.
     *
     * @param e The enum constant
     * @return The enum option item
     */
    public EnumOptionItem<E> getOption(E e) {
        return options.get(e);
    }

    /**
     * Adds an enum option item to the group of possible options.
     *
     * @param option The enum option item
     */
    public void addOption(EnumOptionItem<E> option) {
        options.put(option.getEnum(), option);
    }

}
