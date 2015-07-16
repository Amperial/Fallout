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
package ninja.amp.fallout.command.commands.radio;

import ninja.amp.fallout.FalloutCore;
import ninja.amp.fallout.menu.ItemMenu;
import ninja.amp.fallout.menu.events.ItemClickEvent;
import ninja.amp.fallout.menu.items.StaticMenuItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * An inventory menu used for playing various minecraft records.
 *
 * @author Austin Payne
 */
public class RadioMenu extends ItemMenu {

    public RadioMenu(FalloutCore fallout) {
        super(ChatColor.AQUA + "Radio", Size.TWO_LINE, fallout);

        setItem(0, new RecordItem(new ItemStack(Material.GOLD_RECORD), "13"));
        setItem(1, new RecordItem(new ItemStack(Material.GREEN_RECORD), "cat"));
        setItem(2, new RecordItem(new ItemStack(Material.RECORD_3), "blocks"));
        setItem(3, new RecordItem(new ItemStack(Material.RECORD_4), "chirp"));
        setItem(4, new RecordItem(new ItemStack(Material.RECORD_5), "far"));
        setItem(5, new RecordItem(new ItemStack(Material.RECORD_6), "mall"));
        setItem(6, new RecordItem(new ItemStack(Material.RECORD_7), "mellohi"));
        setItem(7, new RecordItem(new ItemStack(Material.RECORD_8), "stal"));
        setItem(8, new RecordItem(new ItemStack(Material.RECORD_9), "strad"));
        setItem(9, new RecordItem(new ItemStack(Material.RECORD_10), "ward"));
        setItem(10, new RecordItem(new ItemStack(Material.RECORD_11), "11"));
        setItem(11, new RecordItem(new ItemStack(Material.RECORD_12), "wait"));
    }

    /**
     * A menu item used in the radio menu to play a certain minecraft record.
     */
    private class RecordItem extends StaticMenuItem {

        private String sound;

        public RecordItem(ItemStack icon, String sound) {
            super(icon.getItemMeta().getDisplayName(), icon);

            this.sound = "records." + sound;
        }

        @Override
        @SuppressWarnings("deprecation")
        public void onItemClick(ItemClickEvent event) {
            Player player = event.getPlayer();

            player.playSound(player.getLocation(), sound, Float.MAX_VALUE, 1.0F);
        }

    }

}
