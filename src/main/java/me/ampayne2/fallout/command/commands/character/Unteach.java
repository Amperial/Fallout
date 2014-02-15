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
package me.ampayne2.fallout.command.commands.character;

import me.ampayne2.fallout.Fallout;
import me.ampayne2.fallout.characters.Character;
import me.ampayne2.fallout.characters.CharacterManager;
import me.ampayne2.fallout.characters.Skill;
import me.ampayne2.fallout.command.FOCommand;
import me.ampayne2.fallout.config.ConfigType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that unteaches a player a skill.
 */
public class Unteach extends FOCommand {
    private final Fallout fallout;

    public Unteach(Fallout fallout) {
        super(fallout, "unteach", "Unteaches a character a skill.", "/fo unteach <character> <skill>", new Permission("fallout.character.unteach", PermissionDefault.OP), 2, true);
        this.fallout = fallout;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Character character;
        CharacterManager characterManager = fallout.getCharacterManager();
        if (characterManager.isCharacter(args[0])) {
            character = characterManager.getCharacterByName(args[0]);
        } else {
            fallout.getMessenger().sendMessage(player, "error.character.other.doesntexist");
            return;
        }
        Skill skill = Skill.fromName(args[1]);
        if (skill == null) {
            fallout.getMessenger().sendMessage(player, "error.character.skills.doesntexist", args[1]);
        } else if (!character.hasSkill(skill)) {
            fallout.getMessenger().sendMessage(player, "error.character.skills.nottaught", character.getCharacterName());
        } else {
            character.unteachSkill(skill);
            Player taughtPlayer = Bukkit.getPlayerExact(character.getOwnerName());
            if (taughtPlayer != null) {
                fallout.getMessenger().sendMessage(taughtPlayer, "character.forgetskill", skill.getName());
            }
            fallout.getMessenger().sendMessage(player, "character.unteachskill", skill.getName(), character.getCharacterName());
        }
        character.save(fallout.getConfigManager().getConfig(ConfigType.CHARACTER).getConfigurationSection("Characters." + character.getOwnerName()));
        fallout.getConfigManager().getConfigAccessor(ConfigType.CHARACTER).saveConfig();
    }
}
