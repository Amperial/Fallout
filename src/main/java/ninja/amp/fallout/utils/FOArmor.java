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
package ninja.amp.fallout.utils;

import ninja.amp.fallout.characters.Special;
import ninja.amp.fallout.rolls.combat.DamageType;
import org.bukkit.entity.Player;

/**
 * Types of armor in fallout.
 */
public enum FOArmor {
    NONE(new Special(0, 0, 0, 0, 0, 0, 0)),
    LEATHER(new Special(0, 0, 0, 0, 0, 0, 0)),
    ENVIRONMENTAL(new Special(0, 0, 1, 0, 0, -1, 0)),
    RIOT(new Special(0, 0, 2, 0, 0, -1, 0)),
    COMBAT(new Special(0, 0, 0, 0, 0, 0, 0)),
    POWER(new Special(2, 0, 3, 0, 0, -3, 0));

    private final Special rollModifier;

    private FOArmor(Special rollModifier) {
        this.rollModifier = rollModifier;
    }

    /**
     * Gets the roll modifier of the FOArmor.
     *
     * @return The FOArmor's roll modifier.
     */
    public Special getRollModifier() {
        return rollModifier;
    }

    /**
     * Checks if the FOArmor type is able to defend against a roll of a certain damage type.
     *
     * @param damageType The damage type.
     * @param roll The roll.
     * @return If the armor is able to block the damage.
     */
    public boolean canBlock(DamageType damageType, int roll) {
        return damageType.getDefenseValue(this) >= 0 && roll >= damageType.getDefenseValue(this);
    }

    /**
     * Gets the roll modifier a player if wearing a full set of armor.
     *
     * @return A player's roll modifier from armor.
     */
    public static Special getRollModifier(Player player) {
        if (ArmorMaterial.isWearingFullSet(player)) {
            ArmorMaterial material = ArmorMaterial.getArmorMaterial(player.getInventory().getHelmet().getType());
            return material.getFOVersion().getRollModifier();
        } else {
            return NONE.getRollModifier();
        }
    }
}
