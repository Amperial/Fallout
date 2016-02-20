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
package ninja.amp.fallout.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Minecraft armor materials and their corresponding fallout versions.
 *
 * @author Austin Payne
 */
public enum ArmorMaterial {
    LEATHER("Leather"),
    CHAINMAIL("Chainmail"),
    IRON("Iron"),
    GOLD("Ancient"),
    DIAMOND("Bound");

    private static final List<String> materialNames;
    private final String name;
    private final Map<DamageType, Integer> defenseValues = new HashMap<>(9);

    ArmorMaterial(String name) {
        this.name = name;
    }

    /**
     * Gets the display name of the armor material.
     *
     * @return The armor material's display name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the defense value of the armor material against a certain damage type.
     *
     * @param damageType The damage type
     * @return The armor material's resistance to the damage type
     */
    public int getDefenseValue(DamageType damageType) {
        return defenseValues.get(damageType);
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
        PlayerInventory inventory = player.getInventory();
        ArmorMaterial material = getArmorMaterial(inventory.getHelmet().getType());

        return material != null &&
                material.equals(getArmorMaterial(inventory.getChestplate().getType())) &&
                material.equals(getArmorMaterial(inventory.getLeggings().getType())) &&
                material.equals(getArmorMaterial(inventory.getBoots().getType()));
    }

    /**
     * Gets an armor material from its name.
     *
     * @param name The name of the armor material
     * @return The armor material
     */
    public static ArmorMaterial fromName(String name) {
        for (ArmorMaterial armorMaterial : ArmorMaterial.class.getEnumConstants()) {
            if (armorMaterial.getName().equalsIgnoreCase(name)) {
                return armorMaterial;
            }
        }
        return null;
    }

    /**
     * Gets a list of armor material names.
     *
     * @return The list of armor material names
     */
    public static List<String> getMaterialNames() {
        return materialNames;
    }

    static {
        materialNames = new ArrayList<>(5);

        int[][] values = {
                {1, 0, 0, 5, 1, 4, 2, 3},
                {4, 1, 1, 3, 1, 3, 3, 2},
                {5, 2, 2, 2, 2, 3, 3, 2},
                {2, 5, 2, 4, 3, 1, 0, 2},
                {1, 2, 4, 5, 3, 1, 0, 3}
        };

        ArmorMaterial[] armorMaterials = ArmorMaterial.class.getEnumConstants();
        DamageType[] damageTypes = DamageType.class.getEnumConstants();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 9; j++) {
                armorMaterials[i].defenseValues.put(damageTypes[j], values[i][j]);
            }
        }

        for (ArmorMaterial armorMaterial : armorMaterials) {
            materialNames.add(armorMaterial.getName());
        }
    }

}
