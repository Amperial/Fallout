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
package ninja.amp.fallout.config;

import ninja.amp.fallout.Fallout;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains plugin configs.
 */
public class ConfigManager {
    private Map<ConfigType, ConfigAccessor> configs = new HashMap<>();

    /**
     * Creates a new config manager.
     *
     * @param plugin The {@link ninja.amp.fallout.Fallout} instance.
     */
    public ConfigManager(Fallout plugin) {
        // Save main config
        plugin.saveDefaultConfig();

        // Save and load custom configs
        File dataFolder = plugin.getDataFolder();
        for (ConfigType configType : ConfigType.class.getEnumConstants()) {
            addConfigAccessor(new ConfigAccessor(plugin, configType, dataFolder).saveDefaultConfig());
        }
    }

    /**
     * Adds a {@link ninja.amp.fallout.config.ConfigAccessor} to the config manager.
     *
     * @param configAccessor The {@link ninja.amp.fallout.config.ConfigAccessor}.
     */
    public void addConfigAccessor(ConfigAccessor configAccessor) {
        configs.put(configAccessor.getConfigType(), configAccessor);
    }

    /**
     * Gets a certain {@link ninja.amp.fallout.config.ConfigAccessor}.
     *
     * @param configType The {@link ConfigType} of the config.
     * @return The {@link ninja.amp.fallout.config.ConfigAccessor}.
     */
    public ConfigAccessor getConfigAccessor(ConfigType configType) {
        return configs.get(configType);
    }

    /**
     * Gets a certain FileConfiguration.
     *
     * @param configType The {@link ConfigType} of the config.
     * @return The FileConfiguration.
     */
    public FileConfiguration getConfig(ConfigType configType) {
        return configs.get(configType).getConfig();
    }
}
