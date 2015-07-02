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

import java.util.HashMap;
import java.util.Map;

/**
 * A container for traits and their values.<br>
 * Referred to as a SPECIAL, an acronym for the traits contained.
 *
 * @author Austin Payne
 */
public class Special {

    private Map<Trait, Integer> traits = new HashMap<>();

    /**
     * Creates a new SPECIAL from a map of traits and values.
     *
     * @param traits The map of traits and values
     */
    public Special(Map<Trait, Integer> traits) {
        this.traits.putAll(traits);
    }

    /**
     * Creates a new SPECIAL from an existing SPECIAL.
     *
     * @param special The existing SPECIAL
     */
    public Special(Special special) {
        this(special.getTraits());
    }

    /**
     * Creates a new SPECIAL with specific values.
     *
     * @param strength     The Strength value
     * @param perception   The Perception value
     * @param endurance    The Endurance value
     * @param charisma     The Charisma value
     * @param intelligence The intelligence value
     * @param agility      The agility value
     * @param luck         The luck value
     */
    public Special(int strength, int perception, int endurance, int charisma, int intelligence, int agility, int luck) {
        traits.put(Trait.STRENGTH, strength);
        traits.put(Trait.PERCEPTION, perception);
        traits.put(Trait.ENDURANCE, endurance);
        traits.put(Trait.CHARISMA, charisma);
        traits.put(Trait.INTELLIGENCE, intelligence);
        traits.put(Trait.AGILITY, agility);
        traits.put(Trait.LUCK, luck);
    }

    /**
     * Gets a trait's value.
     *
     * @param trait The trait
     * @return The trait's value
     */
    public int get(Trait trait) {
        return traits.get(trait);
    }

    /**
     * Sets a trait's value.
     *
     * @param trait The trait
     * @param value The value
     */
    public void set(Trait trait, int value) {
        traits.put(trait, value);
    }

    /**
     * Copies the values of another SPECIAL.
     *
     * @param special The SPECIAL whose values to copy
     */
    public void set(Special special) {
        for (Trait trait : special.getTraits().keySet()) {
            traits.put(trait, special.get(trait));
        }
    }

    /**
     * Gets the SPECIAL's traits and values.
     *
     * @return The traits and their values
     */
    public Map<Trait, Integer> getTraits() {
        return traits;
    }

    /**
     * Gets the total value of all the SPECIAL's traits.
     *
     * @return The total value of the SPECIAL
     */
    public int getTotal() {
        int total = 0;
        for (Map.Entry<Trait, Integer> trait : traits.entrySet()) {
            total += trait.getValue();
        }
        return total;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("S:").append(traits.get(Trait.STRENGTH))
                .append(" P:").append(traits.get(Trait.PERCEPTION))
                .append(" E:").append(traits.get(Trait.ENDURANCE))
                .append(" C:").append(traits.get(Trait.CHARISMA))
                .append(" I:").append(traits.get(Trait.INTELLIGENCE))
                .append(" A:").append(traits.get(Trait.AGILITY))
                .append(" L:").append(traits.get(Trait.LUCK)).toString();
    }

}
