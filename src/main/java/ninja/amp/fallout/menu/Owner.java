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
package ninja.amp.fallout.menu;

import ninja.amp.fallout.character.Character;
import org.bukkit.entity.Player;

/**
 * Represents a fallout character and the player that currently owns it.
 *
 * @author Austin Payne
 */
public class Owner {

    private final Player player;
    private final Character character;

    public Owner(Player player, Character character) {
        this.player = player;
        this.character = character;
    }

    /**
     * Gets the player of the owner.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the character owned by the player.
     *
     * @return The character.
     */
    public Character getCharacter() {
        return character;
    }

}
