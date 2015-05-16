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
import ninja.amp.fallout.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that sets the sender's character's SPECIAL traits.
 */
public class SetSpecial extends Command {

    public SetSpecial(Fallout plugin) {
        super(plugin, "setspecial");
        setDescription("Sets your fallout character's traits.");
        setCommandUsage("/fo character setspecial");
        setPermission(new Permission("fallout.character.setspecial", PermissionDefault.TRUE));
        setArgumentRange(7, 7);
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
//        Player player = (Player) sender;
//        UUID playerId = player.getUniqueId();
//        CharacterManager characterManager = plugin.getCharacterManager();
//        if (characterManager.isOwner(playerId)) {
//            Character character = characterManager.getCharacterByOwner(playerId);
//            int[] s = new int[7];
//            try {
//                for (int i = 0; i < 7; i++) {
//                    s[i] = Integer.parseInt(args[i]);
//                }
//            } catch (NumberFormatException exception) {
//                plugin.getMessenger().sendMessage(player, FOMessage.ERROR_NUMBERFORMAT);
//                return;
//            }
//            Special special = new Special(s[0], s[1], s[2], s[3], s[4], s[5], s[6]);
//            if (character.getRace().isValid(special)) {
//                character.getSpecial().set(special);
//                plugin.getMessenger().sendMessage(player, FOMessage.SPECIAL_SET, character.getCharacterName());
//                character.save(plugin.getConfigManager().getConfig(ConfigType.CHARACTER).getConfigurationSection("Characters." + playerId));
//                plugin.getConfigManager().getConfigAccessor(ConfigType.CHARACTER).saveConfig();
//            } else {
//                plugin.getMessenger().sendMessage(player, FOMessage.SPECIAL_INVALID);
//            }
//        } else {
//            plugin.getMessenger().sendMessage(player, FOMessage.CHARACTER_NOTOWNER);
//        }
    }
}
