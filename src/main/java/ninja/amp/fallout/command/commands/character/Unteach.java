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
package ninja.amp.fallout.command.commands.character;

import ninja.amp.fallout.Fallout;
import ninja.amp.fallout.characters.Perk;
import ninja.amp.fallout.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * A command that unteaches a player a skill.
 */
public class Unteach extends Command {

    public Unteach(Fallout plugin) {
        super(plugin, "unteach");
        setDescription("Unteaches a character a perk.");
        setCommandUsage("/fo character unteach <character> <perk>");
        setPermission(new Permission("fallout.character.unteach", PermissionDefault.OP));
        setArgumentRange(2, 2);
        setPlayerOnly(false);
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
//        Character character;
//        CharacterManager characterManager = plugin.getCharacterManager();
//        if (characterManager.isCharacter(args[0])) {
//            character = characterManager.getCharacterByName(args[0]);
//        } else {
//            plugin.getMessenger().sendMessage(sender, FOMessage.CHARACTER_DOESNTEXIST);
//            return;
//        }
//        Perk perk = Perk.fromName(args[1]);
//        if (perk == null) {
//            plugin.getMessenger().sendMessage(sender, FOMessage.PERK_DOESNTEXIST, args[1]);
//        } else if (!character.hasPerk(perk)) {
//            plugin.getMessenger().sendMessage(sender, FOMessage.PERK_NOTTAUGHT, character.getCharacterName());
//        } else {
//            character.unteachPerk(perk);
//            Player taughtPlayer = Bukkit.getPlayerExact(character.getOwnerName());
//            if (taughtPlayer != null) {
//                plugin.getMessenger().sendMessage(taughtPlayer, FOMessage.PERK_FORGET, perk.getName());
//            }
//            plugin.getMessenger().sendMessage(sender, FOMessage.PERK_UNTEACH, perk.getName(), character.getCharacterName());
//        }
//        character.save(plugin.getConfigManager().getConfig(ConfigType.CHARACTER).getConfigurationSection("Characters." + character.getOwnerId()));
//        plugin.getConfigManager().getConfigAccessor(ConfigType.CHARACTER).saveConfig();
    }

    @Override
    public List<String> getTabCompleteList(String[] args) {
        if (args.length == 0) {
            return plugin.getCharacterManager().getCharacterList();
        } else if (args.length == 1) {
            return Perk.getPerkNames();
        } else {
            return new ArrayList<>();
        }
    }
}
