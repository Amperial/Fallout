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
import ninja.amp.fallout.menus.ItemMenu;
import ninja.amp.fallout.menus.items.groups.EnumOption;
import ninja.amp.fallout.menus.items.groups.EnumOptionItem;
import ninja.amp.fallout.menus.items.groups.Option;
import ninja.amp.fallout.menus.items.groups.OptionItem;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Inventory menu used for creating a character.
 */
public class CreateMenu extends ItemMenu {
    private EnumOption<Character.Gender> gender;
    private EnumOption<Race> race;
    private Option conformity;
    private Option morality;

    @SuppressWarnings("deprecation")
    public CreateMenu(Fallout plugin, SpecialMenu specialMenu) {
        super("Character Options", ItemMenu.Size.FIVE_LINE, plugin);

        setItem(41, new CreateConfirmItem(plugin, this, specialMenu));
        setItem(39, new CreateCancelItem(plugin));

        ItemStack selected = new ItemStack(Material.WOOL, 1, DyeColor.LIME.getWoolData());
        ItemStack unselected = new ItemStack(Material.WOOL, 1, DyeColor.GRAY.getWoolData());

        gender = new EnumOption<>();
        setItem(1, new EnumOptionItem<>(gender, Character.Gender.MALE, "Male", selected, unselected));
        setItem(2, new EnumOptionItem<>(gender, Character.Gender.FEMALE, "Female", selected, unselected));

        race = new EnumOption<>();
        setItem(4, new EnumOptionItem<>(race, Race.WASTELANDER, "Wastelander", selected, unselected));
        setItem(5, new EnumOptionItem<>(race, Race.GHOUL, "Ghoul", selected, unselected));
        setItem(6, new EnumOptionItem<>(race, Race.SUPER_MUTANT, "Super Mutant", selected, unselected));
        setItem(7, new EnumOptionItem<>(race, Race.VAULT_DWELLER, "Vault Dweller", selected, unselected));

        conformity = new Option();
        setItem(19, new OptionItem(conformity, "Lawful", selected, unselected));
        setItem(20, new OptionItem(conformity, "Balanced", selected, unselected));
        setItem(21, new OptionItem(conformity, "Chaotic", selected, unselected));

        morality = new Option();
        setItem(23, new OptionItem(morality, "Good", selected, unselected));
        setItem(24, new OptionItem(morality, "Neutral", selected, unselected));
        setItem(25, new OptionItem(morality, "Evil", selected, unselected));

        fillEmptySlots();
    }

    /**
     * Gets the currently selected gender of a player.
     *
     * @param player The player.
     * @return The selected gender option.
     */
    public Character.Gender getGender(Player player) {
        return gender.getSelected(player).getEnum();
    }

    /**
     * Gets the currently selected race of a player.
     *
     * @param player The player.
     * @return The selected race option.
     */
    public Race getRace(Player player) {
        return race.getSelected(player).getEnum();
    }

    /**
     * Gets the currently selected alignment of a player.
     *
     * @param player The player.
     * @return The selected alignment option.
     */
    public Character.Alignment getAlignment(Player player) {
        OptionItem conformity = this.conformity.getSelected(player);
        OptionItem morality = this.morality.getSelected(player);
        if (conformity == null || morality == null) {
            return null;
        }

        String alignment = "";
        if (!conformity.getDisplayName().equals("Balanced")) {
            alignment += conformity.getDisplayName().toUpperCase() + "_";
        }
        alignment += morality.getDisplayName().toUpperCase();

        return Character.Alignment.valueOf(alignment);
    }

    /**
     * Resets a player's selected options.
     *
     * @param player The player.
     */
    public void resetOptions(Player player) {
        gender.removeSelected(player);
        race.removeSelected(player);
        conformity.removeSelected(player);
        morality.removeSelected(player);
    }
}
