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
package ninja.amp.fallout.menus.items.special;

import ninja.amp.fallout.Fallout;
import ninja.amp.fallout.characters.Character;
import ninja.amp.fallout.characters.Special;
import ninja.amp.fallout.config.ConfigType;
import ninja.amp.fallout.menus.events.ItemClickEvent;
import ninja.amp.fallout.menus.items.MenuItem;
import ninja.amp.fallout.message.FOMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class SpecialConfirmItem extends MenuItem {
    private Fallout plugin;
    private SpecialMenu menu;

    public SpecialConfirmItem(Fallout plugin, SpecialMenu menu) {
        super(ChatColor.GREEN + "Confirm SPECIAL Modification",
                new ItemStack(Material.EMERALD_BLOCK));

        this.plugin = plugin;
        this.menu = menu;
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        Character character = plugin.getCharacterManager().getCharacterByOwner(playerId);
        Special special = menu.getPendingSpecial(character);

        if (character.getRace().isValid(special)) {
            character.getSpecial().set(special);
            plugin.getMessenger().sendMessage(player, FOMessage.SPECIAL_SET, character.getCharacterName());
            character.save(plugin.getConfigManager().getConfig(ConfigType.CHARACTER).getConfigurationSection(character.getCharacterName()));
            plugin.getConfigManager().getConfigAccessor(ConfigType.CHARACTER).saveConfig();
        } else {
            plugin.getMessenger().sendMessage(player, FOMessage.SPECIAL_INVALID);
        }

        menu.clearPendingSpecial(character);
        event.setWillClose(true);
    }
}
