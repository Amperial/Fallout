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

import me.ampayne2.fallout.Fallout;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

/**
 * Stores the information about a fallout character.
 */
public class Character {
    private final Fallout fallout;
    private final String ownerName;
    private final String characterName;
    private final Map<Trait, Integer> traits = new HashMap<>();
    private final List<Skill> skills = new ArrayList<>();

    /**
     * Creates a Character from default settings.
     *
     * @param fallout       The {@link me.ampayne2.fallout.Fallout} instance.
     * @param ownerName     The name of the owning player.
     * @param characterName The name of the character.
     */
    public Character(Fallout fallout, String ownerName, String characterName) {
        this.fallout = fallout;
        this.ownerName = ownerName;
        this.characterName = characterName;
        for (Trait trait : Trait.class.getEnumConstants()) {
            traits.put(trait, 1);
        }
    }

    /**
     * Loads a Character from a ConfigurationSection.
     *
     * @param fallout The {@link me.ampayne2.fallout.Fallout} instance.
     * @param section The ConfigurationSection.
     */
    public Character(Fallout fallout, ConfigurationSection section) {
        this.fallout = fallout;
        // Updates the old playerName config key to ownerName
        if (section.contains("playerName")) {
            ownerName = section.getString("playerName");
            section.set("playerName", null);
            section.set("ownerName", ownerName);
        } else {
            ownerName = section.getString("ownerName");
        }
        characterName = section.getString("characterName");
        for (Trait trait : Trait.class.getEnumConstants()) {
            traits.put(trait, section.getInt(trait.getName()));
        }
        // A check for old configs without skills
        if (section.contains("skills")) {
            List<String> skillNames = section.getStringList("skills");
            for (String skillName : skillNames) {
                skills.add(Skill.fromName(skillName));
            }
        } else {
            section.set("skills", skills);
        }
    }

    /**
     * Gets the owner's name.
     *
     * @return The owner's name.
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Gets the character's name.
     *
     * @return The character's name.
     */
    public String getCharacterName() {
        return characterName;
    }

    /**
     * Gets the level of a trait.
     *
     * @param trait The trait.
     * @return The level of the trait.
     */
    public int getTrait(Trait trait) {
        return traits.get(trait);
    }

    /**
     * Sets the level of a trait.
     *
     * @param trait The trait.
     * @param level The level of the trait.
     */
    public void setTrait(Trait trait, int level) {
        traits.put(trait, level);
    }

    /**
     * Checks if the character has a certain skill.
     *
     * @param skill The skill.
     * @return True if the character has the skill, else false.
     */
    public boolean hasSkill(Skill skill) {
        return skills.contains(skill);
    }

    /**
     * Teaches the character a skill.
     *
     * @param skill The skill to add.
     */
    public void teachSkill(Skill skill) {
        skills.add(skill);
    }

    /**
     * Unteaches the character a skill.
     *
     * @param skill The skill to remove.
     */
    public void unteachSkill(Skill skill) {
        skills.remove(skill);
    }

    /**
     * Gets the character's skills.
     *
     * @return The skills.
     */
    public List<Skill> getSkills() {
        return Collections.unmodifiableList(skills);
    }

    /**
     * Gets the character's skills in a string.
     *
     * @return The skills.
     */
    public String getSkillList() {
        List<String> skillNames = new ArrayList<>();
        for (Skill skill : skills) {
            skillNames.add(skill.getName());
        }
        return StringUtils.join(skillNames, ", ");
    }

    /**
     * Saves the Character to a ConfigurationSection.
     *
     * @param section The ConfigurationSection to save the Character to.
     */
    public void save(ConfigurationSection section) {
        section.set("ownerName", ownerName);
        section.set("characterName", characterName);
        for (Trait trait : Trait.class.getEnumConstants()) {
            section.set(trait.getName(), traits.get(trait));
        }
        List<String> skillNames = new ArrayList<>();
        for (Skill skill : skills) {
            skillNames.add(skill.getName());
        }
        section.set("skills", skillNames);
    }
}
