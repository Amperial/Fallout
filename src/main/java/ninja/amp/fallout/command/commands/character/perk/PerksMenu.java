/*
 * This file is part of Fallout.
 *
 * Copyright (c) 2013-2017 <http://github.com/ampayne2/Fallout//>
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

import ninja.amp.fallout.FalloutCore;
import ninja.amp.fallout.character.Character;
import ninja.amp.fallout.character.Perk;
import ninja.amp.fallout.menu.ItemMenu;
import ninja.amp.fallout.menu.events.ItemClickEvent;
import ninja.amp.fallout.menu.items.StaticMenuItem;
import ninja.amp.fallout.menu.items.groups.EnumOption;
import ninja.amp.fallout.menu.items.groups.EnumOptionItem;
import ninja.amp.fallout.message.FOMessage;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An inventory menu used for selecting your character's perks.
 *
 * @author Austin Payne
 */
public class PerksMenu extends ItemMenu {

    private FalloutCore fallout;
    private Map<Integer, EnumOption<Perk>> tiers = new HashMap<>();

    @SuppressWarnings("deprecation")
    public PerksMenu(FalloutCore fallout) {
        super(ChatColor.AQUA + "Perk Selection", Size.SIX_LINE, fallout);

        this.fallout = fallout;

        setItem(50, new PerksConfirmItem());
        setItem(48, new PerksCancelItem());

        ItemStack selected = new ItemStack(Material.WOOL, 1, DyeColor.GREEN.getWoolData());
        ItemStack unselected = new ItemStack(Material.WOOL, 1, DyeColor.GRAY.getWoolData());

        EnumOption<Perk> tierOne = new EnumOption<>();
        setItem(0, new PerkOptionItem(tierOne, Perk.SIGHT_ADAPT, selected, unselected));
        setItem(9, new PerkOptionItem(tierOne, Perk.ANTI_COLD, selected, unselected));
        setItem(18, new PerkOptionItem(tierOne, Perk.INCREASED_JUMP, selected, unselected));
        setItem(27, new PerkOptionItem(tierOne, Perk.INCREASED_STEALTH, selected, unselected));
        setItem(36, new PerkOptionItem(tierOne, Perk.ENERGY_REPLENISH, selected, unselected));

        EnumOption<Perk> tierTwo = new EnumOption<>();
        setItem(2, new PerkOptionItem(tierTwo, Perk.SIGHT_DISTANCE, selected, unselected));
        setItem(11, new PerkOptionItem(tierTwo, Perk.INCREASED_STRENGTH, selected, unselected));
        setItem(20, new PerkOptionItem(tierTwo, Perk.INCREASED_SURVIVAL, selected, unselected));
        setItem(29, new PerkOptionItem(tierTwo, Perk.INCREASED_ENDURANCE, selected, unselected));
        setItem(38, new PerkOptionItem(tierTwo, Perk.INCREASED_AIM, selected, unselected));

        EnumOption<Perk> tierThree = new EnumOption<>();
        setItem(4, new PerkOptionItem(tierThree, Perk.DECREASED_BLEEDING, selected, unselected));
        setItem(13, new PerkOptionItem(tierThree, Perk.ANTI_RADIATION, selected, unselected));
        setItem(22, new PerkOptionItem(tierThree, Perk.INCREASED_SENSES, selected, unselected));
        setItem(31, new PerkOptionItem(tierThree, Perk.PERFECT_MEMORY, selected, unselected));
        setItem(40, new PerkOptionItem(tierThree, Perk.FEIGN_DEATH, selected, unselected));

        EnumOption<Perk> tierFour = new EnumOption<>();
        setItem(6, new PerkOptionItem(tierFour, Perk.INCREASED_LOGIC, selected, unselected));
        setItem(15, new PerkOptionItem(tierFour, Perk.INCREASED_CUNNING, selected, unselected));
        setItem(24, new PerkOptionItem(tierFour, Perk.INCREASED_HEAL, selected, unselected));
        setItem(33, new PerkOptionItem(tierFour, Perk.INNOCENT_DEFENSE, selected, unselected));
        setItem(42, new PerkOptionItem(tierFour, Perk.INCREASED_SNEAK, selected, unselected));

        EnumOption<Perk> tierFive = new EnumOption<>();
        setItem(8, new PerkOptionItem(tierFive, Perk.BATTLEFIELD_TERROR, selected, unselected));
        setItem(17, new PerkOptionItem(tierFive, Perk.GENERAL, selected, unselected));
        setItem(26, new PerkOptionItem(tierFive, Perk.CREATURES, selected, unselected));
        setItem(35, new PerkOptionItem(tierFive, Perk.TELEKINESIS, selected, unselected));
        setItem(44, new PerkOptionItem(tierFive, Perk.TELEPATHY, selected, unselected));
        setItem(53, new PerkOptionItem(tierFive, Perk.SPIRIT_FORM, selected, unselected));

        tiers.put(1, tierOne);
        tiers.put(2, tierTwo);
        tiers.put(3, tierThree);
        tiers.put(4, tierFour);
        tiers.put(5, tierFive);
    }

    @Override
    public void open(Player player) {
        Character character = fallout.getCharacterManager().getCharacterByOwner(player.getUniqueId());
        for (Perk perk : character.getPerks()) {
            EnumOption<Perk> perkOption = tiers.get(perk.getTier());
            perkOption.setSelected(player, perkOption.getOption(perk));
        }

        super.open(player);
    }

    /**
     * Gets a player's currently selected perks.
     *
     * @param player The player
     * @return The selected perks
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
     * @param player The player
     */
    public void resetOptions(Player player) {
        for (EnumOption<Perk> tier : tiers.values()) {
            tier.removeSelected(player);
        }
    }

    /**
     * A special enum option item that checks if the character's level should allow selection of that perk.
     */
    private class PerkOptionItem extends EnumOptionItem<Perk> {

        public PerkOptionItem(EnumOption<Perk> group, Perk perk, ItemStack selected, ItemStack unselected) {
            super(group, perk, perk.getName(), selected, unselected, perk.getDescription());
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            Character character = event.getCharacter();

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

    /**
     * A menu item used in the perk selection menu to confirm perk selection.
     */
    private class PerksConfirmItem extends StaticMenuItem {

        @SuppressWarnings("deprecation")
        public PerksConfirmItem() {
            super(ChatColor.GREEN + "Confirm Perk Selection",
                    new ItemStack(Material.STAINED_GLASS, 1, DyeColor.LIGHT_BLUE.getWoolData()),
                    "THIS IS PERMANENT");
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            Player player = event.getPlayer();
            Character character = event.getCharacter();

            List<Perk> perks = getPerks(player);
            for (Perk perk : perks) {
                if (!character.hasPerk(perk)) {
                    character.addPerk(perk);
                    if (perk == Perk.ANTI_RADIATION) {
                        character.updateRadiationResistance();
                    }
                }
            }
            fallout.getCharacterManager().saveCharacter(character);
            fallout.getMessenger().sendMessage(player, FOMessage.PERKS_CONFIRM);

            resetOptions(player);

            event.setWillClose(true);
        }

    }

    /**
     * A menu item used in the perk selection menu to cancel perk selection.
     */
    private class PerksCancelItem extends StaticMenuItem {

        @SuppressWarnings("deprecation")
        public PerksCancelItem() {
            super(ChatColor.DARK_RED + "Cancel Perk Selection",
                    new ItemStack(Material.STAINED_GLASS, 1, DyeColor.MAGENTA.getWoolData()),
                    "Cancels the current",
                    "selection and exits",
                    "the menu.");
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            resetOptions(event.getPlayer());

            event.setWillClose(true);
        }

    }

}
