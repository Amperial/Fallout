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

/**
 * Skills each fallout character has and may level up over time.
 *
 * @author Austin Payne
 */
public enum Skill {
    HEAVY_WEAPONS("HeavyWeapons", Trait.STRENGTH),
    MAGIC("Magic", Trait.INTELLIGENCE),
    GUNS("Guns", Trait.PERCEPTION),
    LIGHT_WEAPONS("LightWeapons", Trait.STRENGTH, Trait.AGILITY),
    EXPLOSIVES("Explosives", Trait.PERCEPTION),
    UNARMED("Unarmed", Trait.STRENGTH, Trait.AGILITY),
    FIRST_AID("FirstAid", Trait.INTELLIGENCE),
    SURGERY("Surgery", Trait.INTELLIGENCE),
    LOCKPICKING("Lockpicking", Trait.PERCEPTION),
    REPAIR("Repair", Trait.INTELLIGENCE),
    SNEAK("Sneak", Trait.AGILITY),
    SPEECH("Speech", Trait.CHARISMA);

    private static final List<String> skillNames;
    private final String name;
    private final Trait[] affected;

    Skill(String name, Trait... affected) {
        this.name = name;

        this.affected = affected;
    }

    /**
     * Gets the display name of the skill.
     *
     * @return The skill's display name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the roll modifier of the skill for a given SPECIAL.
     *
     * @param special The SPECIAL
     * @return The skill's roll modifier for the given SPECIAL
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
     * @param name The name of the skill
     * @return The skill
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
     * Gets a list of skill names.
     *
     * @return The list of skill names
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
