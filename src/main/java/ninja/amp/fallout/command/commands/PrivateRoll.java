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
package ninja.amp.fallout.command.commands;

import ninja.amp.fallout.Fallout;
import ninja.amp.fallout.characters.Character;
import ninja.amp.fallout.characters.CharacterManager;
import ninja.amp.fallout.characters.Skill;
import ninja.amp.fallout.characters.Trait;
import ninja.amp.fallout.command.Command;
import ninja.amp.fallout.message.FOMessage;
import ninja.amp.fallout.utils.FOUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A command that lets you roll a trait or skill privately.
 */
public class PrivateRoll extends Command {
    private final List<String> tabCompleteList = new ArrayList<>();

    public PrivateRoll(Fallout plugin) {
        super(plugin, "privateroll");
        setDescription("Privately rolls the dice with one of your character's traits or skills.");
        setCommandUsage("/fo privateroll <trait/skill>[+/-modifier]");
        setPermission(new Permission("fallout.privateroll", PermissionDefault.TRUE));
        setArgumentRange(1, 1);
        tabCompleteList.addAll(Trait.getTraitNames());
        tabCompleteList.addAll(Skill.getSkillNames());
        tabCompleteList.add("dice");
        tabCompleteList.add("armor");
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        Player player = (Player) sender;
        UUID playerId = player.getUniqueId();
        CharacterManager characterManager = plugin.getCharacterManager();
        if (characterManager.isOwner(playerId)) {
            Character character = characterManager.getCharacterByOwner(playerId);

            // Parse trait/skill name from modifier if one exists
            String rolling = args[0];
            int modifier = 0;
            try {
                String[] arg = args[0].split("\\+");
                if (arg.length > 1) {
                    rolling = arg[0];
                    modifier += Integer.parseInt(arg[1]);
                } else {
                    arg = args[0].split("-");
                    if (arg.length > 1) {
                        rolling = arg[0];
                        modifier -= Integer.parseInt(arg[1]);
                    }
                }
            } catch (NumberFormatException e) {
                plugin.getMessenger().sendMessage(player, FOMessage.ERROR_MODIFIERSYNTAX);
            }

            // Perform the roll
            Trait trait = Trait.fromName(rolling);
            if (trait == null) {
                Skill skill = Skill.fromName(rolling);
                if (skill == null) {
                    plugin.getMessenger().sendMessage(player, FOMessage.ROLL_CANTROLL, args[0]);
                } else {
                    int roll = FOUtils.random(1, 20);
                    Roll.Result outcome = Roll.getResult(roll, Roll.skillModifier(character, skill, modifier), character.getSpecial().get(Trait.LUCK));
                    plugin.getMessenger().sendMessage(plugin.getServer(), FOMessage.ROLL_MESSAGE, roll, skill.getName(), modifier, outcome.getName());
                }
            } else {
                int roll = FOUtils.random(1, 20);
                Roll.Result outcome = Roll.getResult(roll, Roll.specialModifier(character, trait, modifier), character.getSpecial().get(Trait.LUCK));
                plugin.getMessenger().sendMessage(plugin.getServer(), FOMessage.ROLL_MESSAGE, roll, trait.getName(), modifier, outcome.getName());
            }
        } else {
            plugin.getMessenger().sendMessage(player, FOMessage.CHARACTER_NOTOWNER);
        }
    }

    @Override
    public List<String> getTabCompleteList(String[] args) {
        if (args.length == 0) {
            return tabCompleteList;
        } else {
            return new ArrayList<>();
        }
    }
}
