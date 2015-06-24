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
import ninja.amp.fallout.menus.ItemMenu;
import ninja.amp.fallout.menus.Owner;
import ninja.amp.fallout.menus.events.ItemClickEvent;
import ninja.amp.fallout.menus.items.StaticMenuItem;
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

    private CharacterManager characterManager;
    private Map<UUID, Special> pendingSpecials = new HashMap<>();

    public SpecialMenu(Fallout plugin) {
        super("SPECIAL Modification", Size.FIVE_LINE, plugin);

        this.characterManager = plugin.getCharacterManager();

        setItem(41, new SpecialConfirmItem(plugin));
        setItem(39, new SpecialCancelItem());

        setItem(10, new SpecialItem(Trait.STRENGTH, new ItemStack(Material.DIAMOND_SWORD)));
        setItem(1, new SpecialAddItem(Trait.STRENGTH));
        setItem(19, new SpecialRemoveItem(Trait.STRENGTH));

        setItem(11, new SpecialItem(Trait.PERCEPTION, new ItemStack(Material.COMPASS)));
        setItem(2, new SpecialAddItem(Trait.PERCEPTION));
        setItem(20, new SpecialRemoveItem(Trait.PERCEPTION));

        setItem(12, new SpecialItem(Trait.ENDURANCE, new ItemStack(Material.GOLD_BOOTS)));
        setItem(3, new SpecialAddItem(Trait.ENDURANCE));
        setItem(21, new SpecialRemoveItem(Trait.ENDURANCE));

        setItem(13, new SpecialItem(Trait.CHARISMA, new ItemStack(Material.DIAMOND)));
        setItem(4, new SpecialAddItem(Trait.CHARISMA));
        setItem(22, new SpecialRemoveItem(Trait.CHARISMA));

        setItem(14, new SpecialItem(Trait.INTELLIGENCE, new ItemStack(Material.BOOK)));
        setItem(5, new SpecialAddItem(Trait.INTELLIGENCE));
        setItem(23, new SpecialRemoveItem(Trait.INTELLIGENCE));

        setItem(15, new SpecialItem(Trait.AGILITY, new ItemStack(Material.FEATHER)));
        setItem(6, new SpecialAddItem(Trait.AGILITY));
        setItem(24, new SpecialRemoveItem(Trait.AGILITY));

        setItem(16, new SpecialItem(Trait.LUCK, new ItemStack(Material.DIAMOND_SWORD)));
        setItem(7, new SpecialAddItem(Trait.LUCK));
        setItem(25, new SpecialRemoveItem(Trait.LUCK));

        fillEmptySlots();
    }

    @Override
    public void open(Player player) {
        Character character = characterManager.getCharacterByOwner(player.getUniqueId());
        pendingSpecials.put(character.getOwnerId(), new Special(character.getSpecial()));

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
                    new ItemStack(Material.WOOL, 1, DyeColor.LIME.getWoolData()),
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

        private Fallout plugin;

        public SpecialConfirmItem(Fallout plugin) {
            super(ChatColor.GREEN + "Confirm SPECIAL Modification",
                    new ItemStack(Material.EMERALD_BLOCK));

            this.plugin = plugin;
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            Player player = event.getPlayer();
            UUID playerId = player.getUniqueId();
            Character character = event.getCharacter();
            Special special = getPendingSpecial(playerId);

            if (character.getRace().isValid(special)) {
                character.getSpecial().set(special);
                plugin.getCharacterManager().saveCharacter(character);
                plugin.getMessenger().sendMessage(player, FOMessage.SPECIAL_SET, character.getCharacterName());
            } else {
                plugin.getMessenger().sendMessage(player, FOMessage.SPECIAL_INVALID);
            }

            resetPendingSpecial(playerId);

            event.setWillClose(true);
        }

    }

    /**
     * A menu item used in the SPECIAL modification menu to cancel SPECIAL modification.
     */
    private class SpecialCancelItem extends StaticMenuItem {

        public SpecialCancelItem() {
            super(ChatColor.DARK_RED + "Cancel SPECIAL Modification",
                    new ItemStack(Material.REDSTONE_BLOCK),
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
