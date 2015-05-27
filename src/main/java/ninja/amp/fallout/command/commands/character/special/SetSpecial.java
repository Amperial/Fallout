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
package ninja.amp.fallout.command.commands.character.special;

import ninja.amp.fallout.Fallout;
import ninja.amp.fallout.characters.Character;
import ninja.amp.fallout.characters.CharacterManager;
import ninja.amp.fallout.command.Command;
import ninja.amp.fallout.menus.ItemMenu;
import ninja.amp.fallout.message.FOMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.UUID;

/**
 * A command that sets the sender's character's SPECIAL traits.
 */
public class SetSpecial extends Command {
    private ItemMenu menu;

    public SetSpecial(Fallout plugin, SpecialMenu menu) {
        super(plugin, "setspecial");
        setDescription("Sets your fallout character's traits.");
        setCommandUsage("/fo character setspecial");
        setPermission(new Permission("fallout.character.setspecial", PermissionDefault.TRUE));

        this.menu = menu;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        Player player = (Player) sender;
        UUID playerId = player.getUniqueId();
        CharacterManager characterManager = plugin.getCharacterManager();
        if (characterManager.isOwner(playerId)) {
            Character character = characterManager.getCharacterByOwner(playerId);
            menu.open(player);
        } else {
            plugin.getMessenger().sendMessage(player, FOMessage.CHARACTER_NOTOWNER);
        }
    }
}
