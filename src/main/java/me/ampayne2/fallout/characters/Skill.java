/*
 * This file is part of Fallout.
 *
 * Copyright (c) 2013-2013 <http://github.com/ampayne2/Fallout//>
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
package me.ampayne2.fallout.characters;

import java.util.ArrayList;
import java.util.List;

/**
 * An enum of character skills used in fallout.
 */
public enum Skill {
    ARMOR("Armor");

    private final String name;
    private static final List<String> skillNames;

    private Skill(String name) {
        this.name = name;
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
        for (Trait special : Trait.class.getEnumConstants()) {
            skillNames.add(special.getName());
        }
    }
}
