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

import ninja.amp.fallout.FalloutCore;
import ninja.amp.fallout.character.Character;
import ninja.amp.fallout.message.FOMessage;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * A command that abandons the senders fallout character.
 *
 * @author Austin Payne
 */
public class Abandon extends CharacterCommand {

    public Abandon(FalloutCore fallout) {
        super(fallout, "abandon");
        setDescription("Abandons your breach character.");
        setCommandUsage("/bl character abandon");
        setPermission(new Permission("breach.character.abandon", PermissionDefault.OP));
    }

    @Override
    public void execute(String command, Player sender, Character character, List<String> args) {
        fallout.getCharacterManager().abandonCharacter(sender);
        fallout.getMessenger().sendMessage(sender, FOMessage.CHARACTER_ABANDON);
    }

}
