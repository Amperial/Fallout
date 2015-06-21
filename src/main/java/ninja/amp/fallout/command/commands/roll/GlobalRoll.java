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
import ninja.amp.fallout.characters.Skill;
import ninja.amp.fallout.characters.Trait;
import ninja.amp.fallout.command.Command;
import ninja.amp.fallout.command.CommandController;
import ninja.amp.fallout.message.FOMessage;
import ninja.amp.fallout.utils.DamageType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * A command that lets you roll a trait or skill globally.
 *
 * @author Austin Payne
 */
public class GlobalRoll extends Command {

    private final List<String> tabCompleteList = new ArrayList<>();

    public GlobalRoll(Fallout plugin) {
        super(plugin, "globalroll");
        setVisible(false);
        setPermission(new Permission("fallout.globalroll", PermissionDefault.TRUE));
        setArgumentRange(1, 2);
        tabCompleteList.addAll(Trait.getTraitNames());
        tabCompleteList.addAll(Skill.getSkillNames());
        tabCompleteList.add("Dice");
        tabCompleteList.add("Armor");
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        switch (args[0].toLowerCase()) {
            case "dice":
                if (args.length == 2) {
                    plugin.getRollManager().rollDice((Player) sender, args[1], RollManager.Distance.GLOBAL);
                } else {
                    plugin.getMessenger().sendMessage(sender, FOMessage.COMMAND_USAGE, "/fo globalroll dice <amount>d<sides>[+/-modifier]");
                }
                break;
            case "armor":
                if (args.length == 2) {
                    plugin.getRollManager().rollArmor((Player) sender, args[1], RollManager.Distance.GLOBAL);
                } else {
                    plugin.getMessenger().sendMessage(sender, FOMessage.COMMAND_USAGE, "/fo globalroll armor <damage type>[+/-modifier]");
                }
                break;
            default:
                plugin.getRollManager().rollDefault((Player) sender, args[0], RollManager.Distance.GLOBAL);
        }
    }

    @Override
    public List<String> getTabCompleteList(String[] args) {
        if (args.length == 1) {
            if (args[0].isEmpty()) {
                return tabCompleteList;
            } else {
                String arg = args[0].toLowerCase();
                List<String> modifiedList = new ArrayList<>();
                for (String suggestion : tabCompleteList) {
                    if (suggestion.toLowerCase().startsWith(arg)) {
                        modifiedList.add(suggestion);
                    }
                }
                if (modifiedList.isEmpty()) {
                    return tabCompleteList;
                } else {
                    return modifiedList;
                }
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("armor")) {
            if (args[1].isEmpty()) {
                return DamageType.getDamageTypeNames();
            } else {
                String arg = args[1].toLowerCase();
                List<String> modifiedList = new ArrayList<>();
                for (String suggestion : DamageType.getDamageTypeNames()) {
                    if (suggestion.toLowerCase().startsWith(arg)) {
                        modifiedList.add(suggestion);
                    }
                }
                if (modifiedList.isEmpty()) {
                    return DamageType.getDamageTypeNames();
                } else {
                    return modifiedList;
                }
            }
        } else {
            return CommandController.EMPTY_LIST;
        }
    }

}
