package me.ampayne2.fallout.characters;

import java.util.ArrayList;
import java.util.List;

/**
 * Character races used in fallout.
 */
public enum Race {
    HUMAN("Human"),
    GHOUL("Ghoul"),
    SUPER_MUTANT("SuperMutant");

    private final String name;
    private static final List<String> raceNames;

    private Race(String name) {
        this.name = name;
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
