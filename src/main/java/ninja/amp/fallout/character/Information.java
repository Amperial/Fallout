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
import java.util.concurrent.ConcurrentHashMap;

/**
 * Keeps track of pieces of information that fallout characters can learn.
 */
public class Information {

    /**
     * A piece of information that enables the character to wear bound armor.
     */
    public static final String BOUND_ARMOR = "BoundArmor";

    private static final ConcurrentHashMap<String, String> informationPieces = new ConcurrentHashMap<>();

    private Information() {
    }

    /**
     * Checks if a piece of information exists.
     *
     * @param information The piece of information
     * @return {@code true} if the information piece exists
     */
    public static boolean informationExists(String information) {
        return informationPieces.containsKey(information.toLowerCase());
    }

    /**
     * Adds a piece of information to the list of information pieces.
     *
     * @param information The information piece to add
     */
    public static void addInformation(String information) {
        informationPieces.putIfAbsent(information.toLowerCase(), information);
    }

    /**
     * Gets the correct name of a piece of information.
     *
     * @param information The piece of information
     * @return The information piece's correct name
     */
    public static String getInformationPiece(String information) {
        return informationPieces.get(information.toLowerCase());
    }

    /**
     * Gets a list of existing pieces of information.
     *
     * @return The list of information pieces
     */
    public static List<String> getInformationPieces() {
        return new ArrayList<>(informationPieces.values());
    }

    static {
        addInformation(BOUND_ARMOR);
    }

}
