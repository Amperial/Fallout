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
package ninja.amp.fallout.command.commands.character;

import ninja.amp.fallout.Fallout;
import ninja.amp.fallout.characters.Character;
import ninja.amp.fallout.characters.CharacterManager;
import ninja.amp.fallout.command.Command;
import ninja.amp.fallout.message.FOMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that upgrades the level of a fallout character.
 */
public class Upgrade extends Command {

    public Upgrade(Fallout plugin) {
        super(plugin, "upgrade");
        setDescription("Upgrades the level of a fallout character.");
        setCommandUsage("/fo character upgrade <character>");
        setPermission(new Permission("fallout.character.upgrade", PermissionDefault.OP));
        setArgumentRange(1, 1);
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        Player player = (Player) sender;
        CharacterManager characterManager = plugin.getCharacterManager();
        if (characterManager.isLoaded(args[0])) {
            Character character = characterManager.getCharacterByName(args[0]);
            if (character.getLevel() > 4) {
                // Character is already max level
                plugin.getMessenger().sendMessage(player, FOMessage.CHARACTER_MAXLEVEL, character.getCharacterName());
            } else {
                // Upgrade level
                character.increaseLevel();

                // Save character to update level information
                characterManager.saveCharacter(character);

                plugin.getMessenger().sendMessage(player, FOMessage.CHARACTER_UPGRADE, character.getCharacterName(), character.getLevel());
                if (!character.getOwnerId().equals(player.getUniqueId())) {
                    plugin.getMessenger().sendMessage(character, FOMessage.CHARACTER_UPGRADED, character.getLevel());
                }
            }
        } else {
            plugin.getMessenger().sendMessage(player, FOMessage.CHARACTER_DOESNTEXIST);
        }
    }
}
