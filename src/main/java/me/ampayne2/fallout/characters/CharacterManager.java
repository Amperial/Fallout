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
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

/**
 * Manages all of the fallout Characters.
 */
public class CharacterManager {
    private final Fallout fallout;
    private Map<String, Character> charactersByOwner = new HashMap<>();
    private Map<String, Character> charactersByName = new HashMap<>();

    /**
     * Creates a new CharacterManager.
     *
     * @param fallout The {@link me.ampayne2.fallout.Fallout} instance.
     */
    public CharacterManager(Fallout fallout) {
        this.fallout = fallout;

        FileConfiguration characterConfig = fallout.getConfigManager().getConfig(ConfigType.CHARACTER);
        if (characterConfig.getConfigurationSection("Characters") != null) {
            for (String ownerName : characterConfig.getConfigurationSection("Characters").getKeys(false)) {
                if (!isOwner(ownerName)) {
                    addCharacter(new Character(fallout, characterConfig.getConfigurationSection("Characters." + ownerName)));
                }
            }
        }
    }

    /**
     * Creates a character and adds it to the manager.
     *
     * @param ownerName     The name of the character's owner.
     * @param characterName The character's name.
     * @return The Character.
     */
    public Character createCharacter(String ownerName, String characterName) {
        Validate.notNull(ownerName, "Cannot create a character with no owner");
        Validate.notNull(characterName, "Cannot create a character with no name");

        if (!charactersByOwner.containsKey(ownerName)) {
            Character character = new Character(fallout, ownerName, characterName);
            addCharacter(character);
            FileConfiguration characterConfig = fallout.getConfigManager().getConfig(ConfigType.CHARACTER);
            String path = "Characters." + ownerName;
            characterConfig.createSection(path);
            character.save(characterConfig.getConfigurationSection(path));
            fallout.getConfigManager().getConfigAccessor(ConfigType.CHARACTER).saveConfig();
            return character;
        }
        return null;
    }

    /**
     * Adds a character to the manager.
     *
     * @param character The character.
     */
    public void addCharacter(Character character) {
        charactersByOwner.put(character.getOwnerName().toLowerCase(), character);
        charactersByName.put(character.getCharacterName().toLowerCase(), character);
    }

    /**
     * Removes a character.
     *
     * @param character The character.
     */
    public void removeCharacter(Character character) {
        Validate.notNull(character, "Cannot remove a null character");

        fallout.getConfigManager().getConfig(ConfigType.CHARACTER).set("Characters." + character.getOwnerName(), null);
        fallout.getConfigManager().getConfigAccessor(ConfigType.CHARACTER).saveConfig();
        charactersByOwner.remove(character.getOwnerName());
        charactersByName.remove(character.getCharacterName());
    }

    /**
     * Checks if a player owns a character.
     *
     * @param ownerName The name of the player.
     * @return True if the player is an owner, else false.
     */
    public boolean isOwner(String ownerName) {
        Validate.notNull(ownerName, "Owner name cannot be null");

        return charactersByOwner.containsKey(ownerName.toLowerCase());
    }

    /**
     * Checks if the character of a certain name exists.
     *
     * @param characterName The character's name.
     * @return True if the character exists, else false.
     */
    public boolean isCharacter(String characterName) {
        Validate.notNull(characterName, "Character name cannot be null");

        return charactersByName.containsKey(characterName.toLowerCase());
    }

    /**
     * Gets the character owned by a player.
     *
     * @param ownerName The name of the player.
     * @return The character owned by the player.
     */
    public Character getCharacterByOwner(String ownerName) {
        Validate.notNull(ownerName, "Owner name cannot be null");

        return charactersByOwner.containsKey(ownerName.toLowerCase()) ? charactersByOwner.get(ownerName.toLowerCase()) : null;
    }

    /**
     * Gets the character of a given name.
     *
     * @param characterName The character's name.
     * @return The character with the given name.
     */
    public Character getCharacterByName(String characterName) {
        Validate.notNull(characterName, "Character name cannot be null");

        return charactersByName.containsKey(characterName.toLowerCase()) ? charactersByName.get(characterName.toLowerCase()) : null;
    }

    /**
     * Gets the characters in the manager.
     *
     * @return The characters in the manager.
     */
    public Collection<Character> getCharacters() {
        return charactersByOwner.values();
    }

    /**
     * Gets the map of owners and characters.
     *
     * @return The map of owners and characters.
     */
    public Map<String, Character> getCharactersByOwner() {
        return charactersByOwner;
    }

    /**
     * Gets the map of character names and characters.
     *
     * @return The map of character names and characters.
     */
    public Map<String, Character> getCharactersByName() {
        return charactersByName;
    }

    /**
     * Gets a string list of the Characters in the manager.
     *
     * @return A string list of all of the manager's Characters.
     */
    public List<String> getCharacterList() {
        return new ArrayList<>(charactersByName.keySet());
    }
}
