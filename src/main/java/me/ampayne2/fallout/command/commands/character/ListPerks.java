/*
 * This file is part of Fallout.
 *
 * Copyright (c) 2013-2013 <http://github.com/ampayne2/Fallout//>
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
package me.ampayne2.fallout.command.commands.character;

import me.ampayne2.fallout.Fallout;
import me.ampayne2.fallout.characters.Character;
import me.ampayne2.fallout.characters.CharacterManager;
import me.ampayne2.fallout.command.FOCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that lists the sender's character's perks.
 */
public class ListPerks extends FOCommand {
    private final Fallout fallout;

    public ListPerks(Fallout fallout) {
        super(fallout, "listperks", "Lists your or another fallout character's perks.", "/fo character listperks [character]", new Permission("fallout.character.listperks", PermissionDefault.TRUE), 0, 1, true);
        this.fallout = fallout;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Character character;
        CharacterManager characterManager = fallout.getCharacterManager();
        if (args.length == 1) {
            if (characterManager.isCharacter(args[0])) {
                character = characterManager.getCharacterByName(args[0]);
            } else {
                fallout.getMessenger().sendMessage(player, "error.character.other.doesntexist");
                return;
            }
        } else {
            if (characterManager.isOwner(player.getUniqueId())) {
                character = characterManager.getCharacterByOwner(player.getUniqueId());
            } else {
                fallout.getMessenger().sendMessage(player, "error.character.own.doesntexist");
                return;
            }
        }
        if (character.getPerks().isEmpty()) {
            fallout.getMessenger().sendMessage(player, "error.character.perks.noperks", character.getCharacterName());
        } else {
            fallout.getMessenger().sendMessage(player, "character.listperks", character.getCharacterName(), character.getPerkList());
        }
    }
}
