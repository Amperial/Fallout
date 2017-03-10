/*
 * This file is part of Fallout.
 *
 * Copyright (c) 2013-2017 <http://github.com/ampayne2/Fallout//>
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
package ninja.amp.fallout.util;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Fallout utility methods.
 *
 * @author Austin Payne
 */
public final class FOUtils {

    private static final Random RANDOM = new Random();
    private static Pattern NAME_REQUIREMENT = Pattern.compile("([A-Z][a-z]+_)?[A-Z][a-z]+");

    private FOUtils() {
    }

    public static boolean checkName(String name) {
        return NAME_REQUIREMENT == null || NAME_REQUIREMENT.matcher(name).matches();
    }

    public static void setNamePattern(Pattern nameRequirement) {
        NAME_REQUIREMENT = nameRequirement;
    }

    /**
     * Clamps a value between a minimum and maximum value.
     *
     * @param value The value
     * @param min   The minimum value
     * @param max   The maximum value
     * @return The clamped value
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Returns a random int between min and max, inclusive.
     *
     * @param min The minimum value
     * @param max The maximum value
     * @return The random int
     */
    public static int random(int min, int max) {
        return RANDOM.nextInt((max - min) + 1) + min;
    }

    /**
     * Gets the value of a private field.
     *
     * @param object The instance the field resides in
     * @param field  The name of the field
     * @return The value of the private field
     */
    public static Object getPrivateField(Object object, String field) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field objectField = clazz.getDeclaredField(field);
        objectField.setAccessible(true);
        Object result = objectField.get(object);
        objectField.setAccessible(false);
        return result;
    }

}
