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
import ninja.amp.fallout.message.FOMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * A command that lets you roll a trait or skill privately.
 */
public class PrivateRoll extends Command {
    private final List<String> tabCompleteList = new ArrayList<>();

    public PrivateRoll(Fallout plugin) {
        super(plugin, "privateroll");
        setVisible(false);
        setPermission(new Permission("fallout.privateroll", PermissionDefault.TRUE));
        setArgumentRange(1, 2);
        tabCompleteList.addAll(Trait.getTraitNames());
        tabCompleteList.addAll(Skill.getSkillNames());
        tabCompleteList.add("dice");
        tabCompleteList.add("armor");
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        switch (args[0]) {
            case "dice":
                if (args.length == 2) {
                    plugin.getRollManager().rollDice((Player) sender, args[1], RollManager.Distance.PRIVATE);
                } else {
                    plugin.getMessenger().sendMessage(sender, FOMessage.COMMAND_USAGE, "/fo privateroll dice <amount>d<sides>[+/-modifier]");
                }
                break;
            case "armor":
                if (args.length == 2) {
                    plugin.getRollManager().rollArmor((Player) sender, args[1], RollManager.Distance.PRIVATE);
                } else {
                    plugin.getMessenger().sendMessage(sender, FOMessage.COMMAND_USAGE, "/fo privateroll armor <damage type>[+/-modifier]");
                }
                break;
            default:
                plugin.getRollManager().rollDefault((Player) sender, args[0], RollManager.Distance.PRIVATE);
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
