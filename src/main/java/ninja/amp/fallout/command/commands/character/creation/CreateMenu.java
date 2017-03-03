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
package ninja.amp.fallout.command.commands.character.creation;

import ninja.amp.fallout.FalloutCore;
import ninja.amp.fallout.character.Character;
import ninja.amp.fallout.character.CharacterManager;
import ninja.amp.fallout.character.Race;
import ninja.amp.fallout.command.commands.character.special.SpecialMenu;
import ninja.amp.fallout.menu.ItemMenu;
import ninja.amp.fallout.menu.events.ItemClickEvent;
import ninja.amp.fallout.menu.items.StaticMenuItem;
import ninja.amp.fallout.menu.items.SubMenuItem;
import ninja.amp.fallout.menu.items.groups.EnumOption;
import ninja.amp.fallout.menu.items.groups.EnumOptionItem;
import ninja.amp.fallout.menu.items.groups.Option;
import ninja.amp.fallout.menu.items.groups.OptionItem;
import ninja.amp.fallout.message.FOMessage;
import ninja.amp.fallout.message.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * An inventory menu used for creating a character.
 *
 * @author Austin Payne
 */
public class CreateMenu extends ItemMenu {

    private FalloutCore fallout;
    private EnumOption<Character.Gender> gender;
    private EnumOption<Race> race;
    private Option conformity;
    private Option morality;

    @SuppressWarnings("deprecation")
    public CreateMenu(FalloutCore fallout, SpecialMenu specialMenu) {
        super(ChatColor.AQUA + "Character Options", ItemMenu.Size.FIVE_LINE, fallout);

        this.fallout = fallout;

        setItem(41, new CreateConfirmItem(specialMenu));
        setItem(39, new CreateCancelItem());

        ItemStack unselected = new ItemStack(Material.STONE_BUTTON);

        gender = new EnumOption<>();
        setItem(1, new EnumOptionItem<>(gender, Character.Gender.MALE, "Male", new ItemStack(Material.RAW_FISH), unselected));
        setItem(2, new EnumOptionItem<>(gender, Character.Gender.FEMALE, "Female", new ItemStack(Material.RAW_FISH, 1, (short) 1), unselected));

        race = new EnumOption<>();
        setItem(4, new EnumOptionItem<>(race, Race.WASTELANDER, "Wastelander", new ItemStack(Material.SKULL_ITEM, 1, (short) 0), unselected));
        setItem(5, new EnumOptionItem<>(race, Race.GHOUL, "Ghoul", new ItemStack(Material.SKULL_ITEM, 1, (short) 2), unselected));
        setItem(6, new EnumOptionItem<>(race, Race.SUPER_MUTANT, "Super Mutant", new ItemStack(Material.RAW_FISH, 1, (short) 3), unselected));
        setItem(7, new EnumOptionItem<>(race, Race.VAULT_DWELLER, "Vault Dweller", new ItemStack(Material.SKULL_ITEM, 1, (short) 0), unselected));
        setItem(8, new EnumOptionItem<Race>(race, Race.DEITY, "Deity", new ItemStack(Material.DIAMOND_HELMET), unselected) {
            @Override
            public ItemStack getFinalIcon(Player player) {
                return player.hasPermission("fallout.race.deity") ? super.getFinalIcon(player) : null;
            }

            @Override
            public void onItemClick(ItemClickEvent event) {
                if (event.getPlayer().hasPermission("fallout.race.deity")) {
                    super.onItemClick(event);
                }
            }
        });

        ItemStack red = new ItemStack(Material.WOOL, 1, DyeColor.RED.getWoolData());
        ItemStack yellow = new ItemStack(Material.WOOL, 1, DyeColor.YELLOW.getWoolData());
        ItemStack green = new ItemStack(Material.WOOL, 1, DyeColor.LIME.getWoolData());

        conformity = new Option();
        setItem(19, new OptionItem(conformity, "Lawful", green, unselected));
        setItem(20, new OptionItem(conformity, "Balanced", yellow, unselected));
        setItem(21, new OptionItem(conformity, "Chaotic", red, unselected));

        morality = new Option();
        setItem(23, new OptionItem(morality, "Good", green, unselected));
        setItem(24, new OptionItem(morality, "Neutral", yellow, unselected));
        setItem(25, new OptionItem(morality, "Evil", red, unselected));
    }

    @Override
    public void open(Player player) {
        resetOptions(player);

        super.open(player);
    }

    /**
     * Gets the currently selected gender of a player.
     *
     * @param player The player
     * @return The selected gender option
     */
    public Character.Gender getGender(Player player) {
        return gender.getSelected(player).getEnum();
    }

    /**
     * Gets the currently selected race of a player.
     *
     * @param player The player
     * @return The selected race option
     */
    public Race getRace(Player player) {
        return race.getSelected(player).getEnum();
    }

    /**
     * Gets the currently selected alignment of a player.
     *
     * @param player The player
     * @return The selected alignment option
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
     * @param player The player
     */
    public void resetOptions(Player player) {
        gender.removeSelected(player);
        race.removeSelected(player);
        conformity.removeSelected(player);
        morality.removeSelected(player);
    }

    /**
     * A menu item used in the character creation menu to confirm character creation.
     */
    private class CreateConfirmItem extends SubMenuItem {

        @SuppressWarnings("deprecation")
        public CreateConfirmItem(SpecialMenu specialMenu) {
            super(fallout.getPlugin(), ChatColor.GREEN + "Confirm Character Options",
                    new ItemStack(Material.STAINED_GLASS, 1, DyeColor.LIGHT_BLUE.getWoolData()), specialMenu);
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            Player player = event.getPlayer();

            Messenger messenger = fallout.getMessenger();
            CharacterManager characterManager = fallout.getCharacterManager();

            Character.Gender gender = getGender(player);
            Race race = getRace(player);
            Character.Alignment alignment = getAlignment(player);
            if (gender == null || race == null || alignment == null) {
                messenger.sendErrorMessage(player, FOMessage.ERROR_ALLOPTIONS);
                return;
            }
            resetOptions(player);

            Character.CharacterBuilder builder = characterManager.getCharacterBuilder(player);
            builder.gender(gender);
            builder.race(race);
            builder.alignment(alignment);
            characterManager.createCharacter(player);
            messenger.sendMessage(player, FOMessage.CHARACTER_CREATE, builder.getName());

            super.onItemClick(event);
        }

    }

    /**
     * A menu item used in the character creation menu to cancel character creation.
     */
    private class CreateCancelItem extends StaticMenuItem {

        @SuppressWarnings("deprecation")
        public CreateCancelItem() {
            super(ChatColor.DARK_RED + "Cancel Character Creation",
                    new ItemStack(Material.STAINED_GLASS, 1, DyeColor.MAGENTA.getWoolData()),
                    "Cancels character",
                    "creation and exits",
                    "the menu.");
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            fallout.getCharacterManager().unloadCharacter(event.getPlayer());

            event.setWillClose(true);
        }

    }

}
