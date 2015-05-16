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
package ninja.amp.fallout.command.commands;

import ninja.amp.fallout.Fallout;
import ninja.amp.fallout.characters.Character;
import ninja.amp.fallout.characters.CharacterManager;
import ninja.amp.fallout.command.Command;
import ninja.amp.fallout.message.FOMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * A command that tells you the name of the owner of a given character.
 */
public class Whois extends Command {

    public Whois(Fallout plugin) {
        super(plugin, "whois");
        setDescription("Gives the name of the given character's owner.");
        setCommandUsage("/fo whois <character>");
        setPermission(new Permission("fallout.character.whois", PermissionDefault.TRUE));
        setArgumentRange(1, 1);
        setPlayerOnly(false);
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        Character character;
        CharacterManager characterManager = plugin.getCharacterManager();
        if (characterManager.isCharacter(args[0])) {
            character = characterManager.getCharacterByName(args[0]);
            plugin.getMessenger().sendMessage(sender, FOMessage.CHARACTER_NAME, character.getCharacterName(), character.getOwnerName());
        } else {
            plugin.getMessenger().sendMessage(sender, FOMessage.CHARACTER_DOESNTEXIST);
        }
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
