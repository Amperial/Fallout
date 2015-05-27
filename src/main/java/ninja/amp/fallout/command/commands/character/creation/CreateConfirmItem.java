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
import ninja.amp.fallout.characters.Character;
import ninja.amp.fallout.characters.Race;
import ninja.amp.fallout.command.commands.character.special.SpecialMenu;
import ninja.amp.fallout.menus.events.ItemClickEvent;
import ninja.amp.fallout.menus.items.SubMenuItem;
import ninja.amp.fallout.message.FOMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CreateConfirmItem extends SubMenuItem {
    private Fallout plugin;
    private CreateMenu createMenu;

    public CreateConfirmItem(Fallout plugin, CreateMenu createMenu, SpecialMenu specialMenu) {
        super(plugin, ChatColor.GREEN + "Confirm Character Options",
                new ItemStack(Material.EMERALD_BLOCK), specialMenu);

        this.plugin = plugin;
        this.createMenu = createMenu;
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        Character.Gender gender = createMenu.getGender(player);
        Race race = createMenu.getRace(player);
        Character.Alignment alignment = createMenu.getAlignment(player);
        if (gender == null || race == null || alignment == null) {
            plugin.getMessenger().sendMessage(player, FOMessage.ERROR_ALLOPTIONS);
            return;
        }
        createMenu.resetOptions(player);

        Character.CharacterBuilder builder = plugin.getCharacterManager().getCharacterBuilder(player);
        builder.gender(gender);
        builder.race(race);
        builder.alignment(alignment);
        plugin.getCharacterManager().createCharacter(player);
        plugin.getMessenger().sendMessage(player, FOMessage.CHARACTER_CREATE, builder.getName());
        super.onItemClick(event);
    }
}
