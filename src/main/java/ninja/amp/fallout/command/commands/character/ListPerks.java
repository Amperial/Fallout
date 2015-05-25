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
import ninja.amp.fallout.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * A command that lists a character's perks.
 */
public class ListPerks extends Command {

    public ListPerks(Fallout fallout) {
        super(fallout, "listperks");
        setDescription("Lists your or another fallout character's perks.");
        setCommandUsage("/fo character listperks [character]");
        setPermission(new Permission("fallout.character.listperks", PermissionDefault.TRUE));
        setArgumentRange(0, 1);
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
//        Player player = (Player) sender;
//        Character character;
//        CharacterManager characterManager = plugin.getCharacterManager();
//        if (args.length == 1) {
//            if (characterManager.isCharacter(args[0])) {
//                character = characterManager.getCharacterByName(args[0]);
//            } else {
//                plugin.getMessenger().sendMessage(player, FOMessage.CHARACTER_DOESNTEXIST);
//                return;
//            }
//        } else {
//            if (characterManager.isOwner(player.getUniqueId())) {
//                character = characterManager.getCharacterByOwner(player.getUniqueId());
//            } else {
//                plugin.getMessenger().sendMessage(player, FOMessage.CHARACTER_NOTOWNER);
//                return;
//            }
//        }
//        if (character.getPerks().isEmpty()) {
//            plugin.getMessenger().sendMessage(player, FOMessage.PERK_NOPERKS, character.getCharacterName());
//        } else {
//            plugin.getMessenger().sendMessage(player, FOMessage.PERK_LIST, character.getCharacterName(), character.getPerkList());
//        }
    }

    @Override
    public List<String> getTabCompleteList(String[] args) {
        if (args.length == 0) {
            return plugin.getCharacterManager().getCharacterList();
        } else {
            return new ArrayList<>();
        }
    }
}
