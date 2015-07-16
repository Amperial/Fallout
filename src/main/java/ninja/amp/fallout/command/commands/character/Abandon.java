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
import ninja.amp.fallout.character.CharacterManager;
import ninja.amp.fallout.command.Command;
import ninja.amp.fallout.message.FOMessage;
import ninja.amp.fallout.message.Messenger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * A command that abandons the senders fallout character.
 *
 * @author Austin Payne
 */
public class Abandon extends Command {

    public Abandon(FalloutCore fallout) {
        super(fallout, "abandon");
        setDescription("Abandons your fallout character.");
        setCommandUsage("/fo character abandon");
        setPermission(new Permission("fallout.character.abandon", PermissionDefault.OP));
    }

    @Override
    public void execute(String command, CommandSender sender, List<String> args) {
        Player player = (Player) sender;

        Messenger messenger = fallout.getMessenger();
        CharacterManager characterManager = fallout.getCharacterManager();
        if (characterManager.isOwner(player.getUniqueId())) {
            characterManager.abandonCharacter(player);
            messenger.sendMessage(player, FOMessage.CHARACTER_ABANDON);
        } else {
            messenger.sendErrorMessage(player, FOMessage.CHARACTER_NOTOWNER);
        }
    }

}
