/*
 * This file is part of Fallout.
 *
 * Copyright (c) 2013-2014 <http://github.com/ampayne2/Fallout//>
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
package ninja.amp.fallout.utils;

import ninja.amp.fallout.characters.Special;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Materials of armor.
 */
public enum ArmorMaterial {
    LEATHER(new Special(0, 0, 0, 0, 0, 0, 0)),
    CHAINMAIL(new Special(0, 0, 5, 0, 0, -5, 0)),
    IRON(new Special(0, 0, 10, 0, 0, -5, 0)),
    GOLD(new Special(0, 0, 0, 0, 0, 0, 0)),
    DIAMOND(new Special(10, 0, 15, 0, 0, -15, 0));

    private final Special rollModifier;

    private ArmorMaterial(Special rollModifier) {
        this.rollModifier = rollModifier;
    }

    /**
     * Gets the roll modifier of the ArmorMaterial.
     *
     * @return The ArmorMaterial's roll modifier.
     */
    public Special getRollModifier() {
        return rollModifier;
    }

    /**
     * Gets the ArmorMaterial of a piece of armor.
     *
     * @param material The piece of armor.
     * @return The ArmorMaterial.
     */
    public static ArmorMaterial getArmorMaterial(Material material) {
        for (ArmorMaterial armorMaterial : ArmorMaterial.class.getEnumConstants()) {
            if (material.name().contains(armorMaterial.name())) {
                return armorMaterial;
            }
        }
        return null;
    }

    /**
     * Checks if a player is wearing a full set of armor of the same type.
     *
     * @param player The player.
     * @return True if the player is wearing a full set of armor of the same type, else false.
     */
    public static boolean isWearingFullSet(Player player) {
        if (ArmorType.HELMET.canEquip(player) || ArmorType.CHESTPLATE.canEquip(player) || ArmorType.LEGGINGS.canEquip(player) || ArmorType.BOOTS.canEquip(player)) {
            return false;
        }
        ArmorMaterial material = getArmorMaterial(player.getInventory().getHelmet().getType());
        if (!getArmorMaterial(player.getInventory().getChestplate().getType()).equals(material)) {
            return false;
        } else if (!getArmorMaterial(player.getInventory().getLeggings().getType()).equals(material)) {
            return false;
        } else if (!getArmorMaterial(player.getInventory().getBoots().getType()).equals(material)) {
            return false;
        }
        return true;
    }
}
