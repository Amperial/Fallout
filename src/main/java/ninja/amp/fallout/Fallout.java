/*
 * This file is part of Fallout.
 *
 * Copyright (c) 2013-2014 <http://github.com/ampayne2/Fallout//>
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

import ninja.amp.amplib.AmpJavaPlugin;
import ninja.amp.amplib.command.Command;
import ninja.amp.amplib.command.CommandGroup;
import ninja.amp.amplib.command.commands.AboutCommand;
import ninja.amp.amplib.command.commands.HelpCommand;
import ninja.amp.amplib.command.commands.ReloadCommand;
import ninja.amp.amplib.messenger.DefaultMessage;
import ninja.amp.amplib.messenger.Messenger;
import ninja.amp.fallout.characters.CharacterManager;
import ninja.amp.fallout.commands.Roll;
import ninja.amp.fallout.commands.Whois;
import ninja.amp.fallout.commands.character.Create;
import ninja.amp.fallout.commands.character.Delete;
import ninja.amp.fallout.commands.character.ListPerks;
import ninja.amp.fallout.commands.character.ListSpecial;
import ninja.amp.fallout.commands.character.SetSpecial;
import ninja.amp.fallout.commands.character.Teach;
import ninja.amp.fallout.commands.character.Unteach;
import ninja.amp.fallout.config.ConfigType;
import ninja.amp.fallout.message.FOMessage;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.EnumSet;

/**
 * The main class of the Fallout plugin.
 */
public class Fallout extends AmpJavaPlugin {
    private CharacterManager characterManager;
    private FOListener foListener;

    @Override
    public void onEnable() {
        DefaultMessage.PREFIX.setMessage("&c[&eFallout&c] &e");
        DefaultMessage.RELOAD.setMessage("Reloaded Fallout.");
        enableAmpLib();
        getConfigManager().registerConfigTypes(EnumSet.allOf(ConfigType.class));
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        Messenger.PRIMARY_COLOR = ChatColor.valueOf(config.getString("colors.primary", "YELLOW"));
        Messenger.SECONDARY_COLOR = ChatColor.valueOf(config.getString("colors.secondary", "GRAY"));
        Messenger.HIGHLIGHT_COLOR = ChatColor.valueOf(config.getString("colors.highlights", "RED"));
        config.set("configversion", getDescription().getVersion());
        saveConfig();
        getMessenger().registerMessages(EnumSet.allOf(FOMessage.class));
        characterManager = new CharacterManager(this);

        CommandGroup character = new CommandGroup(this, "character")
                .addChildCommand(new Create(this))
                .addChildCommand(new Delete(this))
                .addChildCommand(new ListSpecial(this))
                .addChildCommand(new SetSpecial(this))
                .addChildCommand(new Teach(this))
                .addChildCommand(new Unteach(this))
                .addChildCommand(new ListPerks(this));
        character.setPermission(new Permission("fallout.character.all", PermissionDefault.OP));
        Command about = new AboutCommand(this);
        about.setCommandUsage("/fo");
        Command help = new HelpCommand(this);
        help.setCommandUsage("/fo help");
        Command reload = new ReloadCommand(this);
        reload.setCommandUsage("/fo reload");
        CommandGroup fallout = new CommandGroup(this, "fallout")
                .addChildCommand(about)
                .addChildCommand(help)
                .addChildCommand(reload)
                .addChildCommand(new Whois(this))
                .addChildCommand(new Roll(this))
                .addChildCommand(character);
        fallout.setPermission(new Permission("fallout.all", PermissionDefault.OP));
        getCommandController().addCommand(fallout);

        foListener = new FOListener(this);
    }

    @Override
    public void onDisable() {
        foListener.destroy();
        foListener = null;
        characterManager.destroy();
        characterManager = null;
        disableAmpLib();
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
