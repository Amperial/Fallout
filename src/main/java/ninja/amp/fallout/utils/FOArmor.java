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

/**
 * The types of armor in fallout.
 */
public enum FOArmor {
    NONE,
    LEATHER,
    ENVIRONMENTAL,
    RIOT,
    COMBAT,
    POWER;

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
}
