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

import ninja.amp.fallout.FalloutCore;
import ninja.amp.fallout.character.Character;
import ninja.amp.fallout.character.CharacterManager;
import ninja.amp.fallout.command.Command;
import ninja.amp.fallout.command.commands.character.special.SpecialMenu;
import ninja.amp.fallout.menu.ItemMenu;
import ninja.amp.fallout.message.FOMessage;
import ninja.amp.fallout.message.Messenger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * A command that creates a fallout character.
 *
 * @author Austin Payne
 */
public class Create extends Command {

    private ItemMenu createMenu;

    public Create(FalloutCore fallout, SpecialMenu specialMenu) {
        super(fallout, "create");
        setDescription("Creates a fallout character.");
        setCommandUsage("/fo character create <name> <age> <height in.> <weight lb.>");
        setPermission(new Permission("fallout.character.create", PermissionDefault.TRUE));
        setArgumentRange(4, 4);

        createMenu = new CreateMenu(fallout, specialMenu);
    }

    @Override
    public void execute(String command, CommandSender sender, List<String> args) {
        Player player = (Player) sender;
        String name = args.get(0);
        String age = args.get(1);
        String height = args.get(2);
        String weight = args.get(3);

        Messenger messenger = fallout.getMessenger();
        CharacterManager characterManager = fallout.getCharacterManager();

        if (characterManager.isOwner(player.getUniqueId())) {
            messenger.sendErrorMessage(player, FOMessage.CHARACTER_ALREADYEXISTS);
        } else {
            if (characterManager.isCharacter(name)) {
                messenger.sendErrorMessage(player, FOMessage.CHARACTER_NAMETAKEN);
            } else if (name.length() < 3 || name.length() > 20 || !name.matches("([A-Z][a-z]+_)?[A-Z][a-z]+")) {
                messenger.sendErrorMessage(player, FOMessage.ERROR_NAMEFORMAT);
            } else {
                Character.CharacterBuilder builder = new Character.CharacterBuilder(player);
                builder.name(name);
                try {
                    builder.age(Math.max(6, Integer.parseInt(age)))
                            .height(Math.max(36, Integer.parseInt(height)))
                            .weight(Math.max(72, Integer.parseInt(weight)));
                } catch (NumberFormatException e) {
                    messenger.sendErrorMessage(player, FOMessage.ERROR_NUMBERFORMAT);
                    return;
                }
                characterManager.addCharacterBuilder(player, builder);
                createMenu.open(player);
            }
        }
    }

}
