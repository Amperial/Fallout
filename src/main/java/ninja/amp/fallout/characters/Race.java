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
import java.util.Map;

/**
 * Character races used in fallout.
 */
public enum Race {
    WASTELANDER("Human", new Special(1, 1, 1, 1, 1, 1, 1), new Special(10, 10, 10, 10, 10, 10, 10)),
    GHOUL("Ghoul", new Special(1, 4, 1, 1, 4, 1, 5), new Special(6, 13, 9, 7, 10, 8, 10)),
    SUPER_MUTANT("SuperMutant", new Special(5, 1, 4, 1, 1, 1, 1), new Special(13, 10, 12, 4, 5, 10, 10)),
    VAULT_DWELLER("VaultDweller", new Special(1, 1, 1, 1, 1, 1, 1), new Special(10, 10, 10, 10, 10, 10, 10));

    private final String name;
    private final Special min;
    private final Special max;
    private static final List<String> raceNames;

    private Race(String name, Special min, Special max) {
        this.name = name;
        this.min = min;
        this.max = max;
    }

    /**
     * Gets the display name of the race.
     *
     * @return The race's display name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the minimum Special of the race.
     *
     * @return The race's minimum Special.
     */
    public Special getMinSpecial() {
        return min;
    }

    /**
     * Gets the maximum Special of the race.
     *
     * @return The race's maximum Special.
     */
    public Special getMaxSpecial() {
        return max;
    }

    /**
     * Checks if a Special is valid for the race.
     *
     * @param special The Special.
     * @return True if the Special is valid.
     */
    public boolean isValid(Special special) {
        // Check to see if the total is 40
        int total = 0;
        for (Integer value : special.getTraits().values()) {
            total += value;
        }
        if (total > 40) {
            return false;
        }

        // Check to see if any traits aren't within the min/max limits or multiple are maxed
        int maxTraits = 0;
        for (Map.Entry<Trait, Integer> entry : special.getTraits().entrySet()) {
            if (entry.getValue() < min.get(entry.getKey()) || entry.getValue() > max.get(entry.getKey())) {
                return false;
            } else if (entry.getValue() == max.get(entry.getKey())) {
                if (maxTraits > 1) {
                    return false;
                } else {
                    maxTraits++;
                }
            }
        }

        return true;
    }

    /**
     * Gets a race from its name.
     *
     * @param name The name.
     * @return The race.
     */
    public static Race fromName(String name) {
        for (Race race : Race.class.getEnumConstants()) {
            if (race.getName().equalsIgnoreCase(name)) {
                return race;
            }
        }
        return null;
    }

    /**
     * Gets the list of race names.
     *
     * @return The list of race names.
     */
    public static List<String> getRaceNames() {
        return raceNames;
    }

    static {
        raceNames = new ArrayList<>();
        for (Race race : Race.class.getEnumConstants()) {
            raceNames.add(race.getName());
        }
    }
}
