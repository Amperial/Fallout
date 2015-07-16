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
package ninja.amp.fallout.command;

import ninja.amp.fallout.FalloutCore;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * A command that can contain child commands and be executed.
 *
 * @author Austin Payne
 */
public abstract class Command extends CommandGroup {

    private String commandUsage;
    private String description;
    private boolean visible = true;

    /**
     * Creates a new command.
     *
     * @param fallout The fallout plugin core
     * @param name    The name of the command
     */
    public Command(FalloutCore fallout, String name) {
        super(fallout, name);
    }

    /**
     * Gets the command's command usage.
     *
     * @return The command's command usage
     */
    public String getCommandUsage() {
        return commandUsage;
    }

    /**
     * Sets the command's command usage.
     *
     * @param commandUsage The command usage
     */
    public void setCommandUsage(String commandUsage) {
        this.commandUsage = commandUsage;
    }

    /**
     * Gets the command's description.
     *
     * @return The command's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the command's description.
     *
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the command's visibility.
     *
     * @return The command's visibility
     */
    public boolean getVisible() {
        return visible;
    }

    /**
     * Sets the command's visibility.
     *
     * @param visible The visibility
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public abstract void execute(String command, CommandSender sender, List<String> args);

}
