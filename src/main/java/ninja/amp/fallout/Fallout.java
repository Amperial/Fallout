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
package ninja.amp.fallout;

import ninja.amp.fallout.characters.CharacterManager;
import ninja.amp.fallout.command.CommandController;
import ninja.amp.fallout.command.CommandGroup;
import ninja.amp.fallout.command.commands.AboutCommand;
import ninja.amp.fallout.command.commands.HelpCommand;
import ninja.amp.fallout.command.commands.PrivateRoll;
import ninja.amp.fallout.command.commands.ReloadCommand;
import ninja.amp.fallout.command.commands.Roll;
import ninja.amp.fallout.command.commands.Whois;
import ninja.amp.fallout.command.commands.character.Create;
import ninja.amp.fallout.command.commands.character.Delete;
import ninja.amp.fallout.command.commands.character.ListPerks;
import ninja.amp.fallout.command.commands.character.ListSpecial;
import ninja.amp.fallout.command.commands.character.SetSpecial;
import ninja.amp.fallout.command.commands.character.Teach;
import ninja.amp.fallout.command.commands.character.Unteach;
import ninja.amp.fallout.config.ConfigManager;
import ninja.amp.fallout.menus.MenuListener;
import ninja.amp.fallout.message.Messenger;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class of the Fallout plugin.
 */
public class Fallout extends JavaPlugin {
    private ConfigManager configManager;
    private Messenger messenger;
    private CommandController commandController;
    private CharacterManager characterManager;
    private FOListener foListener;
    private MenuListener menuListener;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        messenger = new Messenger(this);
        commandController = new CommandController(this);
        characterManager = new CharacterManager(this);
        foListener = new FOListener(this);
        menuListener = new MenuListener(this);

        // Create fallout command tree
        CommandGroup character = new CommandGroup(this, "character")
                .addChildCommand(new Create(this))
                .addChildCommand(new Delete(this))
                .addChildCommand(new ListSpecial(this))
                .addChildCommand(new SetSpecial(this))
                .addChildCommand(new Teach(this))
                .addChildCommand(new Unteach(this))
                .addChildCommand(new ListPerks(this));
        character.setPermission(new Permission("fallout.character.all", PermissionDefault.OP));
        CommandGroup fallout = new CommandGroup(this, "fallout")
                .addChildCommand(new AboutCommand(this))
                .addChildCommand(new HelpCommand(this))
                .addChildCommand(new ReloadCommand(this))
                .addChildCommand(new Whois(this))
                .addChildCommand(new Roll(this))
                .addChildCommand(new PrivateRoll(this))
                .addChildCommand(character);
        fallout.setPermission(new Permission("fallout.all", PermissionDefault.OP));
        commandController.addCommand(fallout);
    }

    @Override
    public void onDisable() {
        menuListener.destroy();
        menuListener = null;
        foListener.destroy();
        foListener = null;
        characterManager = null;
        commandController.destroy();
        commandController = null;
        messenger = null;
        configManager = null;
    }

    /**
     * Gets the {@link ninja.amp.fallout.config.ConfigManager}.
     *
     * @return The {@link ninja.amp.fallout.config.ConfigManager} instance.
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Gets the {@link ninja.amp.fallout.message.Messenger}.
     *
     * @return The {@link ninja.amp.fallout.message.Messenger} instance.
     */
    public Messenger getMessenger() {
        return messenger;
    }

    /**
     * Gets the {@link ninja.amp.fallout.command.CommandController}.
     *
     * @return The {@link ninja.amp.fallout.command.CommandController} instance.
     */
    public CommandController getCommandController() {
        return commandController;
    }

    /**
     * Gets the {@link ninja.amp.fallout.characters.CharacterManager}.
     *
     * @return The {@link ninja.amp.fallout.characters.CharacterManager} instance.
     */
    public CharacterManager getCharacterManager() {
        return characterManager;
    }

    /**
     * Gets the {@link ninja.amp.fallout.FOListener}.
     *
     * @return The {@link ninja.amp.fallout.FOListener} instance.
     */
    public FOListener getFoListener() {
        return foListener;
    }
}
