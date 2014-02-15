/*
 * This file is part of Fallout.
 *
 * Copyright (c) 2013-2013 <http://github.com/ampayne2/Fallout//>
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
package me.ampayne2.fallout.utils;

import org.bukkit.Material;

/**
 * Materials of armor.
 */
public enum ArmorMaterial {
    LEATHER,
    CHAINMAIL,
    IRON,
    GOLD,
    DIAMOND;

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
}
