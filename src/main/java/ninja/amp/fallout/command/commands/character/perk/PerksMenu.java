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
package ninja.amp.fallout.command.commands.character.perk;

import ninja.amp.fallout.Fallout;
import ninja.amp.fallout.characters.Character;
import ninja.amp.fallout.characters.CharacterManager;
import ninja.amp.fallout.characters.Perk;
import ninja.amp.fallout.menus.ItemMenu;
import ninja.amp.fallout.menus.events.ItemClickEvent;
import ninja.amp.fallout.menus.items.groups.EnumOption;
import ninja.amp.fallout.menus.items.groups.EnumOptionItem;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Inventory menu used for creating a character.
 */
public class PerksMenu extends ItemMenu {
    private CharacterManager characterManager;
    private Map<Integer, EnumOption<Perk>> tiers = new HashMap<>();

    @SuppressWarnings("deprecation")
    public PerksMenu(Fallout plugin) {
        super("Perk Selection", Size.SIX_LINE, plugin);

        this.characterManager = plugin.getCharacterManager();

        setItem(50, new PerksConfirmItem(plugin, this));
        setItem(48, new PerksCancelItem(this));

        ItemStack selected = new ItemStack(Material.WOOL, 1, DyeColor.LIME.getWoolData());
        ItemStack unselected = new ItemStack(Material.WOOL, 1, DyeColor.GRAY.getWoolData());

        EnumOption<Perk> tierOne = new EnumOption<>();
        setItem(0, new PerkOptionItem(plugin, tierOne, Perk.SIGHT_ADAPT, selected, unselected));
        setItem(9, new PerkOptionItem(plugin, tierOne, Perk.ANTI_COLD, selected, unselected));
        setItem(18, new PerkOptionItem(plugin, tierOne, Perk.INCREASED_JUMP, selected, unselected));
        setItem(27, new PerkOptionItem(plugin, tierOne, Perk.INCREASED_STEALTH, selected, unselected));
        setItem(36, new PerkOptionItem(plugin, tierOne, Perk.ENERGY_REPLENISH, selected, unselected));

        EnumOption<Perk> tierTwo = new EnumOption<>();
        setItem(2, new PerkOptionItem(plugin, tierTwo, Perk.SIGHT_DISTANCE, selected, unselected));
        setItem(11, new PerkOptionItem(plugin, tierTwo, Perk.INCREASED_STRENGTH, selected, unselected));
        setItem(20, new PerkOptionItem(plugin, tierTwo, Perk.INCREASED_SURVIVAL, selected, unselected));
        setItem(29, new PerkOptionItem(plugin, tierTwo, Perk.INCREASED_ENDURANCE, selected, unselected));
        setItem(38, new PerkOptionItem(plugin, tierTwo, Perk.INCREASED_AIM, selected, unselected));

        EnumOption<Perk> tierThree = new EnumOption<>();
        setItem(4, new PerkOptionItem(plugin, tierThree, Perk.DECREASED_BLEEDING, selected, unselected));
        setItem(13, new PerkOptionItem(plugin, tierThree, Perk.ANTI_RADIATION, selected, unselected));
        setItem(22, new PerkOptionItem(plugin, tierThree, Perk.INCREASED_SENSES, selected, unselected));
        setItem(31, new PerkOptionItem(plugin, tierThree, Perk.PERFECT_MEMORY, selected, unselected));
        setItem(40, new PerkOptionItem(plugin, tierThree, Perk.FEIGN_DEATH, selected, unselected));

        EnumOption<Perk> tierFour = new EnumOption<>();
        setItem(6, new PerkOptionItem(plugin, tierFour, Perk.INCREASED_LOGIC, selected, unselected));
        setItem(15, new PerkOptionItem(plugin, tierFour, Perk.INCREASED_CUNNING, selected, unselected));
        setItem(24, new PerkOptionItem(plugin, tierFour, Perk.INCREASED_HEAL, selected, unselected));
        setItem(33, new PerkOptionItem(plugin, tierFour, Perk.INNOCENT_DEFENSE, selected, unselected));
        setItem(42, new PerkOptionItem(plugin, tierFour, Perk.INCREASED_SNEAK, selected, unselected));

        EnumOption<Perk> tierFive = new EnumOption<>();
        setItem(8, new PerkOptionItem(plugin, tierFive, Perk.BATTLEFIELD_TERROR, selected, unselected));
        setItem(17, new PerkOptionItem(plugin, tierFive, Perk.GENERAL, selected, unselected));
        setItem(26, new PerkOptionItem(plugin, tierFive, Perk.CREATURES, selected, unselected));
        setItem(35, new PerkOptionItem(plugin, tierFive, Perk.TELEKINESIS, selected, unselected));
        setItem(44, new PerkOptionItem(plugin, tierFive, Perk.TELEPATHY, selected, unselected));
        setItem(53, new PerkOptionItem(plugin, tierFive, Perk.SPIRIT_FORM, selected, unselected));

        fillEmptySlots();

        tiers.put(1, tierOne);
        tiers.put(2, tierTwo);
        tiers.put(3, tierThree);
        tiers.put(4, tierFour);
        tiers.put(5, tierFive);
    }

    @Override
    public void open(Player player) {
        Character character = characterManager.getCharacterByOwner(player.getUniqueId());
        for (Perk perk : character.getPerks()) {
            EnumOption<Perk> perkOption = tiers.get(perk.getTier());
            perkOption.setSelected(player, perkOption.getOption(perk));
        }

        super.open(player);
    }

    /**
     * Gets a player's currently selected perks.
     *
     * @param player The player.
     * @return The selected perks.
     */
    public List<Perk> getPerks(Player player) {
        List<Perk> perks = new ArrayList<>();
        for (EnumOption<Perk> tier : tiers.values()) {
            EnumOptionItem<Perk> perk = tier.getSelected(player);
            if (perk == null) {
                break;
            }
            perks.add(perk.getEnum());
        }
        return perks;
    }

    /**
     * Resets a player's selected options.
     *
     * @param player The player.
     */
    public void resetOptions(Player player) {
        for (EnumOption<Perk> tier : tiers.values()) {
            tier.removeSelected(player);
        }
    }

    /**
     * A {@link ninja.amp.fallout.menus.items.groups.EnumOptionItem} used exclusively for the PerksMenu.
     */
    private class PerkOptionItem extends EnumOptionItem<Perk> {
        private CharacterManager characterManager;

        public PerkOptionItem(Fallout plugin, EnumOption<Perk> group, Perk perk, ItemStack selected, ItemStack unselected) {
            super(group, perk, perk.getName(), selected, unselected, perk.getDescription());

            this.characterManager = plugin.getCharacterManager();
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            Player player = event.getPlayer();
            UUID playerId = player.getUniqueId();
            Character character = characterManager.getCharacterByOwner(playerId);

            int tier = getEnum().getTier();
            boolean tierSelected = false;
            for (Perk perk : character.getPerks()) {
                if (perk.getTier() == tier) {
                    tierSelected = true;
                    break;
                }
            }

            if (!tierSelected && character.getLevel() >= tier) {
                super.onItemClick(event);
            }
        }
    }
}
