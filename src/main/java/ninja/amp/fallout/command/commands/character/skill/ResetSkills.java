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
package ninja.amp.fallout.command.commands.character.skill;

import ninja.amp.fallout.Fallout;
import ninja.amp.fallout.character.CharacterManager;
import ninja.amp.fallout.character.Skill;
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
 * A command that resets a character's skill levels.
 *
 * @author Austin Payne
 */
public class ResetSkills extends Command {

    public ResetSkills(Fallout plugin) {
        super(plugin, "resetskills");
        setDescription("Resets your or another fallout character's skill levels.");
        setCommandUsage("/fo character resetskills [character]");
        setPermission(new Permission("fallout.character.resetskills", PermissionDefault.OP));
        setArgumentRange(0, 1);
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        Player player = (Player) sender;
        ninja.amp.fallout.character.Character character;
        CharacterManager characterManager = fallout.getCharacterManager();
        if (args.length == 1) {
            if (characterManager.isLoaded(args[0])) {
                character = characterManager.getCharacterByName(args[0]);
            } else {
                fallout.getMessenger().sendMessage(player, FOMessage.CHARACTER_DOESNTEXIST);
                return;
            }
        } else {
            if (characterManager.isOwner(player.getUniqueId())) {
                character = characterManager.getCharacterByOwner(player.getUniqueId());
            } else {
                fallout.getMessenger().sendMessage(player, FOMessage.CHARACTER_NOTOWNER);
                return;
            }
        }
        for (Skill skill : Skill.class.getEnumConstants()) {
            character.setSkillLevel(skill, 1);
        }
        characterManager.saveCharacter(character);
        fallout.getMessenger().sendMessage(player, FOMessage.SKILLS_RESET, character.getCharacterName());
        if (!character.getOwnerId().equals(player.getUniqueId())) {
            fallout.getMessenger().sendMessage(character, FOMessage.SKILLS_RESETTED);
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
