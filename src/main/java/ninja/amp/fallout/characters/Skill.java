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
 * An enum of character skills used in fallout.
 */
public enum Skill {
    BIG_GUNS("BigGuns", Trait.STRENGTH),
    ENERGY_WEAPONS("EnergyWeapons", Trait.INTELLIGENCE, Trait.PERCEPTION),
    CONVENTIONAL_GUNS("ConventionalGuns", Trait.AGILITY),
    MELEE_WEAPONS("MeleeWeapons", Trait.STRENGTH, Trait.AGILITY),
    EXPLOSIVES("Explosives", Trait.PERCEPTION),
    UNARMED("Unarmed", Trait.STRENGTH, Trait.AGILITY),
    FIRST_AID("FirstAid", Trait.INTELLIGENCE),
    SURGERY("Surgery", Trait.INTELLIGENCE),
    LOCKPICKING("Lockpicking", Trait.PERCEPTION),
    REPAIR("Repair", Trait.INTELLIGENCE),
    SCIENCE("Science", Trait.INTELLIGENCE),
    SNEAK("Sneak", Trait.AGILITY),
    SPEECH("Speech", Trait.CHARISMA),
    LOGICAL_THINKING("LogicalThinking", Trait.INTELLIGENCE);

    private final String name;
    private final Trait[] affected;
    private static final List<String> skillNames;

    private Skill(String name, Trait... affected) {
        this.name = name;

        this.affected = affected;
    }

    /**
     * Gets the display name of the skill.
     *
     * @return The skill's display name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the roll modifier of the Skill for a given Special.
     *
     * @param special The Special.
     * @return The Skill's roll modifier for the given Special.
     */
    public int getRollModifier(Special special) {
        int average = 0;
        for (Trait trait : affected) {
            average += special.get(trait);
        }
        average = (average + affected.length - 1) / affected.length;
        return average;
    }

    /**
     * Gets a skill from its name.
     *
     * @param name The name.
     * @return The skill.
     */
    public static Skill fromName(String name) {
        for (Skill skill : Skill.class.getEnumConstants()) {
            if (skill.getName().equalsIgnoreCase(name)) {
                return skill;
            }
        }
        return null;
    }

    /**
     * Gets the list of skill names.
     *
     * @return The list of skill names.
     */
    public static List<String> getSkillNames() {
        return skillNames;
    }

    static {
        skillNames = new ArrayList<>();
        for (Skill skill : Skill.class.getEnumConstants()) {
            skillNames.add(skill.getName());
        }
    }
}
