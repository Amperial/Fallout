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
package ninja.amp.fallout.characters;

import ninja.amp.fallout.Fallout;
import ninja.amp.fallout.config.ConfigManager;
import ninja.amp.fallout.config.ConfigType;
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
    private Fallout plugin;
    private Map<UUID, Character> charactersByOwner = new HashMap<>();
    private Map<String, Character> charactersByName = new HashMap<>();
    private Map<UUID, Character.CharacterBuilder> characterBuilders = new HashMap<>();

    /**
     * Creates a new {@link ninja.amp.fallout.characters.CharacterManager}.
     *
     * @param plugin The {@link ninja.amp.fallout.Fallout} instance.
     */
    public CharacterManager(Fallout plugin) {
        this.plugin = plugin;

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            loadCharacter(player);
        }
    }

    /**
     * Loads a player's character if currently owning one.
     *
     * @param owner The player.
     * @return The player's character.
     */
    public Character loadCharacter(Player owner) {
        ConfigManager configManager = plugin.getConfigManager();

        UUID ownerId = owner.getUniqueId();
        FileConfiguration playerConfig = configManager.getConfig(ConfigType.PLAYER);
        if (playerConfig.contains(ownerId.toString())) {
            // Find name of player's character
            String characterName = playerConfig.getString(ownerId.toString());

            // Load character from character config
            FileConfiguration characterConfig = configManager.getConfig(ConfigType.CHARACTER);
            Character character = new Character(characterConfig.getConfigurationSection(characterName));

            // Save loaded character to update any information
            character.save(characterConfig.getConfigurationSection(characterName));
            configManager.getConfigAccessor(ConfigType.CHARACTER).saveConfig();

            // Add character to manager
            return addToManager(character);
        }

        return null;
    }

    /**
     * Unloads a player's character if currently owning one.
     *
     * @param owner The player.
     */
    public void unloadCharacter(Player owner) {
        UUID ownerId = owner.getUniqueId();
        if (charactersByOwner.containsKey(ownerId)) {
            removeFromManager(charactersByOwner.get(ownerId));
        }
        if (characterBuilders.containsKey(ownerId)) {
            characterBuilders.remove(ownerId);
        }
    }

    /**
     * Adds a character to the manager.
     *
     * @param character The character.
     */
    private Character addToManager(Character character) {
        charactersByOwner.put(character.getOwnerId(), character);
        charactersByName.put(character.getCharacterName().toLowerCase(), character);
        return character;
    }

    /**
     * Removes a character from the manager.
     *
     * @param character The character.
     */
    private void removeFromManager(Character character) {
        charactersByOwner.remove(character.getOwnerId());
        charactersByName.remove(character.getCharacterName().toLowerCase());
    }

    /**
     * Creates a character and adds it to the manager.
     * Player must not already own a character.
     * Player must have a valid builder in the manager.
     *
     * @param owner The character's owner.
     * @return The Character.
     */
    public Character createCharacter(Player owner) {
        ConfigManager configManager = plugin.getConfigManager();

        // Create character from character builder and add to manager
        UUID ownerId = owner.getUniqueId();
        Character character = new Character(characterBuilders.get(ownerId));
        characterBuilders.remove(ownerId);
        addToManager(character);

        // Add owning player to players config
        FileConfiguration playerConfig = configManager.getConfig(ConfigType.PLAYER);
        playerConfig.set(ownerId.toString(), character.getCharacterName());
        configManager.getConfigAccessor(ConfigType.PLAYER).saveConfig();

        // Add character to character config
        FileConfiguration characterConfig = configManager.getConfig(ConfigType.CHARACTER);
        characterConfig.createSection(character.getCharacterName());
        character.save(characterConfig.getConfigurationSection(character.getCharacterName()));
        configManager.getConfigAccessor(ConfigType.CHARACTER).saveConfig();

        // Nickname player
        if (plugin.getConfig().getBoolean("NicknamePlayers", true)) {
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "nick " + owner.getName() + " " + character.getCharacterName());
        }

        return character;
    }

    /**
     * Deletes a character
     *
     * @param character The character.
     */
    public void deleteCharacter(Character character) {
        ConfigManager configManager = plugin.getConfigManager();

        // Remove character from character config
        configManager.getConfig(ConfigType.CHARACTER).set(character.getCharacterName(), null);
        configManager.getConfigAccessor(ConfigType.CHARACTER).saveConfig();

        // Remove owning player from players config
        configManager.getConfig(ConfigType.PLAYER).set(character.getOwnerId().toString(), null);
        configManager.getConfigAccessor(ConfigType.PLAYER).saveConfig();

        // Remove character from manager
        removeFromManager(character);

        // Remove nickname from player if set
        if (plugin.getConfig().getBoolean("NicknamePlayers", true)) {
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "nick " + character.getOwnerName() + " off");
        }
    }

    /**
     * Possesses a currently unowned character.
     * Character must exist in character config.
     * Player must not already be an owner.
     *
     * @param owner The character's new owner.
     * @param characterName The character's name.
     * @return The character.
     */
    public Character possessCharacter(Player owner, String characterName) {
        ConfigManager configManager = plugin.getConfigManager();

        // Load character from character config
        FileConfiguration characterConfig = configManager.getConfig(ConfigType.CHARACTER);
        Character character = new Character(characterConfig.getConfigurationSection(characterName));
        if (character.getOwnerName() == null) {
            // Possess character
            character.possess(owner);

            // Save loaded character to update owner information
            character.save(characterConfig.getConfigurationSection(characterName));
            configManager.getConfigAccessor(ConfigType.CHARACTER).saveConfig();

            // Add owning player to players config
            FileConfiguration playerConfig = configManager.getConfig(ConfigType.PLAYER);
            playerConfig.set(owner.getUniqueId().toString(), character.getCharacterName());
            configManager.getConfigAccessor(ConfigType.PLAYER).saveConfig();

            // Nickname player
            if (plugin.getConfig().getBoolean("NicknamePlayers", true)) {
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "nick " + owner.getName() + " " + character.getCharacterName());
            }

            // Add character to manager
            return addToManager(character);
        }

        return null;
    }

    /**
     * Separates a character from its owner.
     * Player must be the owner of the character.
     *
     * @param owner The character's owner.
     */
    public void abandonCharacter(Player owner) {
        ConfigManager configManager = plugin.getConfigManager();

        UUID ownerId = owner.getUniqueId();
        Character character = charactersByOwner.get(ownerId);

        // Remove character from manager
        removeFromManager(character);

        // Abandon character
        character.abandon();

        // Save character to update owner information
        FileConfiguration characterConfig = configManager.getConfig(ConfigType.CHARACTER);
        character.save(characterConfig.getConfigurationSection(character.getCharacterName()));
        configManager.getConfigAccessor(ConfigType.CHARACTER).saveConfig();

        // Remove owning player from players config
        configManager.getConfig(ConfigType.PLAYER).set(owner.getUniqueId().toString(), null);
        configManager.getConfigAccessor(ConfigType.PLAYER).saveConfig();

        // Remove nickname from player if set
        if (plugin.getConfig().getBoolean("NicknamePlayers", true)) {
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "nick " + character.getOwnerName() + " off");
        }
    }

    /**
     * Adds a character builder to the manager for creation.
     *
     * @param player The character's owner.
     * @param builder The character builder.
     */
    public void addCharacterBuilder(Player player, Character.CharacterBuilder builder) {
        characterBuilders.put(player.getUniqueId(), builder);
    }

    /**
     * Gets the character builder of a player.
     *
     * @param player The character's owner.
     * @return The character builder.
     */
    public Character.CharacterBuilder getCharacterBuilder(Player player) {
        return characterBuilders.get(player.getUniqueId());
    }

    /**
     * Checks if a player owns a character.
     *
     * @param playerId The player's UUID.
     * @return True if the player is an owner, else false.
     */
    public boolean isOwner(UUID playerId) {
        return charactersByOwner.containsKey(playerId);
    }

    /**
     * Checks if the character of a certain name exists.
     *
     * @param characterName The character's name.
     * @return True if the character exists, else false.
     */
    public boolean isCharacter(String characterName) {
        return charactersByName.containsKey(characterName.toLowerCase()) || plugin.getConfigManager().getConfig(ConfigType.CHARACTER).contains(characterName);
    }

    /**
     * Checks if the character of a certain name can be possessed.
     *
     * @param characterName The character's name.
     * @return True if the character can be possessed, else false.
     */
    public boolean canPossess(String characterName) {
        FileConfiguration characterConfig = plugin.getConfigManager().getConfig(ConfigType.CHARACTER);
        return characterConfig.contains(characterName) && !characterConfig.contains(characterName + ".ownerId");
    }

    /**
     * Gets the character owned by a player.
     *
     * @param ownerId The owner's UUID.
     * @return The character owned by the player.
     */
    public Character getCharacterByOwner(UUID ownerId) {
        return charactersByOwner.containsKey(ownerId) ? charactersByOwner.get(ownerId) : null;
    }

    /**
     * Gets the character of a given name. Character must have an online owner.
     *
     * @param characterName The character's name.
     * @return The character with the given name.
     */
    public Character getCharacterByName(String characterName) {
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
}
