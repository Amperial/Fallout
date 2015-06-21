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
package ninja.amp.fallout.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Minecraft armor materials and their corresponding fallout versions.
 *
 * @author Austin Payne
 */
public enum ArmorMaterial {
    LEATHER(FOArmor.LEATHER),
    CHAINMAIL(FOArmor.COMBAT),
    IRON(FOArmor.RIOT),
    GOLD(FOArmor.ENVIRONMENTAL),
    DIAMOND(FOArmor.POWER);

    private final FOArmor foArmor;

    private ArmorMaterial(FOArmor foArmor) {
        this.foArmor = foArmor;
    }

    /**
     * Gets the fallout version of an armor material.
     *
     * @return The fallout armor
     */
    public FOArmor getFOVersion() {
        return foArmor;
    }

    /**
     * Gets the armor material of a piece of armor.
     *
     * @param material The piece of armor
     * @return The armor material
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
     * @param player The player
     * @return {@code true} if the player is wearing a full set of armor of the same type
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
