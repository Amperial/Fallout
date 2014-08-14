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
package ninja.amp.fallout.commands.character;

import ninja.amp.amplib.command.Command;
import ninja.amp.amplib.messenger.DefaultMessage;
import ninja.amp.fallout.Fallout;
import ninja.amp.fallout.characters.Character;
import ninja.amp.fallout.characters.CharacterManager;
import ninja.amp.fallout.characters.Special;
import ninja.amp.fallout.config.ConfigType;
import ninja.amp.fallout.message.FOMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.UUID;

/**
 * A command that sets the sender's character's SPECIAL traits.
 */
public class SetSpecial extends Command {
    private final Fallout fallout;

    public SetSpecial(Fallout fallout) {
        super(fallout, "setspecial");
        setDescription("Sets your fallout character's traits.");
        setCommandUsage("/fo character setspecial <s> <p> <e> <c> <i> <a> <l>");
        setPermission(new Permission("fallout.character.setspecial", PermissionDefault.TRUE));
        setArgumentRange(7, 7);
        this.fallout = fallout;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        Player player = (Player) sender;
        UUID playerId = player.getUniqueId();
        CharacterManager characterManager = fallout.getCharacterManager();
        if (characterManager.isOwner(playerId)) {
            Character character = characterManager.getCharacterByOwner(playerId);
            int s;
            int p;
            int e;
            int c;
            int i;
            int a;
            int l;
            try {
                s = Integer.parseInt(args[0]);
                p = Integer.parseInt(args[1]);
                e = Integer.parseInt(args[2]);
                c = Integer.parseInt(args[3]);
                i = Integer.parseInt(args[4]);
                a = Integer.parseInt(args[5]);
                l = Integer.parseInt(args[6]);
            } catch (NumberFormatException exception) {
                fallout.getMessenger().sendMessage(player, DefaultMessage.ERROR_NUMBERFORMAT);
                return;
            }
            Special special = new Special(s, p, e, c, i, a, l);
            if (character.getRace().isValid(special)) {
                character.setSpecial(special);
                fallout.getMessenger().sendMessage(player, FOMessage.SPECIAL_SET, character.getCharacterName());
                character.save(fallout.getConfigManager().getConfig(ConfigType.CHARACTER).getConfigurationSection("Characters." + playerId));
                fallout.getConfigManager().getConfigAccessor(ConfigType.CHARACTER).saveConfig();
            } else {
                fallout.getMessenger().sendMessage(player, FOMessage.SPECIAL_INVALID);
            }
        } else {
            fallout.getMessenger().sendMessage(player, FOMessage.CHARACTER_NOTOWNER);
        }
    }
}
