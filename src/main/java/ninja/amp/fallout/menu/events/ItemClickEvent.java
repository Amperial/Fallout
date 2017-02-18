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
package ninja.amp.fallout.menu.events;

import ninja.amp.fallout.character.Character;
import org.bukkit.entity.Player;

/**
 * An event called when an item in the item menu is clicked.
 *
 * @author Austin Payne
 */
public class ItemClickEvent {

    private Player player;
    private Character character;
    private boolean goBack = false;
    private boolean close = false;
    private boolean update = false;

    public ItemClickEvent(Player player, Character character) {
        this.player = player;
        this.character = character;
    }

    public ItemClickEvent(Player player) {
        this.player = player;
        this.character = null;
    }

    /**
     * Gets the player who clicked.
     *
     * @return The player who clicked
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the character of the player who clicked.
     *
     * @return The character, if the player is an owner
     */
    public Character getCharacter() {
        return character;
    }

    /**
     * Checks if the {@link ninja.amp.fallout.menu.ItemMenu} will go back to the parent menu.
     *
     * @return {@code true} if the item menu will go back to the parent menu
     */
    public boolean willGoBack() {
        return goBack;
    }

    /**
     * Sets if the {@link ninja.amp.fallout.menu.ItemMenu} will go back to the parent menu.
     *
     * @param goBack If the item menu will go back to the parent menu.
     */
    public void setWillGoBack(boolean goBack) {
        this.goBack = goBack;
        if (goBack) {
            close = false;
            update = false;
        }
    }

    /**
     * Checks if the item menu will close.
     *
     * @return {@code true} if the item menu will close
     */
    public boolean willClose() {
        return close;
    }

    /**
     * Sets if the item menu will close.
     *
     * @param close If the item menu will close
     */
    public void setWillClose(boolean close) {
        this.close = close;
        if (close) {
            goBack = false;
            update = false;
        }
    }

    /**
     * Checks if the item menu will update.
     *
     * @return {@code true} if the item menu will update
     */
    public boolean willUpdate() {
        return update;
    }

    /**
     * Sets if the item menu will update.
     *
     * @param update If the item menu will update
     */
    public void setWillUpdate(boolean update) {
        this.update = update;
        if (update) {
            goBack = false;
            close = false;
        }
    }

}
