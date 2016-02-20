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
package ninja.amp.fallout.command.commands.character.skill;

import ninja.amp.fallout.FalloutCore;
import ninja.amp.fallout.character.Character;
import ninja.amp.fallout.command.commands.character.CharacterCommand;
import ninja.amp.fallout.menu.ItemMenu;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * A command that opens the skill allocation menu, letting the sender view and level up their character's skills.
 *
 * @author Austin Payne
 */
public class Skills extends CharacterCommand {

    private ItemMenu menu;

    public Skills(FalloutCore fallout) {
        super(fallout, "skills");
        setDescription("View and level up your breach character's skills.");
        setCommandUsage("/bl character skills");
        setPermission(new Permission("breach.character.skills", PermissionDefault.TRUE));

        this.menu = new SkillsMenu(fallout);
    }

    @Override
    public void execute(String command, Player sender, Character character, List<String> args) {
        menu.open(sender);
    }

}
