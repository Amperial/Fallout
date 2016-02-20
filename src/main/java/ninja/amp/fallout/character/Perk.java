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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Perks a fallout character can select, one per tier.
 *
 * @author Austin Payne
 */
public enum Perk {
    LOW_LIGHT_VISION(1, "Low-light Vision", "You gain better vision", "in areas of low light."),
    FROST_SAVANT(1, "Frost Savant", "You are much more resistant", "to cold temperatures than most.", "+1 Defence from Frost"),
    ACROBAT(1, "Acrobat", "You are much more flexible and agile,", "allowing you to climb walls that are", "4 blocks high or less."),
    MUFFLED_MOVEMENT(1, "Muffled Movement", "Your sneaking is no longer", "affected by movement speed."),
    UNBREAKING_WALL(1, "Unbreaking Wall", "Due to your strength, you are", "no longer able to be knocked down", "in combat by humanoid enemies."),

    EYE_ADJUSTMENT(2, "Eye Adjustment", "You are able to switch between", "Darkvision and Low-light vision", "as you please."),
    FIRE_SAVANT(2, "Fire Savant", "You are much more resistant", "to fire than most.", "+1 Defence from Fire"),
    REFUELLED(2, "Refuelled", "You feel a rush of energy after", "climbing surfaces, climbing now only", "takes a half-move turn."),
    LIGHT_STEPS(2, "Light Steps", "You no longer activate", "wire traps in areas of", "low-light or better vision."),
    COUNTER_ATTACK(2, "Counter Attack", "If an attack is made", "after blocking an attack with a", "shield you now get +1 to hit."),

    THIRD_EYE(3, "Third Eye", "You are able to detect", "auras of immense magical power."),
    LIGHTNING_SAVANT(3, "Lightning Savant", "You are much more resistant", "to lightning than most.", "+1 Defence from Lightning"),
    FAR_GOALS(3, "Far Goals", "You are able to jump over", "gaps of 5 blocks or less", "using a full move turn."),
    ASSASSIN(3, "Assassin", "You are able to kill easier from stealth.", "+2 to Hit from Stealth"),
    ARMORER(3, "Armorer", "You now feel well at home", "inside of heavy armor.", "While wearing full heavy armor you", "gain +1 Defence to all attacks."),

    DETECTIVE(4, "Detective", "You now gain advantage", "on perception rolls."),
    EARTH_SAVANT(4, "Earth Savant", "You are much more resistant", "to earth than most.", "+1 Defence from Earth"),
    SPEEDY_FAST(4, "Speedy Fast", "You are much faster than anyone else.", "You gain an additional 2 movement."),
    DEATH_FROM_ABOVE(4, "Death from Above", "While attacking from more than", "two meters above your target", "you gain an additional +2 to hit."),
    PENDING_MENDING(4, "Pending Mending", "You now only take half the normal time", "to regenerate from any wounds."),

    ALL_THINGS_LARGE_AND_SMALL(5, "All Things Large and Small", "You gain an additional advantage and", "can track down anything that leaves tracks."),
    THE_ELEMENTALIST(5, "The Elementalist", "You've learned all things come with a price.", "+1 Magic Defence", "+1 against all four elements"),
    TIME_PHASE(5, "Time Phase", "Your agility is unlike any other,", "allowing you to move with such grace", "that you are granted an additional", "action every turn."),
    SHADOW(5, "Shadow", "While stalking someone,", "as if a shadow,", "you gain +5 to stealth rolls."),
    DEATH_INCARNATE(5, "Death Incarnate", "If your health drops below zero,", "you are able to keep fighting until you reach", "half of your Endurance in the negative.", "Once your health hits negative", "it is impossible to heal your wounds.");

    private static final Map<Integer, Set<Perk>> tiers;
    private static final List<String> perkNames;
    private final String name;
    private final String[] description;
    private int tier;

    Perk(int tier, String name, String... description) {
        this.name = name;
        this.tier = tier;
        this.description = description;
    }

    /**
     * Gets the display name of the perk.
     *
     * @return The perk's display name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the tier of the perk.
     *
     * @return The perk's tier
     */
    public int getTier() {
        return tier;
    }

    /**
     * Gets the description of the perk.
     *
     * @return The perk's description
     */
    public String[] getDescription() {
        return description;
    }

    /**
     * Gets a perk from its name.
     *
     * @param name The name of the perk
     * @return The perk
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
     * Gets the perks of a certain tier.
     *
     * @param tier The tier
     * @return The perks in the tier
     */
    public static Set<Perk> getPerks(int tier) {
        return Collections.unmodifiableSet(tiers.get(tier));
    }

    /**
     * Gets a list of perk names.
     *
     * @return The list of perk names
     */
    public static List<String> getPerkNames() {
        return perkNames;
    }

    static {
        tiers = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            tiers.put(i, new HashSet<Perk>());
        }
        perkNames = new ArrayList<>();
        for (Perk perk : Perk.class.getEnumConstants()) {
            tiers.get(perk.getTier()).add(perk);
            perkNames.add(perk.getName());
        }
    }

}
