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
package ninja.amp.fallout.config;

import ninja.amp.fallout.Fallout;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains custom plugin configs.
 *
 * @author Austin Payne
 */
public class ConfigManager {

    private final Fallout plugin;
    private final Map<Config, ConfigAccessor> configs = new HashMap<>();

    /**
     * Creates a new config manager.
     *
     * @param plugin the fallout plugin instance
     */
    public ConfigManager(Fallout plugin) {
        this.plugin = plugin;

        // Save main config
        plugin.saveDefaultConfig();

        // Register custom configs
        registerCustomConfigs(EnumSet.allOf(FOConfig.class), plugin);
    }

    /**
     * Adds a config accessor for each custom config.
     *
     * @param configs the custom configs to register
     * @param plugin  the plugin that owns the configs
     */
    public void registerCustomConfigs(EnumSet<? extends Config> configs, Plugin plugin) {
        configs.forEach(config -> registerCustomConfig(config, plugin));
    }

    /**
     * Adds a config accessor for a custom config.
     *
     * @param config the custom config to register
     * @param plugin the plugin containing the default resources for the config
     */
    public void registerCustomConfig(Config config, Plugin plugin) {
        addConfigAccessor(new ConfigAccessor(plugin, config, this.plugin.getDataFolder()).saveDefaultConfig());
    }

    /**
     * Adds a config accessor to the config manager.
     *
     * @param configAccessor the config accessor
     */
    private void addConfigAccessor(ConfigAccessor configAccessor) {
        configs.put(configAccessor.getConfigType(), configAccessor);
    }

    /**
     * Gets a certain config accessor.
     *
     * @param configType the type of the configuration file
     * @return the config accessor
     */
    public ConfigAccessor getConfigAccessor(Config configType) {
        return configs.get(configType).reloadConfig();
    }

    /**
     * Gets a certain configuration file.
     *
     * @param configType the type of the configuration file
     * @return the configuration file
     */
    public FileConfiguration getConfig(Config configType) {
        return configs.get(configType).reloadConfig().getConfig();
    }

}
