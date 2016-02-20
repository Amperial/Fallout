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

import java.util.ArrayList;
import java.util.List;

/**
 * The types of damage in fallout.
 *
 * @author Austin Payne
 */
public enum DamageType {
    BULLET("Bullet"),
    FIRE("Fire"),
    MAGIC("Magic"),
    ELECTRICITY("Electricity"),
    EXPLOSION("Explosion"),
    CUTTING("Cutting"),
    THRUSTING("Thrusting"),
    BASHING("Bashing");

    private static final List<String> damageTypeNames;
    private final String name;

    DamageType(String name) {
        this.name = name;
    }

    /**
     * Gets the display name of the damage type.
     *
     * @return The damage type's display name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets a damage type from its name.
     *
     * @param name The name
     * @return The damage type
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
     * Gets a list of damage type names.
     *
     * @return The list of damage type names
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
