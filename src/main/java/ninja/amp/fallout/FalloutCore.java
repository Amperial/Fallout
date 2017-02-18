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
package ninja.amp.fallout;

import ninja.amp.fallout.character.CharacterManager;
import ninja.amp.fallout.command.CommandController;
import ninja.amp.fallout.command.commands.roll.RollManager;
import ninja.amp.fallout.config.ConfigManager;
import ninja.amp.fallout.faction.FactionManager;
import ninja.amp.fallout.message.Messenger;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * An interface providing methods to get the core managers that make up the fallout plugin.
 *
 * @author Austin Payne
 */
public interface FalloutCore {

    /**
     * Gets the plugin instance of this fallout core.<br>
     * Not necessarily required to be {@link ninja.amp.fallout.Fallout}.
     *
     * @return This fallout core's plugin instance
     */
    JavaPlugin getPlugin();

    /**
     * Gets the fallout config manager.
     *
     * @return The config manager
     */
    ConfigManager getConfigManager();

    /**
     * Gets the fallout messenger.
     *
     * @return The messenger
     */
    Messenger getMessenger();

    /**
     * Gets the fallout command controller
     *
     * @return The command controller
     */
    CommandController getCommandController();

    /**
     * Gets the fallout character manager
     *
     * @return The character manager
     */
    CharacterManager getCharacterManager();

    /**
     * Gets the fallout roll manager.
     *
     * @return The roll manager
     */
    RollManager getRollManager();

    /**
     * Gets the fallout faction manager.
     *
     * @return The faction manager
     */
    FactionManager getFactionManager();

}
