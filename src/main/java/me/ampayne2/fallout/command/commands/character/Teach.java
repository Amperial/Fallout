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
 * A command that teaches a character a skill.
 */
public class Teach extends FOCommand {
    private final Fallout fallout;

    public Teach(Fallout fallout) {
        super(fallout, "teach", "Teaches a character a skill.", "/fo teach <character> <skill>", new Permission("fallout.character.teach", PermissionDefault.OP), 2, true);
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
        } else if (character.hasSkill(skill)) {
            fallout.getMessenger().sendMessage(player, "error.character.skills.alreadytaught", character.getCharacterName());
        } else {
            character.teachSkill(skill);
            Player taughtPlayer = Bukkit.getPlayerExact(character.getOwnerName());
            if (taughtPlayer != null) {
                fallout.getMessenger().sendMessage(taughtPlayer, "character.learnskill", skill.getName());
            }
            fallout.getMessenger().sendMessage(player, "character.teachskill", skill.getName(), character.getCharacterName());
        }
        character.save(fallout.getConfigManager().getConfig(ConfigType.CHARACTER).getConfigurationSection("Characters." + character.getOwnerName()));
        fallout.getConfigManager().getConfigAccessor(ConfigType.CHARACTER).saveConfig();
    }
}
