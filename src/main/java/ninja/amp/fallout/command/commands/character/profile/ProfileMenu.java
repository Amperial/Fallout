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

import ninja.amp.fallout.FalloutCore;
import ninja.amp.fallout.character.Character;
import ninja.amp.fallout.character.Information;
import ninja.amp.fallout.character.Perk;
import ninja.amp.fallout.character.Skill;
import ninja.amp.fallout.character.Trait;
import ninja.amp.fallout.menu.ItemMenu;
import ninja.amp.fallout.menu.MenuHolder;
import ninja.amp.fallout.menu.Owner;
import ninja.amp.fallout.menu.items.MenuItem;
import ninja.amp.fallout.menu.items.StaticMenuItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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
    public ProfileMenu(FalloutCore fallout) {
        super(ChatColor.AQUA + "Profile: ", Size.SIX_LINE, fallout);

        setItem(1, new CharacterInfoItem("Gender", new ItemStack(Material.BREAD)) {
            @Override
            public ItemStack getIcon(Character character) {
                switch (character.getGender()) {
                    case MALE:
                        return new ItemStack(Material.STRING);
                    case FEMALE:
                        return new ItemStack(Material.NETHER_BRICK_ITEM);
                    default:
                        return super.getIcon(character);
                }
            }

            @Override
            public void addInfo(Character character, List<String> lore) {
                String s = character.getGender().name().toLowerCase();
                lore.add(java.lang.Character.toString(s.charAt(0)).toUpperCase() + s.substring(1));
            }
        });
        setItem(2, new CharacterInfoItem("Race", new ItemStack(Material.LEATHER_HELMET)) {
            @Override
            public ItemStack getIcon(Character character) {
                switch (character.getRace()) {
                    case WASTELANDER:
                    case VAULT_DWELLER:
                        return new ItemStack(Material.SKULL_ITEM, 1, (short) 0);
                    case GHOUL:
                        return new ItemStack(Material.SKULL_ITEM, 1, (short) 2);
                    case SUPER_MUTANT:
                        return new ItemStack(Material.RAW_FISH, 1, (short) 3);
                    case DEITY:
                        return new ItemStack(Material.DIAMOND_HELMET);
                    default:
                        return super.getIcon(character);
                }
            }

            @Override
            public void addInfo(Character character, List<String> lore) {
                lore.add(character.getRace().getName());
            }
        });
        setItem(3, new CharacterInfoItem("Alignment", new ItemStack(Material.ENDER_STONE)) {
            @Override
            @SuppressWarnings("deprecation")
            public ItemStack getIcon(Character character) {
                switch (character.getAlignment()) {
                    case LAWFUL_GOOD:
                        return new ItemStack(Material.GLOWSTONE);
                    case LAWFUL_NEUTRAL:
                        return new ItemStack(Material.REDSTONE_LAMP_OFF);
                    case LAWFUL_EVIL:
                        return new ItemStack(Material.REDSTONE_ORE);
                    case GOOD:
                        return new ItemStack(Material.WOOL, 1, DyeColor.GREEN.getWoolData());
                    case NEUTRAL:
                        return new ItemStack(Material.ENDER_STONE);
                    case EVIL:
                        return new ItemStack(Material.PISTON_STICKY_BASE);
                    case CHAOTIC_GOOD:
                        return new ItemStack(Material.LEAVES);
                    case CHAOTIC_NEUTRAL:
                        return new ItemStack(Material.WOOD, 1, (short) 5);
                    case CHAOTIC_EVIL:
                        return new ItemStack(Material.EMERALD_ORE);
                    default:
                        return super.getIcon(character);
                }
            }

            @Override
            public void addInfo(Character character, List<String> lore) {
                lore.add(character.getAlignment().getName());
            }
        });
        setItem(10, new CharacterInfoItem("Age", new ItemStack(Material.SAPLING)) {
            ItemStack[] icons = new ItemStack[] {
                    new ItemStack(Material.RED_ROSE, 1, (short) 7),
                    new ItemStack(Material.RED_ROSE, 1, (short) 4),
                    new ItemStack(Material.RED_ROSE, 1, (short) 8),
                    new ItemStack(Material.RED_ROSE, 1, (short) 1),
                    new ItemStack(Material.DEAD_BUSH),
                    new ItemStack(Material.RED_ROSE, 1,(short) 5),
                    new ItemStack(Material.LONG_GRASS, 1, (short) 1),
                    new ItemStack(Material.DOUBLE_PLANT, 1, (short) 3),
                    new ItemStack(Material.LONG_GRASS, 1, (short) 2),
                    new ItemStack(Material.SEEDS)
            };
            int[] ageRanges = new int[] { 75, 60, 45, 35, 31, 27, 23, 19, 15, 6};

            @Override
            public ItemStack getIcon(Character character) {
                int age = character.getAge();

                for (int i = 0; i < 10; ++i) {
                    if (age >= ageRanges[i]) {
                        return icons[i].clone();
                    }
                }

                return super.getIcon(character);
            }

            @Override
            public void addInfo(Character character, List<String> lore) {
                lore.add(String.valueOf(character.getAge()));
            }
        });
        setItem(11, new CharacterInfoItem("Weight", new ItemStack(Material.DIAMOND_CHESTPLATE)) {
            @Override
            public void addInfo(Character character, List<String> lore) {
                lore.add(character.getWeight() + " lbs.");
            }
        });
        setItem(12, new CharacterInfoItem("Height", new ItemStack(Material.DIAMOND_LEGGINGS)) {
            @Override
            public void addInfo(Character character, List<String> lore) {
                lore.add(character.getHeight() + "in.");
            }
        });

        setItem(5, new StaticMenuItem("Perks", new ItemStack(Material.STAINED_GLASS, 1, DyeColor.ORANGE.getWoolData())) {
            @Override
            public ItemStack getFinalIcon(Owner owner) {
                ItemStack itemStack = super.getFinalIcon(owner);

                itemStack.setAmount(owner.getCharacter().getLevel());
                return itemStack;
            }
        });
        setItem(4, new CharacterInfoItem("Faction", new ItemStack(Material.PAINTING)) {
            @Override
            public void addInfo(Character character, List<String> lore) {
                if (character.getFaction() == null) {
                    lore.add("No Allegiance");
                } else {
                    lore.add(character.getFaction());
                }
            }
        });
        setItem(13, new CharacterInfoItem("Knowledge", new ItemStack(Material.BOOK_AND_QUILL)) {
            @Override
            public void addInfo(Character character, List<String> lore) {
                for (String information : Information.getInformationPieces()) {
                    if (character.hasKnowledge(information)) {
                        lore.add(information);
                    }
                }

                if (lore.isEmpty()) {
                    lore.add("No Knowledge");
                }
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

        setItem(37, new SpecialItem(Trait.STRENGTH, new ItemStack(Material.IRON_SPADE)));
        setItem(38, new SpecialItem(Trait.PERCEPTION, new ItemStack(Material.STONE_AXE)));
        setItem(39, new SpecialItem(Trait.ENDURANCE, new ItemStack(Material.IRON_CHESTPLATE)));
        setItem(40, new SpecialItem(Trait.CHARISMA, new ItemStack(Material.WATCH)));
        setItem(41, new SpecialItem(Trait.INTELLIGENCE, new ItemStack(Material.NAME_TAG)));
        setItem(42, new SpecialItem(Trait.AGILITY, new ItemStack(Material.FEATHER)));
        setItem(43, new SpecialItem(Trait.LUCK, new ItemStack(Material.FISHING_ROD)));
    }

    /**
     * Opens the profile menu of a character for a player.
     *
     * @param player    The player
     * @param character The character
     */
    public void open(Player player, Character character) {
        MenuHolder holder = MenuHolder.createInventory(this, getSize().toInt(), getName() + character.getCharacterName());
        apply(holder.getInventory(), Bukkit.getPlayer(character.getOwnerId()));
        player.openInventory(holder.getInventory());
    }

    /**
     * A menu item used in the character profile menu to display character details.
     */
    private abstract class CharacterInfoItem extends MenuItem {

        public CharacterInfoItem(String displayName, ItemStack icon, String... lore) {
            super(displayName, icon, lore);
        }

        @Override
        public ItemStack getFinalIcon(Owner owner) {
            List<String> lore = new ArrayList<>(getLore());
            addInfo(owner.getCharacter(), lore);
            return setNameAndLore(getIcon(owner.getCharacter()), getDisplayName(), lore);
        }

        /**
         * Gets the item stack for a certain character's detail.
         *
         * @param character The character
         * @return The item stack
         */
        public ItemStack getIcon(Character character) {
            return getIcon().clone();
        }

        /**
         * Adds information of a certain character's detail to the item stack lore.
         *
         * @param character The character
         * @param lore The item stack
         */
        public abstract void addInfo(Character character, List<String> lore);

    }

    /**
     * A menu item used in the character profile menu to display one of the character's perks.
     */
    private class PerkItem extends MenuItem {

        private int tier;

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
                return null;
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
