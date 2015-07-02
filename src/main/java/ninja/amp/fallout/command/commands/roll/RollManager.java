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
package ninja.amp.fallout.command.commands.roll;

import ninja.amp.fallout.Fallout;
import ninja.amp.fallout.character.Character;
import ninja.amp.fallout.character.CharacterManager;
import ninja.amp.fallout.character.Skill;
import ninja.amp.fallout.character.Trait;
import ninja.amp.fallout.message.FOMessage;
import ninja.amp.fallout.util.ArmorMaterial;
import ninja.amp.fallout.util.DamageType;
import ninja.amp.fallout.util.FOArmor;
import ninja.amp.fallout.util.FOUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Manages all dice, armor, skill, and trait rolling in fallout.
 *
 * @author Austin Payne
 */
public class RollManager {

    private Fallout plugin;
    private int publicDiceLimit;
    private int privateDiceLimit;
    private int diceSidesLimit;

    /**
     * Creates a new roll manager.
     *
     * @param plugin The fallout plugin instance
     */
    public RollManager(Fallout plugin) {
        this.plugin = plugin;

        FileConfiguration config = plugin.getConfig();
        publicDiceLimit = config.getInt("PublicDiceLimit", 15);
        privateDiceLimit = config.getInt("PrivateDiceLimit", 40);
        diceSidesLimit = config.getInt("DiceSidesLimit", 99);
    }

    /**
     * Rolls a skill or trait with an optional modifier.
     *
     * @param player   The player rolling
     * @param value    The roll parameters: <trait/skill>[+/-modifier]
     * @param distance The broadcast distance of the roll
     */
    public void rollDefault(Player player, String value, Distance distance) {
        UUID playerId = player.getUniqueId();
        CharacterManager characterManager = plugin.getCharacterManager();
        if (characterManager.isOwner(playerId)) {
            Character character = characterManager.getCharacterByOwner(playerId);

            // Parse trait/skill name from modifier if one exists
            String rolling = value;
            int modifier = 0;
            try {
                String[] arg = value.split("\\+");
                if (arg.length > 1) {
                    rolling = arg[0];
                    modifier += Integer.parseInt(arg[1]);
                } else {
                    arg = value.split("-");
                    if (arg.length > 1) {
                        rolling = arg[0];
                        modifier -= Integer.parseInt(arg[1]);
                    }
                }
            } catch (NumberFormatException e) {
                plugin.getMessenger().sendMessage(player, FOMessage.ERROR_MODIFIERSYNTAX);
                return;
            }

            // Perform the roll
            Trait trait = Trait.fromName(rolling);
            if (trait == null) {
                Skill skill = Skill.fromName(rolling);
                if (skill == null) {
                    plugin.getMessenger().sendMessage(player, FOMessage.ROLL_CANTROLL, value);
                } else {
                    int roll = FOUtils.random(1, 20);
                    Result outcome = getResult(roll, skillModifier(character, skill, modifier), character.getSpecial().get(Trait.LUCK));
                    switch (distance) {
                        case GLOBAL:
                            plugin.getMessenger().sendMessage(plugin.getServer(), FOMessage.ROLL_BROADCAST, character.getCharacterName(), roll, skill.getName(), modifier, outcome.getName());
                            break;
                        case LOCAL:
                            plugin.getMessenger().sendMessage(player.getLocation(), FOMessage.ROLL_BROADCAST, character.getCharacterName(), roll, skill.getName(), modifier, outcome.getName());
                            break;
                        case PRIVATE:
                            plugin.getMessenger().sendMessage(player, FOMessage.ROLL_MESSAGE, roll, skill.getName(), modifier, outcome.getName());
                            break;
                    }
                }
            } else {
                if (trait == Trait.STRENGTH && ArmorMaterial.isWearingFullSet(player)) {
                    FOArmor foArmor = ArmorMaterial.getArmorMaterial(player.getInventory().getHelmet().getType()).getFOVersion();
                    if (foArmor == FOArmor.POWER) {
                        modifier += 2;
                    }
                }
                int roll = FOUtils.random(1, 20);
                Result outcome = getResult(roll, specialModifier(character, trait, modifier), character.getSpecial().get(Trait.LUCK));
                switch (distance) {
                    case GLOBAL:
                        plugin.getMessenger().sendMessage(plugin.getServer(), FOMessage.ROLL_BROADCAST, character.getCharacterName(), roll, trait.getName(), modifier, outcome.getName());
                        break;
                    case LOCAL:
                        plugin.getMessenger().sendMessage(player.getLocation(), FOMessage.ROLL_BROADCAST, character.getCharacterName(), roll, trait.getName(), modifier, outcome.getName());
                        break;
                    case PRIVATE:
                        plugin.getMessenger().sendMessage(player, FOMessage.ROLL_MESSAGE, roll, trait.getName(), modifier, outcome.getName());
                        break;
                }
            }
        } else {
            plugin.getMessenger().sendMessage(player, FOMessage.CHARACTER_NOTOWNER);
        }
    }

    /**
     * Rolls to see if a player's armor can absorb a hit with an optional modifier.
     *
     * @param player   The player rolling
     * @param value    The roll parameters: <damage type>[+/-modifier]
     * @param distance The broadcast distance of the roll
     */
    public void rollArmor(Player player, String value, Distance distance) {
        UUID playerId = player.getUniqueId();
        CharacterManager characterManager = plugin.getCharacterManager();
        if (characterManager.isOwner(playerId)) {
            Character character = characterManager.getCharacterByOwner(playerId);

            // Parse damage type from modifier if one exists
            String rolling = value;
            int modifier = 0;
            try {
                String[] arg = value.split("\\+");
                if (arg.length > 1) {
                    rolling = arg[0];
                    modifier += Integer.parseInt(arg[1]);
                } else {
                    arg = value.split("-");
                    if (arg.length > 1) {
                        rolling = arg[0];
                        modifier -= Integer.parseInt(arg[1]);
                    }
                }
            } catch (NumberFormatException e) {
                plugin.getMessenger().sendMessage(player, FOMessage.ERROR_MODIFIERSYNTAX);
                return;
            }

            // Perform the roll
            DamageType damageType = DamageType.fromName(rolling);
            if (damageType == null) {
                plugin.getMessenger().sendMessage(player, FOMessage.COMMAND_USAGE, "/fo roll armor <damage type>[+/-modifier]");
                return;
            }
            boolean blocked;
            if (ArmorMaterial.isWearingFullSet(player)) {
                FOArmor foArmor = ArmorMaterial.getArmorMaterial(player.getInventory().getHelmet().getType()).getFOVersion();
                int roll = FOUtils.random(1, 6) + modifier;
                blocked = foArmor.canBlock(damageType, roll);
            } else {
                blocked = false;
            }
            if (blocked) {
                switch (distance) {
                    case GLOBAL:
                        plugin.getMessenger().sendMessage(plugin.getServer(), FOMessage.ROLL_ARMORBROADCAST, character.getCharacterName(), damageType.getName(), "Success");
                        break;
                    case LOCAL:
                        plugin.getMessenger().sendMessage(player.getLocation(), FOMessage.ROLL_ARMORBROADCAST, character.getCharacterName(), damageType.getName(), "Success");
                        break;
                    case PRIVATE:
                        plugin.getMessenger().sendMessage(player, FOMessage.ROLL_ARMORMESSAGE, damageType.getName(), "Success");
                        break;
                }
            } else {
                switch (distance) {
                    case GLOBAL:
                        plugin.getMessenger().sendMessage(plugin.getServer(), FOMessage.ROLL_ARMORBROADCAST, character.getCharacterName(), damageType.getName(), "Failure");
                        break;
                    case LOCAL:
                        plugin.getMessenger().sendMessage(player.getLocation(), FOMessage.ROLL_ARMORBROADCAST, character.getCharacterName(), damageType.getName(), "Failure");
                        break;
                    case PRIVATE:
                        plugin.getMessenger().sendMessage(player, FOMessage.ROLL_ARMORMESSAGE, damageType.getName(), "Failure");
                        break;
                }
            }
        } else {
            plugin.getMessenger().sendMessage(player, FOMessage.CHARACTER_NOTOWNER);
        }
    }

    /**
     * Manually rolls the dice with an optional modifier.
     *
     * @param player   The player rolling
     * @param value    The roll parameters: <amount>d<sides>[+/-modifier]
     * @param distance The broadcast distance of the roll
     */
    public void rollDice(Player player, String value, Distance distance) {
        UUID playerId = player.getUniqueId();
        CharacterManager characterManager = plugin.getCharacterManager();
        if (characterManager.isOwner(playerId)) {
            Character character = characterManager.getCharacterByOwner(playerId);

            // Parse amount and sides from modifier if one exists
            int amount;
            int sides;
            int modifier = 0;
            try {
                String[] arg = value.split("d");
                amount = Integer.parseInt(arg[0]);
                String[] arg2 = arg[1].split("\\+");
                if (arg2.length > 1) {
                    sides = Integer.parseInt(arg2[0]);
                    modifier += Integer.parseInt(arg2[1]);
                } else {
                    arg2 = arg[1].split("-");
                    if (arg2.length > 1) {
                        sides = Integer.parseInt(arg2[0]);
                        modifier -= Integer.parseInt(arg2[1]);
                    } else {
                        sides = Integer.parseInt(arg[1]);
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                plugin.getMessenger().sendMessage(player, FOMessage.COMMAND_USAGE, "/fo roll dice <amount>d<sides>[+/-modifier]");
                return;
            } catch (NumberFormatException e) {
                plugin.getMessenger().sendMessage(player, FOMessage.ERROR_MODIFIERSYNTAX);
                return;
            }

            if ((distance == Distance.PRIVATE && amount > privateDiceLimit) || (distance != Distance.PRIVATE && amount > publicDiceLimit)) {
                plugin.getMessenger().sendMessage(player, FOMessage.ROLL_DICEAMOUNT);
                return;
            }
            if (sides > diceSidesLimit) {
                plugin.getMessenger().sendMessage(player, FOMessage.ROLL_DICESIDES);
                return;
            }

            // Perform the rolls
            Object[] rolls = new Object[amount];
            int total = 0;
            for (int i = 0; i < amount; i++) {
                int roll = FOUtils.random(1, sides) + modifier;
                rolls[i] = roll;
                total += roll;
            }
            String outcome = StringUtils.join(rolls, '+');

            switch (distance) {
                case GLOBAL:
                    plugin.getMessenger().sendMessage(plugin.getServer(), FOMessage.ROLL_DICEBROADCAST, character.getCharacterName(), value, outcome, total);
                    break;
                case LOCAL:
                    plugin.getMessenger().sendMessage(player.getLocation(), FOMessage.ROLL_DICEBROADCAST, character.getCharacterName(), value, outcome, total);
                    break;
                case PRIVATE:
                    plugin.getMessenger().sendMessage(player, FOMessage.ROLL_DICEMESSAGE, value, outcome, total);
                    break;
            }
        } else {
            plugin.getMessenger().sendMessage(player, FOMessage.CHARACTER_NOTOWNER);
        }
    }

    /**
     * Gets the final modifier of a SPECIAL roll.<br>
     * {@code modifier = trait level + initial modifier}
     *
     * @param character The character rolling
     * @param trait     The trait being rolled
     * @param modifier  The optional initial modifier
     * @return The final modifier of the SPECIAL roll
     */
    public int specialModifier(Character character, Trait trait, int modifier) {
        return character.getSpecial().get(trait) + modifier;
    }

    /**
     * Gets the modifier of a skill roll.<br>
     * {@code modifier = skill level - 1 + average level of traits affected rounded up + initial modifier}
     *
     * @param character The character rolling
     * @param skill     The skill being rolled
     * @param modifier  The optional initial modifier
     * @return The final modifier of the skill roll
     */
    public int skillModifier(Character character, Skill skill, int modifier) {
        return character.skillLevel(skill) - 1 + skill.getRollModifier(character.getSpecial()) + modifier;
    }

    /**
     * Gets the result of a SPECIAL or skill roll.<br>
     * A roll of 1 or 20 always results in critical failure or success.<br>
     * Otherwise the algorithm to determine the result is as follows:
     * <pre>{@code
     *     near success = 18 - modifier
     *     if roll > near success
     *         criticals = max(1, min(4, luck - 5))
     *         if roll + criticals > 20 result is critical success
     *         else result is success
     *     else if roll >= near success
     *         result is near success
     *     else
     *         criticals = max(1, 5 - luck)
     *         if roll - criticals < 1 result is critical failure
     *         else result is failure
     * }</pre>
     *
     * @param roll     The roll value
     * @param modifier The roll modifier
     * @param luck     The luck value
     * @return The final result of the SPECIAL or skill roll
     */
    public Result getResult(int roll, int modifier, int luck) {
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
            if (roll + criticals > 20) {
                return Result.CRITICAL_SUCCESS;
            } else {
                return Result.SUCCESS;
            }
        } else if (roll >= nearSuccess) {
            return Result.NEAR_SUCCESS;
        } else {
            // Check if result should be critical
            int criticals = Math.max(1, 5 - luck);
            if (roll - criticals < 1) {
                return Result.CRITICAL_FAILURE;
            } else {
                return Result.FAILURE;
            }
        }
    }

    /**
     * Possible results of a roll.
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

    /**
     * Broadcast distance of a roll.
     */
    public enum Distance {
        GLOBAL,
        LOCAL,
        PRIVATE
    }

}
