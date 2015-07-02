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
import ninja.amp.fallout.character.Character;
import ninja.amp.fallout.character.CharacterManager;
import ninja.amp.fallout.command.Command;
import ninja.amp.fallout.command.CommandController;
import ninja.amp.fallout.message.FOMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * A command that upgrades the level of a fallout character.
 *
 * @author Austin Payne
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
        CharacterManager characterManager = fallout.getCharacterManager();
        if (characterManager.isLoaded(args[0])) {
            Character character = characterManager.getCharacterByName(args[0]);
            if (character.getLevel() > 4) {
                // Character is already max level
                fallout.getMessenger().sendMessage(player, FOMessage.CHARACTER_MAXLEVEL, character.getCharacterName());
            } else {
                // Upgrade level
                character.increaseLevel();

                // Save character to update level information
                characterManager.saveCharacter(character);

                fallout.getMessenger().sendMessage(player, FOMessage.CHARACTER_UPGRADE, character.getCharacterName(), character.getLevel());
                if (!character.getOwnerId().equals(player.getUniqueId())) {
                    fallout.getMessenger().sendMessage(character, FOMessage.CHARACTER_UPGRADED, character.getLevel());
                }
            }
        } else {
            fallout.getMessenger().sendMessage(player, FOMessage.CHARACTER_DOESNTEXIST);
        }
    }

    @Override
    public List<String> getTabCompleteList(String[] args) {
        if (args.length == 1) {
            if (args[0].isEmpty()) {
                return fallout.getCharacterManager().getCharacterList();
            } else {
                String arg = args[0].toLowerCase();
                List<String> modifiedList = new ArrayList<>();
                for (String suggestion : fallout.getCharacterManager().getCharacterList()) {
                    if (suggestion.toLowerCase().startsWith(arg)) {
                        modifiedList.add(suggestion);
                    }
                }
                if (modifiedList.isEmpty()) {
                    return fallout.getCharacterManager().getCharacterList();
                } else {
                    return modifiedList;
                }
            }
        } else {
            return CommandController.EMPTY_LIST;
        }
    }

}
