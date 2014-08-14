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
package ninja.amp.fallout.characters;

import java.util.ArrayList;
import java.util.List;

/**
 * An enum of character perks used in fallout.
 */
public enum Perk {
    ARMOR("Armor");

    private final String name;
    private static final List<String> perkNames;

    private Perk(String name) {
        this.name = name;
    }

    /**
     * Gets the display name of the perk.
     *
     * @return The perk's display name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets a perk from its name.
     *
     * @param name The name.
     * @return The perk.
     */
    public static Perk fromName(String name) {
        for (Perk perk : Perk.class.getEnumConstants()) {
            if (perk.getName().equalsIgnoreCase(name)) {
                return perk;
            }
        }
        return null;
    }

    /**
     * Gets the list of perk names.
     *
     * @return The list of perk names.
     */
    public static List<String> getPerkNames() {
        return perkNames;
    }

    static {
        perkNames = new ArrayList<>();
        for (Perk perk : Perk.class.getEnumConstants()) {
            perkNames.add(perk.getName());
        }
    }
}
