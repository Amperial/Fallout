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
import ninja.amp.fallout.characters.CharacterManager;
import ninja.amp.fallout.command.Command;
import ninja.amp.fallout.message.FOMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that possesses an unowned fallout character.
 *
 * @author Austin Payne
 */
public class Possess extends Command {

    public Possess(Fallout plugin) {
        super(plugin, "possess");
        setDescription("Possesses an unowned fallout character, abandoning your current.");
        setCommandUsage("/fo character possess <character>");
        setPermission(new Permission("fallout.character.possess", PermissionDefault.OP));
        setArgumentRange(1, 1);
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        Player player = (Player) sender;
        CharacterManager characterManager = plugin.getCharacterManager();
        if (characterManager.isCharacter(args[0])) {
            if (characterManager.canPossess(args[0])) {
                if (characterManager.isOwner(player.getUniqueId())) {
                    characterManager.abandonCharacter(player);
                    plugin.getMessenger().sendMessage(player, FOMessage.CHARACTER_ABANDON);
                }
                characterManager.possessCharacter(player, args[0]);
                plugin.getMessenger().sendMessage(player, FOMessage.CHARACTER_POSSESS, args[0]);
            } else {
                plugin.getMessenger().sendMessage(player, FOMessage.CHARACTER_ALREADYOWNED);
            }
        } else {
            plugin.getMessenger().sendMessage(player, FOMessage.CHARACTER_DOESNTEXIST);
        }
    }

}
