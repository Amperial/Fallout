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
package ninja.amp.fallout.command.commands.character.perk;

import ninja.amp.fallout.FalloutCore;
import ninja.amp.fallout.command.Command;
import ninja.amp.fallout.menu.ItemMenu;
import ninja.amp.fallout.message.FOMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * A command that lets the sender view and select their character's perks.
 *
 * @author Austin Payne
 */
public class Perks extends Command {

    private ItemMenu menu;

    public Perks(FalloutCore fallout) {
        super(fallout, "perks");
        setDescription("View and select your fallout character's perks.");
        setCommandUsage("/fo character perks");
        setPermission(new Permission("fallout.character.perks", PermissionDefault.TRUE));

        this.menu = new PerksMenu(fallout);
    }

    @Override
    public void execute(String command, CommandSender sender, List<String> args) {
        Player player = (Player) sender;

        if (fallout.getCharacterManager().isOwner(player.getUniqueId())) {
            menu.open(player);
        } else {
            fallout.getMessenger().sendErrorMessage(player, FOMessage.CHARACTER_NOTOWNER);
        }
    }

}
