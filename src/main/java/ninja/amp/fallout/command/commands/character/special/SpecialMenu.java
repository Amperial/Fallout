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
package ninja.amp.fallout.command.commands.character.special;

import ninja.amp.fallout.FalloutCore;
import ninja.amp.fallout.character.Character;
import ninja.amp.fallout.character.Special;
import ninja.amp.fallout.character.Trait;
import ninja.amp.fallout.menu.ItemMenu;
import ninja.amp.fallout.menu.Owner;
import ninja.amp.fallout.menu.events.ItemClickEvent;
import ninja.amp.fallout.menu.items.StaticMenuItem;
import ninja.amp.fallout.message.FOMessage;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * An inventory menu used for viewing and setting a character's SPECIAL.
 *
 * @author Austin Payne
 */
public class SpecialMenu extends ItemMenu {

    private FalloutCore fallout;
    private Map<UUID, Special> pendingSpecials = new HashMap<>();

    public SpecialMenu(FalloutCore fallout) {
        super(ChatColor.AQUA + "SPECIAL Modification", Size.FIVE_LINE, fallout);

        this.fallout = fallout;

        setItem(41, new SpecialConfirmItem());
        setItem(39, new SpecialCancelItem());

        setItem(10, new SpecialItem(Trait.STRENGTH, new ItemStack(Material.IRON_SPADE)));
        setItem(1, new SpecialAddItem(Trait.STRENGTH));
        setItem(19, new SpecialRemoveItem(Trait.STRENGTH));

        setItem(11, new SpecialItem(Trait.PERCEPTION, new ItemStack(Material.STONE_AXE)));
        setItem(2, new SpecialAddItem(Trait.PERCEPTION));
        setItem(20, new SpecialRemoveItem(Trait.PERCEPTION));

        setItem(12, new SpecialItem(Trait.ENDURANCE, new ItemStack(Material.IRON_CHESTPLATE)));
        setItem(3, new SpecialAddItem(Trait.ENDURANCE));
        setItem(21, new SpecialRemoveItem(Trait.ENDURANCE));

        setItem(13, new SpecialItem(Trait.CHARISMA, new ItemStack(Material.WATCH)));
        setItem(4, new SpecialAddItem(Trait.CHARISMA));
        setItem(22, new SpecialRemoveItem(Trait.CHARISMA));

        setItem(14, new SpecialItem(Trait.INTELLIGENCE, new ItemStack(Material.NAME_TAG)));
        setItem(5, new SpecialAddItem(Trait.INTELLIGENCE));
        setItem(23, new SpecialRemoveItem(Trait.INTELLIGENCE));

        setItem(15, new SpecialItem(Trait.AGILITY, new ItemStack(Material.FEATHER)));
        setItem(6, new SpecialAddItem(Trait.AGILITY));
        setItem(24, new SpecialRemoveItem(Trait.AGILITY));

        setItem(16, new SpecialItem(Trait.LUCK, new ItemStack(Material.FISHING_ROD)));
        setItem(7, new SpecialAddItem(Trait.LUCK));
        setItem(25, new SpecialRemoveItem(Trait.LUCK));
    }

    @Override
    public void open(Player player) {
        Character character = fallout.getCharacterManager().getCharacterByOwner(player.getUniqueId());
        pendingSpecials.put(character.getOwnerId(), character.getSpecial());

        super.open(player);
    }

    /**
     * Gets the pending SPECIAL of a character.
     *
     * @param ownerId The uuid of the character's owner
     * @return The character's modified SPECIAL
     */
    public Special getPendingSpecial(UUID ownerId) {
        return pendingSpecials.get(ownerId);
    }

    /**
     * Resets a character's pending SPECIAL.
     *
     * @param ownerId The uuid of the character's owner
     */
    public void resetPendingSpecial(UUID ownerId) {
        pendingSpecials.remove(ownerId);
    }

    /**
     * A menu item used in the SPECIAL modification menu to indicate the current level of a trait.
     */
    private class SpecialItem extends StaticMenuItem {

        private Trait trait;

        public SpecialItem(Trait trait, ItemStack icon, String... lore) {
            super(trait.getName(), icon, lore);

            this.trait = trait;
        }

        @Override
        public ItemStack getFinalIcon(Player player) {
            ItemStack finalIcon = getIcon().clone();

            Special special = getPendingSpecial(player.getUniqueId());
            finalIcon.setAmount(special.get(trait));

            return finalIcon;
        }

    }

    /**
     * A menu item used in the SPECIAL modification menu to increase the level of a trait.
     */
    private class SpecialAddItem extends StaticMenuItem {

        private Trait trait;
        private ItemStack disabledIcon;

        @SuppressWarnings("deprecation")
        public SpecialAddItem(Trait trait) {
            super("Add Point",
                    new ItemStack(Material.WOOL, 1, DyeColor.GREEN.getWoolData()),
                    "Increases the below",
                    "trait by one point.");

            this.trait = trait;

            disabledIcon = new ItemStack(Material.WOOL, 1, DyeColor.GRAY.getWoolData());
            setNameAndLore(disabledIcon, getDisplayName(), getLore());
        }

        @Override
        @SuppressWarnings("deprecation")
        public ItemStack getFinalIcon(Owner owner) {
            Character character = owner.getCharacter();
            Special special = getPendingSpecial(character.getOwnerId());

            special.set(trait, special.get(trait) + 1);
            boolean valid = character.getRace().isValid(special);
            special.set(trait, special.get(trait) - 1);

            return valid ? super.getFinalIcon(owner) : disabledIcon;
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            Character character = event.getCharacter();
            Special special = getPendingSpecial(character.getOwnerId());

            special.set(trait, special.get(trait) + 1);
            if (character.getRace().isValid(special)) {
                event.setWillUpdate(true);
            } else {
                special.set(trait, special.get(trait) - 1);
            }
        }

    }

    /**
     * A menu item used in the SPECIAL modification menu to decrease the level of a trait.
     */
    private class SpecialRemoveItem extends StaticMenuItem {

        private Trait trait;
        private ItemStack disabledIcon;

        @SuppressWarnings("deprecation")
        public SpecialRemoveItem(Trait trait) {
            super("Remove Point",
                    new ItemStack(Material.WOOL, 1, DyeColor.RED.getWoolData()),
                    "Decreases the above",
                    "trait by one point.");

            this.trait = trait;

            disabledIcon = new ItemStack(Material.WOOL, 1, DyeColor.GRAY.getWoolData());
            setNameAndLore(disabledIcon, getDisplayName(), getLore());
        }

        @Override
        @SuppressWarnings("deprecation")
        public ItemStack getFinalIcon(Owner owner) {
            Character character = owner.getCharacter();
            Special special = getPendingSpecial(character.getOwnerId());

            special.set(trait, special.get(trait) - 1);
            boolean valid = character.getRace().isValid(special);
            special.set(trait, special.get(trait) + 1);

            return valid ? super.getFinalIcon(owner) : disabledIcon;
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            Character character = event.getCharacter();
            Special special = getPendingSpecial(character.getOwnerId());

            special.set(trait, special.get(trait) - 1);
            if (character.getRace().isValid(special)) {
                event.setWillUpdate(true);
            } else {
                special.set(trait, special.get(trait) + 1);
            }
        }

    }

    /**
     * A menu item used in the SPECIAL modification menu to confirm SPECIAL modification.
     */
    private class SpecialConfirmItem extends StaticMenuItem {

        @SuppressWarnings("deprecation")
        public SpecialConfirmItem() {
            super(ChatColor.GREEN + "Confirm SPECIAL Modification",
                    new ItemStack(Material.STAINED_GLASS, 1, DyeColor.LIGHT_BLUE.getWoolData()));
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            Player player = event.getPlayer();
            UUID playerId = player.getUniqueId();
            Character character = event.getCharacter();
            Special special = getPendingSpecial(playerId);

            if (character.getRace().isValid(special)) {
                character.setSpecial(special);
                character.updateRadiationResistance();
                fallout.getCharacterManager().saveCharacter(character);
                fallout.getMessenger().sendMessage(player, FOMessage.SPECIAL_SET, character.getCharacterName());
            } else {
                fallout.getMessenger().sendErrorMessage(player, FOMessage.SPECIAL_INVALID);
            }

            resetPendingSpecial(playerId);

            event.setWillClose(true);
        }

    }

    /**
     * A menu item used in the SPECIAL modification menu to cancel SPECIAL modification.
     */
    private class SpecialCancelItem extends StaticMenuItem {

        @SuppressWarnings("deprecation")
        public SpecialCancelItem() {
            super(ChatColor.DARK_RED + "Cancel SPECIAL Modification",
                    new ItemStack(Material.STAINED_GLASS, 1, DyeColor.MAGENTA.getWoolData()),
                    "Cancels the current",
                    "selection and exits",
                    "the menu.");
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            resetPendingSpecial(event.getPlayer().getUniqueId());

            event.setWillClose(true);
        }

    }

}
