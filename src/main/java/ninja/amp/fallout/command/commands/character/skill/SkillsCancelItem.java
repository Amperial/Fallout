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
package ninja.amp.fallout.command.commands.character.skill;

import ninja.amp.fallout.menus.events.ItemClickEvent;
import ninja.amp.fallout.menus.items.StaticMenuItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SkillsCancelItem extends StaticMenuItem {
    private SkillsMenu menu;

    public SkillsCancelItem(SkillsMenu menu) {
        super(ChatColor.DARK_RED + "Cancel Skill Allocation",
                new ItemStack(Material.REDSTONE_BLOCK),
                "Cancels the current",
                "selection and exits",
                "the menu.");

        this.menu = menu;
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        menu.resetPendingSkills(event.getPlayer().getUniqueId());
        event.setWillClose(true);
    }
}
