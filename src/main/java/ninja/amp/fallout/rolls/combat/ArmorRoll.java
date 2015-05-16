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
package ninja.amp.fallout.rolls.combat;

import ninja.amp.fallout.utils.FOArmor;
import ninja.amp.fallout.utils.FOUtils;

public class ArmorRoll {
    private final FOArmor armorType;
    private final DamageType damageType;

    public ArmorRoll(FOArmor armorType, DamageType damageType) {
        this.armorType = armorType;
        this.damageType = damageType;
    }

    public int roll() {
        int roll = FOUtils.random(1, 6);
        return roll;
        //return armorType.canBlock(damageType, roll);
    }
}
