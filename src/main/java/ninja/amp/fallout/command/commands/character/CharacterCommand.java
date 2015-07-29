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
import ninja.amp.fallout.character.CharacterManager;
import ninja.amp.fallout.command.Command;
import ninja.amp.fallout.message.FOMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * A command that requires the sender to own a character to execute.
 *
 * @author Austin Payne
 */
public abstract class CharacterCommand extends Command {

    public CharacterCommand(FalloutCore fallout, String name) {
        super(fallout, name);
    }

    @Override
    public void execute(String command, CommandSender sender, List<String> args) {
        Player player = (Player) sender;
        UUID playerId = player.getUniqueId();

        CharacterManager characterManager = fallout.getCharacterManager();

        if (characterManager.isOwner(playerId)) {
            execute(command, player, characterManager.getCharacterByOwner(playerId), args);
        } else {
            fallout.getMessenger().sendErrorMessage(player, FOMessage.CHARACTER_NOTOWNER);
        }
    }

    /**
     * The command executor.
     *
     * @param command   The command label
     * @param sender    The sender of the command
     * @param character The character owned by the sender
     * @param args      The arguments sent with the command
     */
    public abstract void execute(String command, Player sender, Character character, List<String> args);

}
