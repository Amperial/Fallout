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
import me.ampayne2.fallout.characters.CharacterManager;
import me.ampayne2.fallout.command.FOCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that creates a fallout character.
 */
public class Create extends FOCommand {
    private final Fallout fallout;

    public Create(Fallout fallout) {
        super(fallout, "create", "Creates a fallout character.", "/fo character create <name>", new Permission("fallout.character.create", PermissionDefault.TRUE), 1, true);
        this.fallout = fallout;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        Player player = (Player) sender;
        CharacterManager characterManager = fallout.getCharacterManager();
        if (characterManager.isOwner(player.getUniqueId())) {
            fallout.getMessenger().sendMessage(player, "error.character.own.alreadyexists");
        } else {
            String characterName = args[0];
            if (characterManager.isCharacter(characterName)) {
                fallout.getMessenger().sendMessage(player, "error.character.other.alreadyexists");
            } else {
                characterManager.createCharacter(player, characterName);
                fallout.getMessenger().sendMessage(player, "character.create", characterName);
            }
        }
    }
}
