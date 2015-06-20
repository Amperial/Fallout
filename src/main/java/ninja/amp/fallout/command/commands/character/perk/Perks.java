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

import ninja.amp.fallout.Fallout;
import ninja.amp.fallout.command.Command;
import ninja.amp.fallout.menus.ItemMenu;
import ninja.amp.fallout.message.FOMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.UUID;

/**
 * A command that lets the sender view and select their character's perks.
 */
public class Perks extends Command {
    private ItemMenu menu;

    public Perks(Fallout plugin) {
        super(plugin, "perks");
        setDescription("View and select your fallout character's perks.");
        setCommandUsage("/fo character perks");
        setPermission(new Permission("fallout.character.perks", PermissionDefault.TRUE));

        this.menu = new PerksMenu(plugin);
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        Player player = (Player) sender;
        UUID playerId = player.getUniqueId();
        if (plugin.getCharacterManager().isOwner(playerId)) {
            menu.open(player);
        } else {
            plugin.getMessenger().sendMessage(player, FOMessage.CHARACTER_NOTOWNER);
        }
    }
}