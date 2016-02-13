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
package ninja.amp.fallout.command.commands.character.knowledge;

import ninja.amp.fallout.FalloutCore;
import ninja.amp.fallout.character.Character;
import ninja.amp.fallout.character.CharacterManager;
import ninja.amp.fallout.character.Information;
import ninja.amp.fallout.command.Command;
import ninja.amp.fallout.message.FOMessage;
import ninja.amp.fallout.message.Messenger;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * A command that teaches a fallout character a certain piece of information.
 *
 * @author Austin Payne
 */
public class Teach extends Command {

    public Teach(FalloutCore fallout) {
        super(fallout, "teach");
        setDescription("Teaches a fallout character secret information.");
        setCommandUsage("/fo character teach <character> <information>");
        setPermission(new Permission("fallout.character.teach", PermissionDefault.OP));
        setArgumentRange(2, 2);
        setPlayerOnly(false);
    }

    @Override
    public void execute(String command, CommandSender sender, List<String> args) {
        String name = args.get(0);
        String information = args.get(1);

        Messenger messenger = fallout.getMessenger();
        CharacterManager characterManager = fallout.getCharacterManager();

        if (characterManager.isLoaded(name)) {
            Character character = characterManager.getCharacterByName(name);
            if (Information.informationExists(information)) {
                information = Information.getInformationPiece(information);
                if (character.hasKnowledge(information)) {
                    messenger.sendErrorMessage(sender, FOMessage.INFORMATION_ALREADYTAUGHT, character.getCharacterName(), information);
                } else {
                    character.addKnowledge(information);

                    characterManager.saveCharacter(character);

                    messenger.sendMessage(sender, FOMessage.INFORMATION_TEACH, character.getCharacterName(), information);
                    messenger.sendMessage(character, FOMessage.INFORMATION_LEARN, information);
                }
            } else {
                messenger.sendErrorMessage(sender, FOMessage.INFORMATION_DOESNTEXIST, information);
            }
        } else {
            messenger.sendErrorMessage(sender, FOMessage.CHARACTER_DOESNTEXIST);
        }
    }

    @Override
    public List<String> tabComplete(List<String> args) {
        switch (args.size()) {
            case 1:
                return tabCompletions(args.get(0), fallout.getCharacterManager().getCharacterList());
            case 2:
                return tabCompletions(args.get(1), Information.getInformationPieces());
            default:
                return EMPTY_LIST;
        }
    }

}
