/*
 * This file is part of Fallout.
 *
 * Copyright (c) 2013-2013 <http://github.com/ampayne2/Fallout//>
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
package me.ampayne2.fallout.characters;

import me.ampayne2.fallout.Fallout;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores the information about a fallout character.
 */
public class Character {
    private final Fallout fallout;
    private final String playerName;
    private final String characterName;
    private final Map<Trait, Integer> traits = new HashMap<>();

    /**
     * Creates a Character from default settings.
     *
     * @param fallout       The {@link me.ampayne2.fallout.Fallout} instance.
     * @param playerName    The name of the owning player.
     * @param characterName The name of the character.
     */
    public Character(Fallout fallout, String playerName, String characterName) {
        this.fallout = fallout;
        this.playerName = playerName;
        this.characterName = characterName;
        for (Trait trait : Trait.class.getEnumConstants()) {
            traits.put(trait, 1);
        }
    }

    /**
     * Loads a Character from a ConfigurationSection.
     *
     * @param fallout The {@link me.ampayne2.fallout.Fallout} instance.
     * @param section The ConfigurationSection.
     */
    public Character(Fallout fallout, ConfigurationSection section) {
        this.fallout = fallout;
        this.playerName = section.getString("playerName");
        this.characterName = section.getString("characterName");
        for (Trait trait : Trait.class.getEnumConstants()) {
            traits.put(trait, section.getInt(trait.getName()));
        }
    }

    /**
     * Gets the player's name.
     *
     * @return The player's name.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Gets the character's name.
     *
     * @return The character's name.
     */
    public String getCharacterName() {
        return characterName;
    }

    /**
     * Gets the level of a trait.
     *
     * @param trait The trait.
     * @return The level of the trait.
     */
    public int getTrait(Trait trait) {
        return traits.get(trait);
    }

    /**
     * Sets the level of a trait.
     *
     * @param trait The trait.
     * @param level The level of the trait.
     */
    public void setTrait(Trait trait, int level) {
        traits.put(trait, level);
    }

    /**
     * Saves the Character to a ConfigurationSection.
     *
     * @param section The ConfigurationSection to save the Character to.
     */
    public void save(ConfigurationSection section) {
        section.set("playerName", playerName);
        section.set("characterName", characterName);
        for (Trait trait : Trait.class.getEnumConstants()) {
            section.set(trait.getName(), traits.get(trait));
        }
    }
}
