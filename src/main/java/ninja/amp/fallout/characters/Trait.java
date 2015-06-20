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
package ninja.amp.fallout.characters;

import java.util.ArrayList;
import java.util.List;

/**
 * An enum of character traits used in fallout.
 */
public enum Trait {
    STRENGTH("Strength"),
    PERCEPTION("Perception"),
    ENDURANCE("Endurance"),
    CHARISMA("Charisma"),
    INTELLIGENCE("Intelligence"),
    AGILITY("Agility"),
    LUCK("Luck");

    private static final List<String> traitNames;
    private final String name;

    private Trait(String name) {
        this.name = name;
    }

    /**
     * Gets the display name of the trait.
     *
     * @return The trait's display name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets a trait from its name.
     *
     * @param name The name.
     * @return The trait.
     */
    public static Trait fromName(String name) {
        for (Trait special : Trait.class.getEnumConstants()) {
            if (special.getName().equalsIgnoreCase(name)) {
                return special;
            }
        }
        return null;
    }

    /**
     * Gets the list of trait names.
     *
     * @return The list of trait names.
     */
    public static List<String> getTraitNames() {
        return traitNames;
    }

    static {
        traitNames = new ArrayList<>();
        for (Trait special : Trait.class.getEnumConstants()) {
            traitNames.add(special.getName());
        }
    }
}
