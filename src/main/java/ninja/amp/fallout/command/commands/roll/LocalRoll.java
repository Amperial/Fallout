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

import ninja.amp.fallout.FalloutCore;
import ninja.amp.fallout.character.Character;
import ninja.amp.fallout.character.Skill;
import ninja.amp.fallout.character.Trait;
import ninja.amp.fallout.command.commands.character.CharacterCommand;
import ninja.amp.fallout.message.FOMessage;
import ninja.amp.fallout.message.Messenger;
import ninja.amp.fallout.util.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * A command that lets you roll a trait or skill locally.
 *
 * @author Austin Payne
 */
public class LocalRoll extends CharacterCommand {

    private final List<String> tabCompleteList = new ArrayList<>();

    public LocalRoll(FalloutCore fallout) {
        super(fallout, "roll");
        setDescription("Rolls the dice with one of your character's traits or skills.");
        setCommandUsage("/fo [global/private]roll <trait/skill>[+/-modifier]");
        setPermission(new Permission("fallout.roll", PermissionDefault.TRUE));
        setArgumentRange(1, 2);

        tabCompleteList.addAll(Trait.getTraitNames());
        tabCompleteList.addAll(Skill.getSkillNames());
        tabCompleteList.add("Dice");
        tabCompleteList.add("Armor");
    }

    @Override
    public void execute(String command, Player sender, Character character, List<String> args) {
        String target = args.get(0);

        Messenger messenger = fallout.getMessenger();
        RollManager rollManager = fallout.getRollManager();
        switch (target.toLowerCase()) {
            case "dice":
                if (args.size() == 2) {
                    rollManager.rollDice(sender, character, args.get(1), RollManager.Distance.LOCAL);
                } else {
                    messenger.sendErrorMessage(sender, FOMessage.COMMAND_USAGE, "/fo roll dice <amount>d<sides>[+/-modifier]");
                }
                break;
            case "armor":
                if (args.size() == 2) {
                    rollManager.rollArmor(sender, character, args.get(1), RollManager.Distance.LOCAL);
                } else {
                    messenger.sendErrorMessage(sender, FOMessage.COMMAND_USAGE, "/fo roll armor <damage type>[+/-modifier]");
                }
                break;
            default:
                rollManager.rollStandard(sender, character, target, RollManager.Distance.LOCAL);
        }
    }

    @Override
    public List<String> getTabCompleteList(List<String> args) {
        switch (args.size()) {
            case 1:
                return tabCompletions(args.get(0), tabCompleteList);
            case 2:
                if (args.get(0).equalsIgnoreCase("armor")) {
                    return tabCompletions(args.get(1), DamageType.getDamageTypeNames());
                }
            default:
                return EMPTY_LIST;
        }
    }

}
