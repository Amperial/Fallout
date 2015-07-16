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
import ninja.amp.fallout.message.Messenger;
import ninja.amp.fallout.util.ArmorMaterial;
import ninja.amp.fallout.util.DamageType;
import ninja.amp.fallout.util.FOUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
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
    public void rollStandard(Player player, String value, Distance distance) {
        UUID playerId = player.getUniqueId();

        Messenger messenger = plugin.getMessenger();
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
                messenger.sendErrorMessage(player, FOMessage.ERROR_MODIFIERSYNTAX);
                return;
            }

            // Perform the roll
            Trait trait = Trait.fromName(rolling);
            int finalModifier;
            if (trait == null) {
                Skill skill = Skill.fromName(rolling);
                if (skill == null) {
                    messenger.sendErrorMessage(player, FOMessage.ROLL_CANTROLL, value);
                    return;
                }
                rolling = skill.getName();
                finalModifier = skillModifier(character, skill, modifier);
            } else {
                rolling = trait.getName();
                if (trait == Trait.STRENGTH && ArmorMaterial.isWearingFullSet(player)) {
                    ArmorMaterial material = ArmorMaterial.getArmorMaterial(player.getInventory().getHelmet().getType());
                    if (material == ArmorMaterial.DIAMOND) {
                        modifier += 2;
                    }
                }
                finalModifier = specialModifier(character, trait, modifier);
            }
            int roll = FOUtils.random(1, 20);
            int luck = character.getSpecial().get(Trait.LUCK);
            FOMessage result = getResult(roll, finalModifier, luck);
            String visualizer = getRollVisualizer(roll, finalModifier, luck);
            switch (distance) {
                case GLOBAL:
                    messenger.sendMessage(plugin.getServer(), FOMessage.ROLL_STANDARD_PUBLIC, character.getCharacterName(), rolling, modifier < 0 ? modifier : "+" + modifier, visualizer, result);
                    break;
                case LOCAL:
                    messenger.sendMessage(player.getLocation(), FOMessage.ROLL_STANDARD_PUBLIC, character.getCharacterName(), rolling, modifier < 0 ? modifier : "+" + modifier, visualizer, result);
                    break;
                case PRIVATE:
                    messenger.sendMessage(player, FOMessage.ROLL_STANDARD_PRIVATE, rolling, modifier < 0 ? modifier : "+" + modifier, visualizer, result);
                    break;
            }
        } else {
            messenger.sendErrorMessage(player, FOMessage.CHARACTER_NOTOWNER);
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

        Messenger messenger = plugin.getMessenger();
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
                messenger.sendErrorMessage(player, FOMessage.ERROR_MODIFIERSYNTAX);
                return;
            }

            // Perform the roll
            DamageType damageType = DamageType.fromName(rolling);
            if (damageType == null) {
                messenger.sendErrorMessage(player, FOMessage.COMMAND_USAGE, "/fo roll armor <damage type>[+/-modifier]");
                return;
            }
            boolean blocked;
            if (ArmorMaterial.isWearingFullSet(player)) {
                ArmorMaterial material = ArmorMaterial.getArmorMaterial(player.getInventory().getHelmet().getType());
                int damage = FOUtils.random(1, 6) + modifier;
                int defenseValue = material.getDefenseValue(damageType);
                blocked = defenseValue > 0 && damage <= defenseValue;
            } else {
                blocked = false;
            }

            FOMessage result = blocked ? FOMessage.RESULT_SUCCESS : FOMessage.RESULT_FAILURE;
            switch (distance) {
                case GLOBAL:
                    messenger.sendMessage(plugin.getServer(), FOMessage.ROLL_ARMOR_PUBLIC, character.getCharacterName(), damageType.getName(), result);
                    break;
                case LOCAL:
                    messenger.sendMessage(player.getLocation(), FOMessage.ROLL_ARMOR_PUBLIC, character.getCharacterName(), damageType.getName(), result);
                    break;
                case PRIVATE:
                    messenger.sendMessage(player, FOMessage.ROLL_ARMOR_PRIVATE, damageType.getName(), result);
            }
        } else {
            messenger.sendErrorMessage(player, FOMessage.CHARACTER_NOTOWNER);
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

        Messenger messenger = plugin.getMessenger();
        CharacterManager characterManager = plugin.getCharacterManager();
        if (characterManager.isOwner(playerId)) {
            Character character = characterManager.getCharacterByOwner(playerId);

            // Parse amount and sides from modifier if one exists
            int amount;
            int sides;
            int modifier = 0;
            try {
                String[] arg = value.split("d");
                amount = Math.max(1, Integer.parseInt(arg[0]));
                String[] arg2 = arg[1].split("\\+");
                if (arg2.length > 1) {
                    sides = Math.max(1, Integer.parseInt(arg2[0]));
                    modifier += Integer.parseInt(arg2[1]);
                } else {
                    arg2 = arg[1].split("-");
                    if (arg2.length > 1) {
                        sides = Math.max(1, Integer.parseInt(arg2[0]));
                        modifier -= Integer.parseInt(arg2[1]);
                    } else {
                        sides = Math.max(1, Integer.parseInt(arg[1]));
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                messenger.sendErrorMessage(player, FOMessage.COMMAND_USAGE, "/fo roll dice <amount>d<sides>[+/-modifier]");
                return;
            } catch (NumberFormatException e) {
                messenger.sendErrorMessage(player, FOMessage.ERROR_MODIFIERSYNTAX);
                return;
            }

            if ((distance == Distance.PRIVATE && amount > privateDiceLimit) || (distance != Distance.PRIVATE && amount > publicDiceLimit)) {
                messenger.sendErrorMessage(player, FOMessage.ROLL_DICEAMOUNT);
                return;
            }
            if (sides > diceSidesLimit) {
                messenger.sendErrorMessage(player, FOMessage.ROLL_DICESIDES);
                return;
            }

            // Perform the rolls
            Object[] rolls = new Object[amount];
            int total = modifier;
            for (int i = 0; i < amount; i++) {
                int roll = FOUtils.random(1, sides);
                rolls[i] = roll;
                total += roll;
            }
            String outcome = StringUtils.join(rolls, '+');

            switch (distance) {
                case GLOBAL:
                    messenger.sendMessage(plugin.getServer(), FOMessage.ROLL_DICE_PUBLIC, character.getCharacterName(), value, outcome, total);
                    break;
                case LOCAL:
                    messenger.sendMessage(player.getLocation(), FOMessage.ROLL_DICE_PUBLIC, character.getCharacterName(), value, outcome, total);
                    break;
                case PRIVATE:
                    messenger.sendMessage(player, FOMessage.ROLL_DICE_PRIVATE, value, outcome, total);
                    break;
            }
        } else {
            messenger.sendErrorMessage(player, FOMessage.CHARACTER_NOTOWNER);
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
        return (int) (character.getSpecial().get(trait) * 1.5) + modifier;
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
        return character.skillLevel(skill) + skill.getRollModifier(character.getSpecial()) + modifier;
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
    public FOMessage getResult(int roll, int modifier, int luck) {
        // 1 or 20 is always critical
        if (roll == 1) {
            return FOMessage.RESULT_CRITICALFAILURE;
        } else if (roll == 20) {
            return FOMessage.RESULT_CRITICALSUCCESS;
        }

        int nearSuccess = 18 - modifier;
        if (roll > nearSuccess) {
            // Check if result should be critical
            int criticals = Math.max(1, Math.min(4, luck - 5));
            if (roll + criticals > 20) {
                return FOMessage.RESULT_CRITICALSUCCESS;
            } else {
                return FOMessage.RESULT_SUCCESS;
            }
        } else if (roll >= nearSuccess) {
            return FOMessage.RESULT_NEARSUCCESS;
        } else {
            // Check if result should be critical
            int criticals = Math.max(1, 5 - luck);
            if (roll - criticals < 1) {
                return FOMessage.RESULT_CRITICALFAILURE;
            } else {
                return FOMessage.RESULT_FAILURE;
            }
        }
    }

    private String getRollVisualizer(int roll, int modifier, int luck) {
        int criticalFailures = Math.max(1, 5 - luck);
        int criticalSuccesses = Math.max(1, Math.min(4, luck - 5));
        int successThreshold = 17 - modifier;
        String[] symbols = new String[20];

        for (int i = 0; i < 20; ++i) {
            if (i == roll - 1) {
                symbols[i] = ChatColor.AQUA.toString();
            }
            if (i < criticalFailures) {
                if (symbols[i] == null) {
                    symbols[i] = ChatColor.DARK_RED.toString();
                }
                symbols[i] = symbols[i] + "\u2588";
            } else if (i > 19 - criticalSuccesses) {
                if (symbols[i] == null) {
                    symbols[i] = ChatColor.DARK_GREEN.toString();
                }
                symbols[i] = symbols[i] + "\u2588";
            } else if (i == successThreshold) {
                if (symbols[i] == null) {
                    symbols[i] = ChatColor.YELLOW.toString();
                }
                symbols[i] = symbols[i] + "\u2591";
            } else if (successThreshold < 17 && i > successThreshold && i < 18) {
                if (symbols[i] == null) {
                    symbols[i] = ChatColor.GREEN.toString();
                }
                symbols[i] = symbols[i] + "\u2592";
            } else if (successThreshold > 17 && i < successThreshold && i > 16) {
                if (symbols[i] == null) {
                    symbols[i] = ChatColor.RED.toString();
                }
                symbols[i] = symbols[i] + "\u2592";
            } else if (i < successThreshold) {
                if (symbols[i] == null) {
                    symbols[i] = ChatColor.RED.toString();
                }
                symbols[i] = symbols[i] + "\u2593";
            } else if (i > successThreshold) {
                if (symbols[i] == null) {
                    symbols[i] = ChatColor.GREEN.toString();
                }
                symbols[i] = symbols[i] + "\u2593";
            }
        }

        return StringUtils.join(symbols);
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
