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
package ninja.amp.fallout.character;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Races a fallout character can select.
 *
 * @author Austin Payne
 */
public enum Race {
    WASTELANDER("Human", new Special(1, 1, 1, 1, 1, 1, 1), new Special(10, 10, 10, 10, 10, 10, 10)),
    GHOUL("Ghoul", new Special(1, 4, 1, 1, 4, 1, 5), new Special(6, 13, 9, 7, 10, 8, 10)),
    SUPER_MUTANT("Super Mutant", new Special(7, 2, 5, 1, 3, 3, 4), new Special(12, 11, 11, 4, 8, 8, 10)),
    VAULT_DWELLER("Vault Dweller", new Special(1, 1, 1, 1, 1, 1, 1), new Special(10, 10, 10, 10, 10, 10, 10)),
    DEITY("Deity", new Special(1, 1, 1, 1, 1, 1, 1), new Special(-1, -1, -1, -1, -1, -1, -1));

    private static final List<String> raceNames;
    private final String name;
    private final Special min;
    private final Special max;

    Race(String name, Special min, Special max) {
        this.name = name;
        this.min = min;
        this.max = max;
    }

    /**
     * Gets the display name of the race.
     *
     * @return The race's display name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the minimum SPECIAL of the race.
     *
     * @return The race's minimum SPECIAL
     */
    public Special getMinSpecial() {
        return min;
    }

    /**
     * Gets the maximum SPECIAL of the race.
     *
     * @return The race's maximum SPECIAL
     */
    public Special getMaxSpecial() {
        return max;
    }

    /**
     * Checks if a certain SPECIAL is valid for the race.<br>
     * The traits in a valid SPECIAL add up to 40, with no more than two traits at their maximum value.<br>
     * SPECIALs of the race {@code DEITY} are only restricted by the minimum value of 1 for each trait.
     *
     * @param special The SPECIAL
     * @return {@code true} if the SPECIAL is valid
     */
    public boolean isValid(Special special) {
        int total = 0;
        for (Integer value : special.getTraits().values()) {
            total += value;
        }

        // These conditions don't apply to the deity race
        if (this != DEITY) {
            // Check to see if the total is 40
            if (total > 40) {
                return false;
            }

            // Check to see if any traits are greater than the max or that multiple are max
            int maxTraits = 0;
            for (Map.Entry<Trait, Integer> entry : special.getTraits().entrySet()) {
                if (entry.getValue() > max.get(entry.getKey())) {
                    return false;
                } else if (entry.getValue() == max.get(entry.getKey())) {
                    if (maxTraits > 1) {
                        return false;
                    } else {
                        maxTraits++;
                    }
                }
            }
        }

        // Check to see if any traits are less than the min
        for (Map.Entry<Trait, Integer> entry : special.getTraits().entrySet()) {
            if (entry.getValue() < min.get(entry.getKey())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Gets a race from its name.
     *
     * @param name The name of the race
     * @return The race
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
     * Gets a list of race names.
     *
     * @return The list of race names
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
