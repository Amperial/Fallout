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
import ninja.amp.fallout.characters.CharacterManager;
import ninja.amp.fallout.characters.Special;
import ninja.amp.fallout.characters.Trait;
import ninja.amp.fallout.menus.items.StaticMenuItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpecialItem extends StaticMenuItem {
    private CharacterManager characterManager;
    private SpecialMenu menu;
    private Trait trait;

    public SpecialItem(Fallout plugin, SpecialMenu menu, Trait trait, ItemStack icon, String... lore) {
        super(trait.getName(), icon, lore);

        this.characterManager = plugin.getCharacterManager();
        this.menu = menu;
        this.trait = trait;
    }

    @Override
    public ItemStack getFinalIcon(Player player) {
        ItemStack finalIcon = getIcon().clone();

        Special special = menu.getPendingSpecial(characterManager.getCharacterByOwner(player.getUniqueId()));
        finalIcon.setAmount(special.get(trait));

        return finalIcon;
    }
}
