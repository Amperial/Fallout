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

import ninja.amp.fallout.character.CharacterManager;
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
import ninja.amp.fallout.command.commands.character.knowledge.Teach;
import ninja.amp.fallout.command.commands.character.knowledge.Unteach;
import ninja.amp.fallout.command.commands.character.perk.Perks;
import ninja.amp.fallout.command.commands.character.perk.ResetPerks;
import ninja.amp.fallout.command.commands.character.profile.Profile;
import ninja.amp.fallout.command.commands.character.skill.ResetSkills;
import ninja.amp.fallout.command.commands.character.skill.Skills;
import ninja.amp.fallout.command.commands.character.special.Special;
import ninja.amp.fallout.command.commands.character.special.SpecialMenu;
import ninja.amp.fallout.command.commands.radio.Radio;
import ninja.amp.fallout.command.commands.roll.ArmorRoll;
import ninja.amp.fallout.command.commands.roll.DiceRoll;
import ninja.amp.fallout.command.commands.roll.GlobalRoll;
import ninja.amp.fallout.command.commands.roll.LocalRoll;
import ninja.amp.fallout.command.commands.roll.PrivateRoll;
import ninja.amp.fallout.command.commands.roll.RollManager;
import ninja.amp.fallout.config.ConfigManager;
import ninja.amp.fallout.faction.FactionManager;
import ninja.amp.fallout.menu.MenuListener;
import ninja.amp.fallout.message.Messenger;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * The main class of the Fallout plugin.
 *
 * @author Austin Payne
 */
public class Fallout extends JavaPlugin implements FalloutCore {

    private ConfigManager configManager;
    private Messenger messenger;
    private CommandController commandController;
    private CharacterManager characterManager;
    private RollManager rollManager;
    private FactionManager factionManager;
    private FOListener foListener;
    private MenuListener menuListener;
    private Set<Plugin> disabledExtensions = new HashSet<>();

    @Override
    public void onEnable() {
        // The order managers are created in is important
        configManager = new ConfigManager(this);
        messenger = new Messenger(this);
        commandController = new CommandController(this);
        characterManager = new CharacterManager(this);
        rollManager = new RollManager(this);
        factionManager = new FactionManager(this);
        foListener = new FOListener(this);
        menuListener = new MenuListener(this);

        // Create fallout command tree

        // Character commands. These are added to both /fo and /fo character
        SpecialMenu specialMenu = new SpecialMenu(this);
        Command create = new Create(this, specialMenu);
        Command delete = new Delete(this);
        Command profile = new Profile(this);
        Command possess = new Possess(this);
        Command abandon = new Abandon(this);
        Command upgrade = new Upgrade(this);
        Command special = new Special(this, specialMenu);
        Command skills = new Skills(this);
        Command resetSkills = new ResetSkills(this);
        Command perks = new Perks(this);
        Command resetPerks = new ResetPerks(this);
        Command teach = new Teach(this);
        Command unteach = new Unteach(this);
        CommandGroup character = new CommandGroup(this, "character")
                .addChildCommand(create)
                .addChildCommand(delete)
                .addChildCommand(profile)
                .addChildCommand(possess)
                .addChildCommand(abandon)
                .addChildCommand(upgrade)
                .addChildCommand(special)
                .addChildCommand(skills)
                .addChildCommand(resetSkills)
                .addChildCommand(perks)
                .addChildCommand(resetPerks)
                .addChildCommand(teach)
                .addChildCommand(unteach);
        character.setPermission(new Permission("breach.character.all", PermissionDefault.OP));

        // Main /fo command tree
        CommandGroup fallout = new CommandGroup(this, "breach")
                .addChildCommand(new AboutCommand(this))
                .addChildCommand(new HelpCommand(this))
                .addChildCommand(new ReloadCommand(this))
                .addChildCommand(new Radio(this))
                .addChildCommand(new LocalRoll(this)
                        .addChildCommand(new ArmorRoll(this))
                        .addChildCommand(new DiceRoll(this)))
                .addChildCommand(new GlobalRoll(this))
                .addChildCommand(new PrivateRoll(this))
                .addChildCommand(new Whois(this))
                .addChildCommand(create)
                .addChildCommand(delete)
                .addChildCommand(profile)
                .addChildCommand(possess)
                .addChildCommand(abandon)
                .addChildCommand(upgrade)
                .addChildCommand(special)
                .addChildCommand(skills)
                .addChildCommand(resetSkills)
                .addChildCommand(perks)
                .addChildCommand(resetPerks)
                .addChildCommand(teach)
                .addChildCommand(unteach)
                .addChildCommand(character);
        fallout.setPermission(new Permission("breach.all", PermissionDefault.OP));

        // Add fallout command tree to command controller
        commandController.addCommand(fallout);

        // Enable any plugins that happen to depend on fallout and were disabled
        for (Plugin plugin : disabledExtensions) {
            if (!plugin.isEnabled()) {
                plugin.getPluginLoader().enablePlugin(plugin);
            }
        }
    }

    @Override
    public void onDisable() {
        // Disable any plugins that happen to depend on fallout
        for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
            if (plugin.getDescription().getDepend().contains("Fallout") && plugin.isEnabled()) {
                getServer().getPluginManager().disablePlugin(plugin);
                disabledExtensions.add(plugin);
            }
        }

        // The order managers are destroyed in is not important
        MenuListener.closeOpenMenus();
        menuListener = null;
        foListener = null;
        factionManager = null;
        rollManager = null;
        characterManager = null;
        commandController.unregisterCommands();
        commandController = null;
        messenger = null;
        configManager = null;
    }

    @Override
    public JavaPlugin getPlugin() {
        return this;
    }

    @Override
    public ConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    public Messenger getMessenger() {
        return messenger;
    }

    @Override
    public CommandController getCommandController() {
        return commandController;
    }

    @Override
    public CharacterManager getCharacterManager() {
        return characterManager;
    }

    @Override
    public RollManager getRollManager() {
        return rollManager;
    }

    @Override
    public FactionManager getFactionManager() {
        return factionManager;
    }

    /**
     * Gets the fallout listener.
     *
     * @return The fallout listener
     */
    public FOListener getFoListener() {
        return foListener;
    }

    /**
     * Gets the unique id and name of all online players.
     *
     * @return The map of player id and names
     */
    public static Map<UUID, String> getOnlinePlayers() {
        return FOListener.onlinePlayers == null ? null : new HashMap<>(FOListener.onlinePlayers);
    }

}
