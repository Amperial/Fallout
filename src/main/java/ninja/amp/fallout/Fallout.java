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
import ninja.amp.fallout.command.Command;
import ninja.amp.fallout.command.CommandController;
import ninja.amp.fallout.command.CommandGroup;
import ninja.amp.fallout.command.commands.AboutCommand;
import ninja.amp.fallout.command.commands.HelpCommand;
import ninja.amp.fallout.command.commands.ReloadCommand;
import ninja.amp.fallout.command.commands.Whois;
import ninja.amp.fallout.command.commands.character.Abandon;
import ninja.amp.fallout.command.commands.character.Delete;
import ninja.amp.fallout.command.commands.character.Possess;
import ninja.amp.fallout.command.commands.character.Upgrade;
import ninja.amp.fallout.command.commands.character.creation.Create;
import ninja.amp.fallout.command.commands.character.perk.Perks;
import ninja.amp.fallout.command.commands.character.perk.ResetPerks;
import ninja.amp.fallout.command.commands.character.skill.ResetSkills;
import ninja.amp.fallout.command.commands.character.skill.Skills;
import ninja.amp.fallout.command.commands.character.special.ListSpecial;
import ninja.amp.fallout.command.commands.character.special.SetSpecial;
import ninja.amp.fallout.command.commands.character.special.SpecialMenu;
import ninja.amp.fallout.command.commands.roll.ArmorRoll;
import ninja.amp.fallout.command.commands.roll.DiceRoll;
import ninja.amp.fallout.command.commands.roll.GlobalRoll;
import ninja.amp.fallout.command.commands.roll.LocalRoll;
import ninja.amp.fallout.command.commands.roll.PrivateRoll;
import ninja.amp.fallout.command.commands.roll.RollManager;
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
    private RollManager rollManager;

    @Override
    public void onEnable() {
        // The order managers are created in is important
        configManager = new ConfigManager(this);
        messenger = new Messenger(this);
        commandController = new CommandController(this);
        characterManager = new CharacterManager(this);
        rollManager = new RollManager(this);
        foListener = new FOListener(this);
        menuListener = new MenuListener(this);

        // Create fallout command tree

        // Character commands. These are added to both /fo and /fo character
        SpecialMenu specialMenu = new SpecialMenu(this);
        Command create = new Create(this, specialMenu);
        Command delete = new Delete(this);
        Command possess = new Possess(this);
        Command abandon = new Abandon(this);
        Command upgrade = new Upgrade(this);
        Command listSpecial = new ListSpecial(this);
        Command setSpecial = new SetSpecial(this, specialMenu);
        Command skills = new Skills(this);
        Command resetSkills = new ResetSkills(this);
        Command perks = new Perks(this);
        Command resetPerks = new ResetPerks(this);
        CommandGroup character = new CommandGroup(this, "character")
                .addChildCommand(create)
                .addChildCommand(delete)
                .addChildCommand(possess)
                .addChildCommand(abandon)
                .addChildCommand(upgrade)
                .addChildCommand(listSpecial)
                .addChildCommand(setSpecial)
                .addChildCommand(skills)
                .addChildCommand(resetSkills)
                .addChildCommand(perks)
                .addChildCommand(resetPerks);
        character.setPermission(new Permission("fallout.character.all", PermissionDefault.OP));

        // Main /fo command tree
        CommandGroup fallout = new CommandGroup(this, "fallout")
                .addChildCommand(new AboutCommand(this))
                .addChildCommand(new HelpCommand(this))
                .addChildCommand(new ReloadCommand(this))
                .addChildCommand(new LocalRoll(this)
                        .addChildCommand(new ArmorRoll(this))
                        .addChildCommand(new DiceRoll(this)))
                .addChildCommand(new GlobalRoll(this))
                .addChildCommand(new PrivateRoll(this))
                .addChildCommand(new Whois(this))
                .addChildCommand(create)
                .addChildCommand(delete)
                .addChildCommand(possess)
                .addChildCommand(abandon)
                .addChildCommand(upgrade)
                .addChildCommand(listSpecial)
                .addChildCommand(setSpecial)
                .addChildCommand(skills)
                .addChildCommand(resetSkills)
                .addChildCommand(perks)
                .addChildCommand(resetPerks)
                .addChildCommand(character);
        fallout.setPermission(new Permission("fallout.all", PermissionDefault.OP));

        // Add fallout command tree to command controller
        commandController.addCommand(fallout);
    }

    @Override
    public void onDisable() {
        // The order managers are destroyed in is not important
        menuListener.destroy();
        menuListener = null;
        foListener.destroy();
        foListener = null;
        rollManager = null;
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
     * Gets the {@link ninja.amp.fallout.command.commands.roll.RollManager}.
     *
     * @return The {@link ninja.amp.fallout.command.commands.roll.RollManager} instance.
     */
    public RollManager getRollManager() {
        return rollManager;
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
