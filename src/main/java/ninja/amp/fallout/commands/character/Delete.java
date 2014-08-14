/*
 * This file is part of Fallout.
 *
 * Copyright (c) 2013-2014 <http://github.com/ampayne2/Fallout//>
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
package ninja.amp.fallout.commands.character;

import ninja.amp.amplib.command.Command;
import ninja.amp.fallout.Fallout;
import ninja.amp.fallout.characters.CharacterManager;
import ninja.amp.fallout.message.FOMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that deletes the senders fallout character.
 */
public class Delete extends Command {
    private final Fallout fallout;

    public Delete(Fallout fallout) {
        super(fallout, "delete");
        setDescription("Deletes your fallout character.");
        setCommandUsage("/fo character delete");
        setPermission(new Permission("fallout.character.delete", PermissionDefault.TRUE));
        this.fallout = fallout;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        Player player = (Player) sender;
        CharacterManager characterManager = fallout.getCharacterManager();
        if (characterManager.isOwner(player.getUniqueId())) {
            characterManager.removeCharacter(characterManager.getCharacterByOwner(player.getUniqueId()));
            fallout.getMessenger().sendMessage(player, FOMessage.CHARACTER_DELETE);
        } else {
            fallout.getMessenger().sendMessage(player, FOMessage.CHARACTER_NOTOWNER);
        }
    }
}
