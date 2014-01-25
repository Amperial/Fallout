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
import me.ampayne2.fallout.config.ConfigType;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages all of the fallout Characters.
 */
public class CharacterManager {
    private final Fallout fallout;
    private final Map<String, Character> characters = new HashMap<>();

    /**
     * Creates a new CharacterManager.
     *
     * @param fallout The {@link me.ampayne2.fallout.Fallout} instance.
     */
    public CharacterManager(Fallout fallout) {
        this.fallout = fallout;

        FileConfiguration characterConfig = fallout.getConfigManager().getConfig(ConfigType.CHARACTER);
        if (characterConfig.getConfigurationSection("Characters") != null) {
            for (String playerName : characterConfig.getConfigurationSection("Characters").getKeys(false)) {
                if (!hasCharacter(playerName)) {
                    characters.put(playerName, new Character(fallout, characterConfig.getConfigurationSection("Characters." + playerName)));
                }
            }
        }
    }

    /**
     * Checks if the manager has a character.
     *
     * @param playerName The name of the character's owner.
     * @return True if the manager has the character, else false.
     */
    public boolean hasCharacter(String playerName) {
        return characters.containsKey(playerName);
    }

    /**
     * Creates a character.
     *
     * @param playerName    The name of the character's owner.
     * @param characterName The character's name.
     * @return The Character.
     */
    public Character createCharacter(String playerName, String characterName) {
        if (!characters.containsKey(playerName)) {
            Character character = new Character(fallout, playerName, characterName);
            characters.put(playerName, character);
            FileConfiguration characterConfig = fallout.getConfigManager().getConfig(ConfigType.CHARACTER);
            String path = "Characters." + playerName;
            characterConfig.createSection(path);
            character.save(characterConfig.getConfigurationSection(path));
            fallout.getConfigManager().getConfigAccessor(ConfigType.CHARACTER).saveConfig();
            return character;
        }
        return null;
    }

    /**
     * Removes a character.
     *
     * @param playerName The name of the character's owner.
     */
    public void removeCharacter(String playerName) {
        Character character = characters.get(playerName);
        if (character != null) {
            fallout.getConfigManager().getConfig(ConfigType.CHARACTER).set("Characters." + playerName, null);
            fallout.getConfigManager().getConfigAccessor(ConfigType.CHARACTER).saveConfig();
            characters.remove(playerName);
        }
    }

    /**
     * Gets a character.
     *
     * @param playerName The name of the character's owner.
     * @return The Character.
     */
    public Character getCharacter(String playerName) {
        return characters.get(playerName);
    }

    /**
     * Gets the Characters in the manager.
     *
     * @return All of the manager's Characters.
     */
    public Map<String, Character> getCharacters() {
        return characters;
    }

    /**
     * Gets a string list of the Characters in the manager.
     *
     * @return A string list of all of the manager's Characters.
     */
    public List<String> getCharacterList() {
        return new ArrayList<>(characters.keySet());
    }
}
