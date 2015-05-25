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
 * A command that lets you roll a trait or skill.
 */
public class Roll extends Command {
    private final List<String> tabCompleteList = new ArrayList<>();

    public Roll(Fallout plugin) {
        super(plugin, "roll");
        setDescription("Rolls the dice with one of your character's traits or skills.");
        setCommandUsage("/fo roll <trait/skill>[+/-modifier]");
        setPermission(new Permission("fallout.roll", PermissionDefault.TRUE));
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
                    Result outcome = getResult(roll, skillModifier(character, skill, modifier), character.getSpecial().get(Trait.LUCK));
                    plugin.getMessenger().sendMessage(plugin.getServer(), FOMessage.ROLL_BROADCAST, character.getCharacterName(), roll, skill.getName(), modifier, outcome.getName());
                }
            } else {
                int roll = FOUtils.random(1, 20);
                Result outcome = getResult(roll, specialModifier(character, trait, modifier), character.getSpecial().get(Trait.LUCK));
                plugin.getMessenger().sendMessage(plugin.getServer(), FOMessage.ROLL_BROADCAST, character.getCharacterName(), roll, trait.getName(), modifier, outcome.getName());
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

    public static int rollDice(int dice, int sides, int modifier) {
        return FOUtils.random(1, sides) + modifier;
    }

    public static int specialModifier(Character character, Trait trait, int modifier) {
        return character.getSpecial().get(trait) + modifier;
    }

    public static int skillModifier(Character character, Skill skill, int modifier) {
        return character.skillLevel(skill) + skill.getRollModifier(character.getSpecial()) + modifier;
    }

    public static Result getResult(int roll, int modifier, int luck) {
        // 1 or 20 is always critical
        if (roll == 1) {
            return Result.CRITICAL_FAILURE;
        } else if (roll == 20) {
            return Result.CRITICAL_SUCCESS;
        }

        int nearSuccess = 18 - modifier;
        if (roll > nearSuccess) {
            // Check if result should be critical
            int criticals = Math.max(1, Math.min(4, luck - 5));
            if (roll >= criticals) {
                return Result.CRITICAL_SUCCESS;
            } else {
                return Result.SUCCESS;
            }
        } else if (roll >= nearSuccess) {
            return Result.NEAR_SUCCESS;
        } else {
            // Check if result should be critical
            int criticals = Math.max(1, 5 - luck);
            if (roll <= criticals) {
                return Result.CRITICAL_FAILURE;
            } else {
                return Result.FAILURE;
            }
        }
    }

    /**
     * Results of a roll.
     */
    public enum Result {
        CRITICAL_FAILURE("Critical Failure"),
        FAILURE("Failure"),
        NEAR_SUCCESS("Near Success"),
        SUCCESS("Success"),
        CRITICAL_SUCCESS("Critical Success");

        private final String name;

        private Result(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
