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
package ninja.amp.fallout.menus;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Allows you to set the {@link ninja.amp.fallout.menus.ItemMenu} that created the Inventory as the Inventory's holder.
 */
public class MenuHolder implements InventoryHolder {
    private ItemMenu menu;
    private Inventory inventory;

    public MenuHolder(ItemMenu menu) {
        this.menu = menu;
    }

    /**
     * Gets the {@link ninja.amp.fallout.menus.ItemMenu} holding the Inventory.
     *
     * @return The {@link ninja.amp.fallout.menus.ItemMenu} holding the Inventory.
     */
    public ItemMenu getMenu() {
        return menu;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}