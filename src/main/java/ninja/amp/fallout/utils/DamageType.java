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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The types of damage in fallout.
 */
public enum DamageType {
    BULLET("Bullet", 6, 5, 3, 2, 1),
    LASER("Laser", 5, 4, 4, 3, 0),
    FIRE("Fire", -1, 2, 6, 5, 2),
    PLASMA("Plasma", -1, 5, 6, 5, 4),
    EMP("EMP",-1, -1, -1, -1, 5),
    EXPLOSION("Explosion", 6, 4, 6, 5, 3),
    CUTTING("Cutting", 3, 6, 5, 3, 1),
    THRUSTING("Thrusting", 5, -1, 4, 3, 0),
    BASHING("Bashing", 4, 5, 5, 4, 3);

    private final String name;
    private final Map<FOArmor, Integer> defenseValues = new HashMap<>();
    private static final List<String> damageTypeNames;

    private DamageType(String name, int leather, int env, int riot, int combat, int power) {
        this.name = name;

        defenseValues.put(FOArmor.LEATHER, leather);
        defenseValues.put(FOArmor.ENVIRONMENTAL, env);
        defenseValues.put(FOArmor.RIOT, riot);
        defenseValues.put(FOArmor.COMBAT, combat);
        defenseValues.put(FOArmor.POWER, power);
    }

    /**
     * Gets the display name of the damage type.
     *
     * @return The damage type's display name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the defense value of an armor type against the damage type.
     *
     * @param armorType The FOArmor type.
     * @return The defense value.
     */
    public int getDefenseValue(FOArmor armorType) {
        return defenseValues.get(armorType);
    }

    /**
     * Gets a damage type from its name.
     *
     * @param name The name.
     * @return The damage type.
     */
    public static DamageType fromName(String name) {
        for (DamageType damageType : DamageType.class.getEnumConstants()) {
            if (damageType.getName().equalsIgnoreCase(name)) {
                return damageType;
            }
        }
        return null;
    }

    /**
     * Gets the list of damage type names.
     *
     * @return The list of damage type names.
     */
    public static List<String> getDamageTypeNames() {
        return damageTypeNames;
    }

    static {
        damageTypeNames = new ArrayList<>();
        for (DamageType damageType : DamageType.class.getEnumConstants()) {
            damageTypeNames.add(damageType.getName());
        }
    }
}
