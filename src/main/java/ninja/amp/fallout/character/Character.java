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

import ninja.amp.fallout.util.ArmorMaterial;
import ninja.amp.fallout.util.FOUtils;
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
 *
 * @author Austin Payne
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
    private List<String> knowledge = new ArrayList<>();
    private String faction;
    private int radiation;
    private int resistance;
    private long lastRadX;
    private long remainingRadX;

    /**
     * Creates a Character from a character builder.
     *
     * @param builder The character builder
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
        this.special = new Special(race.getMinSpecial());
        // All skill levels start at 1
        for (Skill skill : Skill.class.getEnumConstants()) {
            skills.put(skill, 0);
        }
        this.level = 0;
        this.faction = null;
        this.radiation = 0;
        this.updateRadiationResistance();
        this.lastRadX = 0;
        this.remainingRadX = 0;
    }

    /**
     * Creates a Character, loading it from a configuration section.
     *
     * @param section The configuration section
     * @throws Exception If section is formatted incorrectly or does not represent a complete character
     */
    public Character(ConfigurationSection section) throws Exception {
        if (section.isString("ownerName")) {
            String lastOwnerName = section.getString("ownerName");
            try {
                this.ownerId = UUID.fromString(section.getString("ownerId"));
            } catch (IllegalArgumentException e) {
                throw new Exception("Missing or invalid owner ID");
            }
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
        if (section.isString("name")) {
            this.characterName = section.getString("name");
            if (characterName.length() < 3 || characterName.length() > 20 || !characterName.matches("([A-Z][a-z]+_)?[A-Z][a-z]+")) {
                throw new Exception("Character names must be comprised of between 3 and 20 letters");
            }
        } else {
            throw new Exception("Missing or invalid character name");
        }
        this.race = Race.fromName(section.getString("race"));
        if (race == null) {
            throw new Exception("Missing or invalid race");
        }
        if (section.isInt("age")) {
            this.age = Math.max(6, section.getInt("age"));
        } else {
            throw new Exception("Missing or invalid age");
        }
        if (section.isInt("height")) {
            this.height = Math.max(36, section.getInt("height"));
        } else {
            throw new Exception("Missing or invalid height");
        }
        if (section.isInt("weight")) {
            this.weight = Math.max(72, section.getInt("weight"));
        } else {
            throw new Exception("Missing or invalid weight");
        }
        try {
            this.gender = Gender.valueOf(section.getString("gender"));
        } catch (IllegalArgumentException e) {
            throw new Exception("Missing or invalid gender");
        }
        try {
            this.alignment = Alignment.valueOf(section.getString("alignment"));
        } catch (IllegalArgumentException e) {
            throw new Exception("Missing or invalid alignment");
        }
        if (section.isConfigurationSection("special")) {
            ConfigurationSection specialSection = section.getConfigurationSection("special");
            Map<Trait, Integer> traits = new HashMap<>();
            for (Trait trait : Trait.class.getEnumConstants()) {
                if (specialSection.isInt(trait.getName())) {
                    int level = specialSection.getInt(trait.getName());
                    if (race != Race.DEITY) {
                        level = FOUtils.clamp(level, race.getMinSpecial().get(trait), race.getMaxSpecial().get(trait));
                    }
                    traits.put(trait, level);
                } else {
                    throw new Exception("Missing or invalid trait: " + trait.getName());
                }
            }
            special = new Special(traits);
        } else {
            throw new Exception("Missing or invalid special");
        }
        if (section.isConfigurationSection("skills")) {
            ConfigurationSection skillLevels = section.getConfigurationSection("skills");
            for (Skill skill : Skill.class.getEnumConstants()) {
                if (skillLevels.isInt(skill.getName())) {
                    skills.put(skill, FOUtils.clamp(skillLevels.getInt(skill.getName()), 0, 5));
                } else {
                    throw new Exception("Missing or invalid skill: " + skill.getName());
                }
            }
        } else {
            throw new Exception("Missing or invalid skills");
        }
        if (section.isList("perks")) {
            List<String> perkNames = section.getStringList("perks");
            for (String perkName : perkNames) {
                Perk perk = Perk.fromName(perkName);
                if (perk != null) {
                    perks.add(perk);
                }
            }
        } else {
            throw new Exception("Missing or invalid perks");
        }
        if (section.isInt("level")) {
            this.level = FOUtils.clamp(section.getInt("level"), 0, 5);
        } else {
            throw new Exception("Missing or invalid level");
        }
        if (section.isList("knowledge")) {
            this.knowledge = section.getStringList("knowledge");
        } else {
            throw new Exception("Missing or invalid knowledge");
        }
        this.faction = section.getString("faction");
        if (section.isInt("radiation")) {
            this.radiation = FOUtils.clamp(section.getInt("radiation"), 0, 1000);
        } else {
            throw new Exception("Missing or invalid radiation");
        }
        updateRadiationResistance();
        if (section.isLong("lastRadX") && section.isLong("remainingRadX")) {
            this.lastRadX = section.getLong("lastRadX");
            this.remainingRadX = section.getLong("remainingRadX");
        }
    }

    /**
     * Gets the owner's name.
     *
     * @return The owner's name
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Gets the owner's uuid.
     *
     * @return The owner's uuid
     */
    public UUID getOwnerId() {
        return ownerId;
    }

    /**
     * Gets the character's name.
     *
     * @return The character's name
     */
    public String getCharacterName() {
        return characterName;
    }

    /**
     * Gets the character's race.
     *
     * @return The character's race
     */
    public Race getRace() {
        return race;
    }

    /**
     * Gets the character's age in years.
     *
     * @return The character's age
     */
    public int getAge() {
        return age;
    }

    /**
     * Gets the character's height in inches.
     *
     * @return The character's height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the character's weight in pounds.
     *
     * @return The character's weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Gets the character's gender.
     *
     * @return The character's gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Gets the character's alignment.
     *
     * @return The character's alignment
     */
    public Alignment getAlignment() {
        return alignment;
    }

    /**
     * Gets the character's SPECIAL.
     *
     * @return The character's SPECIAL
     */
    public Special getSpecial() {
        return special;
    }

    /**
     * Checks if the character has a certain perk.
     *
     * @param perk The perk
     * @return {@code true} if the character has the perk
     */
    public boolean hasPerk(Perk perk) {
        return perks.contains(perk);
    }

    /**
     * Adds a perk to the character.
     *
     * @param perk The perk to add
     */
    public void addPerk(Perk perk) {
        perks.add(perk);
        if (perk == Perk.ANTI_RADIATION) {
            updateRadiationResistance();
        }
    }

    /**
     * Removes a perk from the character.
     *
     * @param perk The perk to remove
     */
    public void removePerk(Perk perk) {
        perks.remove(perk);
        if (perk == Perk.ANTI_RADIATION) {
            updateRadiationResistance();
        }
    }

    /**
     * Gets the character's perks.
     *
     * @return An unmodifiable view of the character's perks
     */
    public List<Perk> getPerks() {
        return Collections.unmodifiableList(perks);
    }

    /**
     * Gets a formatted string containing the character's perks.
     *
     * @return The perks, in regular expression format "perk[, perk]*"
     */
    public String getPerkList() {
        List<String> perkNames = new ArrayList<>();
        for (Perk perk : perks) {
            perkNames.add(perk.getName());
        }
        return StringUtils.join(perkNames, ", ");
    }

    /**
     * Checks the character's level of a certain skill.
     *
     * @param skill The skill
     * @return The level
     */
    public int skillLevel(Skill skill) {
        return skills.containsKey(skill) ? skills.get(skill) : 0;
    }

    /**
     * Sets the character's level of a certain skill.
     *
     * @param skill The skill
     * @param level The level
     */
    public void setSkillLevel(Skill skill, int level) {
        skills.put(skill, level);
    }

    /**
     * Gets the character's skills and their levels.
     *
     * @return An unmodifiable view of the character's skill levels
     */
    public Map<Skill, Integer> getSkillLevels() {
        return Collections.unmodifiableMap(skills);
    }

    /**
     * Gets a formatted string containing the character's skills and their levels.
     *
     * @return The skill levels, in regular expression format "skill - level[, skill - level]*"
     */
    public String getSkillList() {
        List<String> skillLevels = new ArrayList<>();
        for (Map.Entry<Skill, Integer> skill : skills.entrySet()) {
            skillLevels.add(skill.getKey().getName() + " - " + skill.getValue());
        }
        return StringUtils.join(skillLevels, ", ");
    }

    /**
     * Gets the character's level.
     *
     * @return The character's level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Increases the character's level by 1.
     */
    public void increaseLevel() {
        level += 1;
    }

    /**
     * Checks if the character knows a certain piece of information.
     *
     * @param information The piece of information
     * @return {@code true} if the character knows the piece of information
     */
    public boolean hasKnowledge(String information) {
        return knowledge.contains(information.toLowerCase());
    }

    /**
     * Adds a piece of information to the character.
     *
     * @param information The piece of information
     */
    public void addKnowledge(String information) {
        knowledge.add(information.toLowerCase());
    }

    /**
     * Removes a piece of information from the character.
     *
     * @param information The piece of information
     */
    public void removeKnowledge(String information) {
        knowledge.remove(information.toLowerCase());
    }

    /**
     * Gets the character's faction.
     *
     * @return The character's faction
     */
    public String getFaction() {
        return faction;
    }

    /**
     * Sets the character's faction.
     *
     * @param faction The faction
     */
    public void setFaction(String faction) {
        this.faction = faction;
    }

    /**
     * Gets the character's radiation level.
     *
     * @return The character's radiation level
     */
    public int getRadiation() {
        return radiation;
    }

    /**
     * Adds radiation to the character.
     *
     * @param radiation The radiation to add
     */
    public void addRadiation(int radiation) {
        this.radiation = FOUtils.clamp(this.radiation + radiation, 0, 1000);
    }

    /**
     * Resets the character's radiation level.
     */
    public void resetRadiation() {
        this.radiation = 0;
    }

    /**
     * Gets the character's radiation resistance.
     *
     * @return The character's radiation resistance
     */
    public int getRadiationResistance() {
        int finalResistance = resistance;
        if (System.currentTimeMillis() - lastRadX <= 240000) {
            finalResistance += 25 + (skillLevel(Skill.FIRST_AID) * 10);
        }
        return Math.min(finalResistance, 85);
    }

    /**
     * Updates the character's radiation resistance.
     */
    public void updateRadiationResistance() {
        // Initial resistance based on endurance
        resistance = 2 * special.get(Trait.ENDURANCE);
        // Resistance from perks
        if (perks.contains(Perk.ANTI_RADIATION)) {
            resistance += 25;
        }
        // Resistance from armor
        if (ownerId != null) {
            Player player = Bukkit.getPlayer(ownerId);
            if (player != null) {
                if (ArmorMaterial.isWearingFullSet(player)) {
                    ArmorMaterial material = ArmorMaterial.getArmorMaterial(player.getInventory().getHelmet().getType());
                    if (ArmorMaterial.DIAMOND.equals(material)) {
                        resistance += 10;
                    } else if (ArmorMaterial.GOLD.equals(material)) {
                        resistance += 30;
                    }
                }
            }
        }
    }

    /**
     * Updates the character's rad-x duration and last time used.
     */
    public void useRadX() {
        long timeElapsed = System.currentTimeMillis() - lastRadX;
        if (remainingRadX > timeElapsed) {
            remainingRadX -= timeElapsed;
        } else {
            remainingRadX = 0;
        }
        remainingRadX += 240000L;
        lastRadX += timeElapsed;
    }

    /**
     * Possesses the character by a player.
     *
     * @param owner The character's new owner
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
     * Saves the character to a configuration section.
     *
     * @param section The configuration section
     */
    public void save(ConfigurationSection section) {
        if (ownerName == null) {
            section.set("ownerName", null);
            section.set("ownerId", null);
        } else {
            section.set("ownerName", ownerName);
            section.set("ownerId", ownerId.toString());
        }
        section.set("name", characterName);
        section.set("race", race.getName());
        section.set("age", age);
        section.set("height", height);
        section.set("weight", weight);
        section.set("gender", gender.name());
        section.set("alignment", alignment.name());
        ConfigurationSection specialSection = section.createSection("special");
        for (Map.Entry<Trait, Integer> entry : special.getTraits().entrySet()) {
            specialSection.set(entry.getKey().getName(), entry.getValue());
        }
        ConfigurationSection skillLevels = section.createSection("skills");
        for (Map.Entry<Skill, Integer> skill : skills.entrySet()) {
            skillLevels.set(skill.getKey().getName(), skill.getValue());
        }
        List<String> perkNames = new ArrayList<>();
        for (Perk perk : perks) {
            perkNames.add(perk.getName());
        }
        section.set("perks", perkNames);
        section.set("level", level);
        section.set("knowledge", knowledge);
        section.set("faction", faction);
        section.set("radiation", radiation);
        section.set("lastRadX", lastRadX);
        section.set("remainingRadX", remainingRadX);
    }

    /**
     * Genders a fallout character can have.
     */
    public enum Gender {
        MALE,
        FEMALE
    }

    /**
     * DnD Alignments a fallout character can have.
     */
    public enum Alignment {
        LAWFUL_GOOD("Lawful Good"),
        LAWFUL_NEUTRAL("Lawful Neutral"),
        LAWFUL_EVIL("Lawful Evil"),
        GOOD("Good"),
        NEUTRAL("Neutral"),
        EVIL("Evil"),
        CHAOTIC_GOOD("Chaotic Good"),
        CHAOTIC_NEUTRAL("Chaotic Neutral"),
        CHAOTIC_EVIL("Chaotic Evil");

        private final String name;

        Alignment(String name) {
            this.name = name;
        }

        /**
         * Gets the display name of the alignment.
         *
         * @return The alignment's display name
         */
        public String getName() {
            return name;
        }
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

        public String getName() {
            return characterName;
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
