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
package ninja.amp.fallout.command.commands.character.creation;

import ninja.amp.fallout.Fallout;
import ninja.amp.fallout.character.Character;
import ninja.amp.fallout.character.CharacterManager;
import ninja.amp.fallout.command.Command;
import ninja.amp.fallout.command.commands.character.special.SpecialMenu;
import ninja.amp.fallout.message.FOMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that creates a fallout character.
 *
 * @author Austin Payne
 */
public class Create extends Command {

    private CreateMenu createMenu;

    public Create(Fallout plugin, SpecialMenu specialMenu) {
        super(plugin, "create");
        setDescription("Creates a fallout character.");
        setCommandUsage("/fo character create <name> <age> <height in.> <weight lb.>");
        setPermission(new Permission("fallout.character.create", PermissionDefault.TRUE));
        setArgumentRange(4, 4);

        createMenu = new CreateMenu(plugin, specialMenu);
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        Player player = (Player) sender;

        CharacterManager characterManager = fallout.getCharacterManager();
        if (characterManager.isOwner(player.getUniqueId())) {
            fallout.getMessenger().sendMessage(player, FOMessage.CHARACTER_ALREADYEXISTS);
        } else {
            String characterName = args[0];
            if (characterManager.isCharacter(characterName)) {
                fallout.getMessenger().sendMessage(player, FOMessage.CHARACTER_NAMETAKEN);
            } else if (characterName.length() < 3 || characterName.length() > 20 || !characterName.matches("[a-zA-Z]*")) {
                fallout.getMessenger().sendMessage(player, FOMessage.ERROR_NAMEFORMAT);
            } else {
                Character.CharacterBuilder builder = new Character.CharacterBuilder(player);
                builder.name(characterName);
                try {
                    builder.age(Math.max(6, Integer.parseInt(args[1])))
                            .height(Math.max(36, Integer.parseInt(args[2])))
                            .weight(Math.max(72, Integer.parseInt(args[3])));
                } catch (NumberFormatException e) {
                    fallout.getMessenger().sendMessage(player, FOMessage.ERROR_NUMBERFORMAT);
                    return;
                }
                fallout.getCharacterManager().addCharacterBuilder(player, builder);
                createMenu.open(player);
            }
        }
    }

}
