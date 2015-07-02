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
package ninja.amp.fallout.command.commands.character.profile;

import ninja.amp.fallout.Fallout;
import ninja.amp.fallout.character.Character;
import ninja.amp.fallout.character.Perk;
import ninja.amp.fallout.character.Skill;
import ninja.amp.fallout.character.Trait;
import ninja.amp.fallout.menu.ItemMenu;
import ninja.amp.fallout.menu.MenuHolder;
import ninja.amp.fallout.menu.Owner;
import ninja.amp.fallout.menu.items.MenuItem;
import ninja.amp.fallout.menu.items.StaticMenuItem;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An inventory menu used for viewing a character's profile.
 *
 * @author Austin Payne
 */
public class ProfileMenu extends ItemMenu {

    @SuppressWarnings("deprecation")
    public ProfileMenu(Fallout plugin) {
        super("Character Profile", Size.SIX_LINE, plugin);

        ItemStack infoItem = new ItemStack(Material.WOOL, 1, DyeColor.RED.getWoolData());
        setItem(1, new CharacterInfoItem("Gender", infoItem) {
            @Override
            public String getInfo(Character character) {
                String s = character.getGender().name().toLowerCase();
                return java.lang.Character.toString(s.charAt(0)).toUpperCase() + s.substring(1);
            }
        });
        setItem(2, new CharacterInfoItem("Race", infoItem) {
            @Override
            public String getInfo(Character character) {
                return character.getRace().getName();
            }
        });
        setItem(3, new CharacterInfoItem("Alignment", infoItem) {
            @Override
            public String getInfo(Character character) {
                return character.getAlignment().getName();
            }
        });
        setItem(10, new CharacterInfoItem("Age", infoItem) {
            @Override
            public String getInfo(Character character) {
                return String.valueOf(character.getAge());
            }
        });
        setItem(11, new CharacterInfoItem("Height", infoItem) {
            @Override
            public String getInfo(Character character) {
                return character.getHeight() + " in.";
            }
        });
        setItem(12, new CharacterInfoItem("Weight", infoItem) {
            @Override
            public String getInfo(Character character) {
                return character.getWeight() + " lbs.";
            }
        });

        setItem(5, new StaticMenuItem("Perks", new ItemStack(Material.GOLD_BLOCK)) {
            @Override
            public ItemStack getFinalIcon(Owner owner) {
                ItemStack itemStack = super.getFinalIcon(owner);
                itemStack.setAmount(owner.getCharacter().getLevel());
                return itemStack;
            }
        });

        ItemStack perkItem = new ItemStack(Material.WOOL, 1, DyeColor.GRAY.getWoolData());
        setItem(6, new PerkItem(1, perkItem));
        setItem(7, new PerkItem(2, perkItem));
        setItem(14, new PerkItem(3, perkItem));
        setItem(15, new PerkItem(4, perkItem));
        setItem(16, new PerkItem(5, perkItem));

        ItemStack skillItem = new ItemStack(Material.WOOL, 1, DyeColor.BLUE.getWoolData());
        setItem(27, new SkillItem(Skill.BIG_GUNS, "Big Guns", skillItem));
        setItem(28, new SkillItem(Skill.CONVENTIONAL_GUNS, "Conventional Guns", skillItem));
        setItem(29, new SkillItem(Skill.ENERGY_WEAPONS, "Energy Weapons", skillItem));
        setItem(30, new SkillItem(Skill.MELEE_WEAPONS, "Melee Weapons", skillItem));
        setItem(32, new SkillItem(Skill.LOCKPICKING, "Lockpicking", skillItem));
        setItem(33, new SkillItem(Skill.SNEAK, "Sneak", skillItem));
        setItem(35, new SkillItem(Skill.SPEECH, "Speech", skillItem));
        setItem(45, new SkillItem(Skill.EXPLOSIVES, "Explosives", skillItem));
        setItem(46, new SkillItem(Skill.UNARMED, "Unarmed", skillItem));
        setItem(48, new SkillItem(Skill.FIRST_AID, "First Aid", skillItem));
        setItem(49, new SkillItem(Skill.SURGERY, "Surgery", skillItem));
        setItem(50, new SkillItem(Skill.REPAIR, "Repair", skillItem));
        setItem(52, new SkillItem(Skill.SCIENCE, "Science", skillItem));
        setItem(53, new SkillItem(Skill.LOGICAL_THINKING, "Logical Thinking", skillItem));

        ItemStack traitItem = new ItemStack(Material.WOOL, 1, DyeColor.LIME.getWoolData());
        setItem(37, new SpecialItem(Trait.STRENGTH, traitItem));
        setItem(38, new SpecialItem(Trait.PERCEPTION, traitItem));
        setItem(39, new SpecialItem(Trait.ENDURANCE, traitItem));
        setItem(40, new SpecialItem(Trait.CHARISMA, traitItem));
        setItem(41, new SpecialItem(Trait.INTELLIGENCE, traitItem));
        setItem(42, new SpecialItem(Trait.AGILITY, traitItem));
        setItem(43, new SpecialItem(Trait.LUCK, traitItem));

        fillEmptySlots();
    }

    /**
     * Opens the profile menu of a character for a player.
     *
     * @param player    The player
     * @param character The character
     */
    public void open(Player player, Character character) {
        MenuHolder owner = new MenuHolder(this);
        Inventory inventory = Bukkit.createInventory(owner, getSize().getSize(), getName() + ": " + character.getCharacterName());
        owner.setInventory(inventory);
        apply(inventory, Bukkit.getPlayer(character.getOwnerId()));
        player.openInventory(inventory);
    }

    /**
     * A menu item used in the character profile menu to display character details.
     */
    private abstract class CharacterInfoItem extends MenuItem {

        public CharacterInfoItem(String displayName, ItemStack icon) {
            super(displayName, icon);
        }

        @Override
        public ItemStack getFinalIcon(Owner owner) {
            List<String> lore = new ArrayList<>();
            lore.add(getInfo(owner.getCharacter()));
            return setNameAndLore(getIcon().clone(), getDisplayName(), lore);
        }

        /**
         * Gets the info of a certain type of a character.
         *
         * @param character The character
         * @return The info
         */
        public abstract String getInfo(Character character);

    }

    /**
     * A menu item used in the character profile menu to display one of the character's perks.
     */
    private class PerkItem extends MenuItem {

        private int tier;

        @SuppressWarnings("deprecation")
        private final ItemStack invisibleItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());

        public PerkItem(int tier, ItemStack icon) {
            super("", icon, "Tier " + tier + " Perk");

            this.tier = tier;
        }

        @Override
        public ItemStack getFinalIcon(Owner owner) {
            Perk selected = null;
            for (Perk perk : owner.getCharacter().getPerks()) {
                if (perk.getTier() == tier) {
                    selected = perk;
                    break;
                }
            }
            if (selected == null) {
                return invisibleItem;
            } else {
                List<String> lore = new ArrayList<>(getLore());
                lore.addAll(Arrays.asList(selected.getDescription()));
                return setNameAndLore(getIcon().clone(), selected.getName(), lore);
            }
        }

    }

    /**
     * A menu item used in the character profile menu to display a trait of the character's SPECIAL.
     */
    private class SpecialItem extends StaticMenuItem {

        private Trait trait;

        public SpecialItem(Trait trait, ItemStack icon, String... lore) {
            super(trait.getName(), icon, lore);

            this.trait = trait;
        }

        @Override
        public ItemStack getFinalIcon(Owner owner) {
            ItemStack finalIcon = getIcon().clone();
            finalIcon.setAmount(owner.getCharacter().getSpecial().get(trait));
            return finalIcon;
        }

    }

    /**
     * A menu item used in the character profile menu to display the level of the character's skill.
     */
    private class SkillItem extends StaticMenuItem {

        private Skill skill;

        public SkillItem(Skill skill, String displayName, ItemStack icon, String... lore) {
            super(displayName, icon, lore);

            this.skill = skill;
        }

        @Override
        public ItemStack getFinalIcon(Owner owner) {
            ItemStack finalIcon = getIcon().clone();
            finalIcon.setAmount(owner.getCharacter().skillLevel(skill));
            return finalIcon;
        }

    }

}
