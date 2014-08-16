/*
 * This file is part of Fallout.
 *
 * Copyright (c) 2013-2014 <http://github.com/ampayne2/Fallout//>
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
package ninja.amp.fallout.characters;

import ninja.amp.fallout.Fallout;
import ninja.amp.fallout.config.ConfigType;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Manages all of the fallout Characters.
 */
public class CharacterManager {
    private Fallout fallout;
    private Map<UUID, Character> charactersByOwner = new HashMap<>();
    private Map<String, Character> charactersByName = new HashMap<>();

    /**
     * Creates a new {@link ninja.amp.fallout.characters.CharacterManager}.
     *
     * @param fallout The {@link ninja.amp.fallout.Fallout} instance.
     */
    public CharacterManager(Fallout fallout) {
        this.fallout = fallout;

        for (Player player : fallout.getServer().getOnlinePlayers()) {
            loadCharacter(player);
        }
    }

    /**
     * Loads a character if it exists.
     *
     * @param owner The character's owner.
     */
    public Character loadCharacter(Player owner) {
        UUID ownerId = owner.getUniqueId();
        FileConfiguration characterConfig = fallout.getConfigManager().getConfig(ConfigType.CHARACTER);
        if (!isOwner(ownerId)) {
            String oldPath = "Characters." + owner.getName();
            String newPath = "Characters." + ownerId;
            if (characterConfig.contains(oldPath)) {
                ConfigurationSection section = characterConfig.getConfigurationSection(oldPath);
                characterConfig.set(oldPath, null);
                characterConfig.set(newPath, section);
            }
            if (characterConfig.contains(newPath)) {
                Character character = new Character(fallout, characterConfig.getConfigurationSection(newPath));
                fallout.getConfigManager().getConfigAccessor(ConfigType.CHARACTER).saveConfig();
                return addCharacter(character);
            }
        }
        return null;
    }

    /**
     * Unloads a character.
     *
     * @param character The character to unload.
     */
    public void unloadCharacter(Character character) {
        charactersByOwner.remove(character.getOwnerId());
        charactersByName.remove(character.getCharacterName());
    }

    /**
     * Creates a character and adds it to the manager.
     *
     * @param owner         The character's owner.
     * @param characterName The character's name.
     * @return The Character.
     */
    public Character createCharacter(Player owner, String characterName, Race race) {
        Validate.notNull(owner, "Cannot create a character with no owner");
        Validate.notNull(characterName, "Cannot create a character with no name");

        if (!charactersByOwner.containsKey(owner.getUniqueId())) {
            Character character = new Character(fallout, owner, characterName, race);
            addCharacter(character);
            FileConfiguration characterConfig = fallout.getConfigManager().getConfig(ConfigType.CHARACTER);
            String path = "Characters." + owner.getUniqueId();
            characterConfig.createSection(path);
            character.save(characterConfig.getConfigurationSection(path));
            fallout.getConfigManager().getConfigAccessor(ConfigType.CHARACTER).saveConfig();

            if (fallout.getConfig().getBoolean("NicknamePlayers", true)) {
                fallout.getServer().dispatchCommand(fallout.getServer().getConsoleSender(), "nick " + owner.getName() + " " + characterName);
            }
            return character;
        }
        return null;
    }

    /**
     * Adds a character to the manager.
     *
     * @param character The character.
     */
    public Character addCharacter(Character character) {
        charactersByOwner.put(character.getOwnerId(), character);
        charactersByName.put(character.getCharacterName().toLowerCase(), character);
        return character;
    }

    /**
     * Removes a character.
     *
     * @param character The character.
     */
    public void removeCharacter(Character character) {
        Validate.notNull(character, "Cannot remove a null character");

        fallout.getConfigManager().getConfig(ConfigType.CHARACTER).set("Characters." + character.getOwnerId(), null);
        fallout.getConfigManager().getConfigAccessor(ConfigType.CHARACTER).saveConfig();
        charactersByOwner.remove(character.getOwnerId());
        charactersByName.remove(character.getCharacterName());

        if (fallout.getConfig().getBoolean("NicknamePlayers", true)) {
            fallout.getServer().dispatchCommand(fallout.getServer().getConsoleSender(), "nick " + character.getOwnerName() + " off");
        }
        character.destroy();
    }

    /**
     * Checks if a player owns a character.
     *
     * @param playerId The player's UUID.
     * @return True if the player is an owner, else false.
     */
    public boolean isOwner(UUID playerId) {
        Validate.notNull(playerId, "Owner cannot be null");

        return charactersByOwner.containsKey(playerId);
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
     * @param ownerId The owner's UUID.
     * @return The character owned by the player.
     */
    public Character getCharacterByOwner(UUID ownerId) {
        Validate.notNull(ownerId, "Owner name cannot be null");

        return charactersByOwner.containsKey(ownerId) ? charactersByOwner.get(ownerId) : null;
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
    public Map<UUID, Character> getCharactersByOwner() {
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

    /**
     * Destroys the {@link ninja.amp.fallout.characters.CharacterManager}.
     */
    public void destroy() {
        fallout = null;
        for (Character character : charactersByOwner.values()) {
            character.destroy();
        }
        charactersByOwner.clear();
        charactersByOwner = null;
        charactersByName.clear();
        charactersByName = null;
    }
}
