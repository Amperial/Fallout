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
package me.ampayne2.fallout;

import me.ampayne2.fallout.characters.CharacterManager;
import me.ampayne2.fallout.command.CommandController;
import me.ampayne2.fallout.config.ConfigManager;
import me.ampayne2.fallout.message.Messenger;
import me.ampayne2.fallout.message.RecipientHandler;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class of the Fallout plugin.
 */
public class Fallout extends JavaPlugin {
    private ConfigManager configManager;
    private Messenger messenger;
    private CommandController commandController;
    private CharacterManager characterManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        messenger = new Messenger(this).registerRecipient(CommandSender.class, new RecipientHandler() {
            @Override
            public void sendMessage(Object recipient, String message) {
                ((CommandSender) recipient).sendMessage(message);
            }
        }).registerRecipient(Server.class, new RecipientHandler() {
            @Override
            public void sendMessage(Object recipient, String message) {
                ((Server) recipient).broadcastMessage(message);
            }
        });
        commandController = new CommandController(this);
        characterManager = new CharacterManager(this);
    }

    @Override
    public void onDisable() {
        characterManager = null;
        getCommand(commandController.getMainCommand().getName()).setExecutor(null);
        commandController = null;
        messenger = null;
        configManager = null;
    }

    /**
     * Gets the fallout config manager.
     *
     * @return The ConfigManager instance.
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Gets the fallout message manager.
     *
     * @return The Messenger instance.
     */
    public Messenger getMessenger() {
        return messenger;
    }

    /**
     * Gets the fallout command controller.
     *
     * @return The CommandController instance.
     */
    public CommandController getCommandController() {
        return commandController;
    }

    /**
     * Gets the fallout character manager.
     *
     * @return The CharacterManager instance.
     */
    public CharacterManager getCharacterManager() {
        return characterManager;
    }
}
