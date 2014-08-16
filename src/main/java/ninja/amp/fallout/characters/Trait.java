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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final String name;
    private static final List<String> traitNames;
    private static final Map<Integer, Integer> rollModifiers = new HashMap<>();

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

    /**
     * Gets the roll modifier of a trait at a certain value.
     *
     * @param value The value.
     * @return The roll modifier.
     */
    public static int getRollModifier(int value) {
        return rollModifiers.containsKey(value) ? rollModifiers.get(value) : 0;
    }

    static {
        traitNames = new ArrayList<>();
        for (Trait special : Trait.class.getEnumConstants()) {
            traitNames.add(special.getName());
        }

        rollModifiers.put(1, -20);
        rollModifiers.put(2, -10);
        rollModifiers.put(3, 0);
        rollModifiers.put(4, 1);
        rollModifiers.put(5, 2);
        rollModifiers.put(6, 3);
        rollModifiers.put(7, 4);
        rollModifiers.put(8, 5);
        rollModifiers.put(9, 6);
        rollModifiers.put(10, 7);
        rollModifiers.put(11, 8);
        rollModifiers.put(12, 9);
        rollModifiers.put(13, 10);
    }
}
