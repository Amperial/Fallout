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
package me.ampayne2.fallout.config;

/**
 * An enumeration of the fallout custom config types.
 */
public enum ConfigType {
    PLUGIN("config.yml"),
    MESSAGE("Messages.yml"),
    CHARACTER("Characters.yml");

    private final String fileName;

    private ConfigType(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Gets the ConfigType's file name.
     *
     * @return The file name of the ConfigType.
     */
    public String getFileName() {
        return fileName;
    }
}
