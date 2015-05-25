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

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Stores the information about a fallout character.
 */
public class Character {
    private String ownerName;
    private UUID ownerId;
    private String characterName;
    private Race race;
    private int age;
    private int height;
    private int weight;
    private Gender gender;
    private Alignment alignment;
    private Special special;
    private Map<Skill, Integer> skills = new HashMap<>();
    private List<Perk> perks = new ArrayList<>();
    private int level;

    /**
     * Creates a Character from default settings.
     *
     * @param builder The {@link ninja.amp.fallout.characters.Character.CharacterBuilder}.
     */
    public Character(CharacterBuilder builder) {
        this.ownerName = builder.ownerName;
        this.ownerId = builder.ownerId;
        this.characterName = builder.characterName;
        this.race = builder.race;
        this.age = builder.age;
        this.height = builder.height;
        this.weight = builder.weight;
        this.gender = builder.gender;
        this.alignment = builder.alignment;
        this.special = race.getMinSpecial();
        this.level = 0;
    }

    /**
     * Loads a Character from a ConfigurationSection.
     *
     * @param section The ConfigurationSection.
     */
    public Character(ConfigurationSection section) {
        if (section.contains("ownerName")) {
            String lastOwnerName = section.getString("ownerName");
            this.ownerId = UUID.fromString(section.getString("ownerId"));
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (ownerId.equals(player.getUniqueId())) {
                    lastOwnerName = player.getName();
                    break;
                }
            }
            this.ownerName = lastOwnerName;
        } else {
            this.ownerName = null;
            this.ownerId = null;
        }
        this.characterName = section.getName();
        this.race = Race.fromName(section.getString("race"));
        this.age = section.getInt("age");
        this.height = section.getInt("height");
        this.weight = section.getInt("weight");
        this.gender = Gender.valueOf(section.getString("gender"));
        this.alignment = Alignment.valueOf(section.getString("alignment"));
        Map<Trait, Integer> traits = new HashMap<>();
        for (Trait trait : Trait.class.getEnumConstants()) {
            traits.put(trait, section.getInt(trait.getName()));
        }
        special = new Special(traits);
        ConfigurationSection skillLevels = section.getConfigurationSection("skills");
        for (Skill skill : Skill.class.getEnumConstants()) {
            if (skillLevels.contains(skill.name())) {
                skills.put(skill, skillLevels.getInt(skill.name()));
            }
        }
        List<String> perkNames = section.getStringList("perks");
        for (String perkName : perkNames) {
            perks.add(Perk.fromName(perkName));
        }
        this.level = section.getInt("level");
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
     * Gets the owner's id.
     *
     * @return The owner's id.
     */
    public UUID getOwnerId() {
        return ownerId;
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
     * Gets the character's race.
     *
     * @return The character's race.
     */
    public Race getRace() {
        return race;
    }

    /**
     * Gets the character's age.
     *
     * @return The character's age.
     */
    public int getAge() {
        return age;
    }

    /**
     * Gets the character's height.
     *
     * @return The character's height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the character's weight.
     *
     * @return The character's weight.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Gets the character's gender.
     *
     * @return The character's gender.
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Gets the character's alignment.
     *
     * @return The character's alignment.
     */
    public Alignment getAlignment() {
        return alignment;
    }

    /**
     * Gets the character's Special.
     *
     * @return The character's Special.
     */
    public Special getSpecial() {
        return special;
    }

    /**
     * Checks if the character has a certain perk.
     *
     * @param perk The perk.
     * @return True if the character has the perk, else false.
     */
    public boolean hasPerk(Perk perk) {
        return perks.contains(perk);
    }

    /**
     * Teaches the character a perk.
     *
     * @param perk The perk to add.
     */
    public void teachPerk(Perk perk) {
        perks.add(perk);
    }

    /**
     * Gets the character's perks.
     *
     * @return The perks.
     */
    public List<Perk> getPerks() {
        return Collections.unmodifiableList(perks);
    }

    /**
     * Gets the character's perks in a string.
     *
     * @return The perks.
     */
    public String getPerkList() {
        List<String> perkNames = new ArrayList<>();
        for (Perk perk : perks) {
            perkNames.add(perk.getName());
        }
        return StringUtils.join(perkNames, ", ");
    }

    /**
     * Checks the level of the character's skill.
     *
     * @param skill The skill.
     * @return The level.
     */
    public int skillLevel(Skill skill) {
        return skills.containsKey(skill) ? skills.get(skill) : 0;
    }

    /**
     * Increases the level of the character's skill.
     *
     * @param skill The skill.
     */
    public void increaseSkill(Skill skill) {
        if (skills.containsKey(skill)) {
            skills.put(skill, skills.get(skill) + 1);
        } else {
            skills.put(skill, 1);
        }
    }

    /**
     * Gets the character's skill levels.
     *
     * @return The skill levels.
     */
    public Map<Skill, Integer> getSkills() {
        return Collections.unmodifiableMap(skills);
    }

    /**
     * Gets the character's skills and levels in a string.
     *
     * @return The skills and levels.
     */
    public String getSkillList() {
        List<String> skillLevels = new ArrayList<>();
        for (Map.Entry<Skill, Integer> skill : skills.entrySet()) {
            skillLevels.add(skill.getKey().getName() + " - " + skill.getValue());
        }
        return StringUtils.join(skillLevels, ", ");
    }

    /**
     * Possesses the character by a player.
     *
     * @param owner The character's new owner.
     */
    public void possess(Player owner) {
        this.ownerName = owner.getName();
        this.ownerId = owner.getUniqueId();
    }

    /**
     * Abandons the character's owner.
     */
    public void abandon() {
        ownerName = null;
        ownerId = null;
    }

    /**
     * Saves the Character to a ConfigurationSection.
     *
     * @param section The ConfigurationSection to save the Character to.
     */
    public void save(ConfigurationSection section) {
        if (ownerName == null) {
            section.set("ownerName", null);
            section.set("ownerId", null);
        } else {
            section.set("ownerName", ownerName);
            section.set("ownerId", ownerId.toString());
        }
        section.set("race", race.getName());
        section.set("age", age);
        section.set("height", height);
        section.set("weight", weight);
        section.set("gender", gender.name());
        section.set("alignment", alignment.name());
        for (Map.Entry<Trait, Integer> entry : special.getTraits().entrySet()) {
            section.set(entry.getKey().getName(), entry.getValue());
        }
        ConfigurationSection skillLevels = section.createSection("skills");
        for (Map.Entry<Skill, Integer> skill : skills.entrySet()) {
            skillLevels.set(skill.getKey().name(), skill.getValue());
        }
        List<String> perkNames = new ArrayList<>();
        for (Perk perk : perks) {
            perkNames.add(perk.getName());
        }
        section.set("perks", perkNames);
        section.set("level", level);
    }

    /**
     * Gender of a fallout character.
     */
    public enum Gender {
        MALE,
        FEMALE
    }

    /**
     * DnD Alignment of a fallout character.
     */
    public enum Alignment {
        LAWFUL_GOOD,
        LAWFUL_NEUTRAL,
        LAWFUL_EVIL,
        GOOD,
        NEUTRAL,
        EVIL,
        CHAOTIC_GOOD,
        CHAOTIC_NEUTRAL,
        CHAOTIC_EVIL
    }

    /**
     * Builder class for a new fallout character.
     */
    public static class CharacterBuilder {
        private final String ownerName;
        private final UUID ownerId;
        private String characterName;
        private Race race;
        private int age;
        private int height;
        private int weight;
        private Gender gender;
        private Alignment alignment;

        public CharacterBuilder(Player player) {
            this.ownerName = player.getName();
            this.ownerId = player.getUniqueId();
        }

        public CharacterBuilder name(String name) {
            this.characterName = name;
            return this;
        }

        public CharacterBuilder race(Race race) {
            this.race = race;
            return this;
        }

        public CharacterBuilder age(int age) {
            this.age = age;
            return this;
        }

        public CharacterBuilder height(int height) {
            this.height = height;
            return this;
        }

        public CharacterBuilder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public CharacterBuilder gender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public CharacterBuilder alignment(Alignment alignment) {
            this.alignment = alignment;
            return this;
        }

        public Character build() {
            return new Character(this);
        }
    }
}
