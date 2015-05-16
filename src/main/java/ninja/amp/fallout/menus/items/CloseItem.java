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
package ninja.amp.fallout.menus.items;

import ninja.amp.fallout.menus.events.ItemClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * A {@link ninja.amp.fallout.menus.items.StaticMenuItem} that closes the {@link ninja.amp.fallout.menus.ItemMenu}.
 */
public class CloseItem extends StaticMenuItem {

    public CloseItem() {
        super(ChatColor.RED + "Close", new ItemStack(Material.RECORD_4));
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        event.setWillClose(true);
    }
}
