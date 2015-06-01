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
import ninja.amp.fallout.characters.Special;
import ninja.amp.fallout.characters.Trait;
import ninja.amp.fallout.menus.events.ItemClickEvent;
import ninja.amp.fallout.menus.items.StaticMenuItem;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpecialRemoveItem extends StaticMenuItem {
    private CharacterManager characterManager;
    private SpecialMenu menu;
    private Trait trait;

    private ItemStack disabledIcon;

    @SuppressWarnings("deprecation")
    public SpecialRemoveItem(Fallout plugin, SpecialMenu menu, Trait trait) {
        super("Remove Point",
                new ItemStack(Material.WOOL, 1, DyeColor.RED.getWoolData()),
                "Decreases the above",
                "trait by one point.");

        this.characterManager = plugin.getCharacterManager();
        this.menu = menu;
        this.trait = trait;

        disabledIcon = new ItemStack(Material.WOOL, 1, DyeColor.GRAY.getWoolData());
        setNameAndLore(disabledIcon, getDisplayName(), getLore());
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemStack getFinalIcon(Player player) {
        ItemStack finalIcon = super.getFinalIcon(player);

        Character character = characterManager.getCharacterByOwner(player.getUniqueId());
        Special special = menu.getPendingSpecial(character.getOwnerId());

        special.set(trait, special.get(trait) - 1);
        if (!character.getRace().isValid(special)) {
            finalIcon = disabledIcon;
        }
        special.set(trait, special.get(trait) + 1);

        return finalIcon;
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        Character character = characterManager.getCharacterByOwner(event.getPlayer().getUniqueId());
        Special special = menu.getPendingSpecial(character.getOwnerId());

        special.set(trait, special.get(trait) - 1);
        if (character.getRace().isValid(special)) {
            event.setWillUpdate(true);
        } else {
            special.set(trait, special.get(trait) + 1);
        }
    }
}
