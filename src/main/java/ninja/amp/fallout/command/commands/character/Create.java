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
import ninja.amp.fallout.characters.Race;
import ninja.amp.fallout.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * A command that creates a fallout character.
 */
public class Create extends Command {

    public Create(Fallout plugin) {
        super(plugin, "create");
        setDescription("Creates a fallout character.");
        setCommandUsage("/fo character create <name> <age> <height in.> <weight lb.>");
        setPermission(new Permission("fallout.character.create", PermissionDefault.TRUE));
        setArgumentRange(4, 4);
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
//        Player player = (Player) sender;
//        CharacterManager characterManager = plugin.getCharacterManager();
//        if (characterManager.isOwner(player.getUniqueId())) {
//            plugin.getMessenger().sendMessage(player, FOMessage.CHARACTER_ALREADYEXISTS);
//        } else {
//            String characterName = args[0];
//            if (characterManager.isCharacter(characterName)) {
//                plugin.getMessenger().sendMessage(player, FOMessage.CHARACTER_NAMETAKEN);
//            } else {
//                Race race = Race.fromName(args[1]);
//                if (race == null) {
//                    plugin.getMessenger().sendMessage(player, FOMessage.RACE_DOESNTEXIST, args[1]);
//                } else {
//                    characterManager.createCharacter(player, characterName, race);
//                    plugin.getMessenger().sendMessage(player, FOMessage.CHARACTER_CREATE, characterName);
//                }
//            }
//        }
    }

    @Override
    public List<String> getTabCompleteList(String[] args) {
        if (args.length == 1) {
            return Race.getRaceNames();
        } else {
            return new ArrayList<>();
        }
    }
}
