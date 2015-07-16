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
import ninja.amp.fallout.command.Command;
import ninja.amp.fallout.util.DamageType;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * A command that lets you roll to see if your armor can absorb a hit.
 *
 * @author Austin Payne
 */
public class ArmorRoll extends Command {

    public ArmorRoll(Fallout plugin) {
        super(plugin, "armor");
        setDescription("Rolls to see if your armor can absorb a hit.");
        setCommandUsage("/fo [global/private]roll armor <damage type>[+/-modifier]");
        setArgumentRange(1, 1);
    }

    @Override
    public void execute(String command, CommandSender sender, List<String> args) {
        // Command is only here to be shown in help.
        // Rolling is handled in GlobalRoll/LocalRoll/PrivateRoll commands
    }

    @Override
    public List<String> getTabCompleteList(List<String> args) {
        switch (args.size()) {
            case 1:
                return tabCompletions(args.get(0), DamageType.getDamageTypeNames());
            default:
                return EMPTY_LIST;
        }
    }

}
