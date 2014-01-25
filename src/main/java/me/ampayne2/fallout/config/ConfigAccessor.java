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

import me.ampayne2.fallout.Fallout;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

/**
 * Used to access a YamlConfiguration file.
 */
public class ConfigAccessor {
    private final Fallout fallout;
    private final ConfigType configType;
    private final File configFile;
    private FileConfiguration fileConfiguration;

    /**
     * Creates a new ConfigAccessor.
     *
     * @param fallout    The {@link me.ampayne2.fallout.Fallout} instance.
     * @param configType The ConfigType of the configuration file.
     * @param parent     The parent file.
     */
    public ConfigAccessor(Fallout fallout, ConfigType configType, File parent) {
        this.fallout = fallout;
        this.configType = configType;
        this.configFile = new File(parent, configType.getFileName());
    }

    /**
     * Reloads the configuration file from disk.
     */
    public ConfigAccessor reloadConfig() {
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

        InputStream defConfigStream = fallout.getResource(configType.getFileName());
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            fileConfiguration.setDefaults(defConfig);
        }
        return this;
    }

    /**
     * Gets the config.
     *
     * @return The FileConfiguration.
     */
    public FileConfiguration getConfig() {
        if (fileConfiguration == null) {
            reloadConfig();
        }
        return fileConfiguration;
    }

    /**
     * Saves the config to disk.
     */
    public ConfigAccessor saveConfig() {
        if (fileConfiguration != null) {
            try {
                getConfig().save(configFile);
            } catch (IOException e) {
                fallout.getMessenger().log(Level.SEVERE, "Could not save config to " + configFile);
                fallout.getMessenger().debug(e);
            }
        }
        return this;
    }

    /**
     * Generates the default config if it hasn't already been generated.
     */
    public ConfigAccessor saveDefaultConfig() {
        if (!configFile.exists()) {
            fallout.saveResource(configType.getFileName(), false);
        }
        return this;
    }

    /**
     * Gets the ConfigType.
     *
     * @return The ConfigAccessor's ConfigType.
     */
    public ConfigType getConfigType() {
        return configType;
    }
}
