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
import me.ampayne2.fallout.characters.Trait;
import me.ampayne2.fallout.command.FOCommand;
import me.ampayne2.fallout.config.ConfigType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that sets the sender's character's SPECIAL traits.
 */
public class SetSpecial extends FOCommand {
    private final Fallout fallout;

    public SetSpecial(Fallout fallout) {
        super(fallout, "setspecial", "Sets your fallout character's traits.", "/fo character setspecial <s> <p> <e> <c> <i> <a> <l>", new Permission("fallout.character.setspecial", PermissionDefault.TRUE), 7, true);
        this.fallout = fallout;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        Player player = (Player) sender;
        String playerName = player.getName();
        CharacterManager characterManager = fallout.getCharacterManager();
        if (characterManager.hasCharacter(playerName)) {
            Character character = characterManager.getCharacter(playerName);
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
                fallout.getMessenger().sendMessage(player, "error.numberformat");
                return;
            }
            if ((s + p + e + c + i + a + l) != 40) {
                fallout.getMessenger().sendMessage(player, "error.character.points");
            } else if (s < 0 || p < 0 || e < 0 || c < 0 || i < 0 || a < 0 || l < 0) {
                fallout.getMessenger().sendMessage(player, "error.numbertoolow", "0");
            } else {
                character.setTrait(Trait.STRENGTH, s);
                character.setTrait(Trait.PERCEPTION, p);
                character.setTrait(Trait.ENDURANCE, e);
                character.setTrait(Trait.CHARISMA, c);
                character.setTrait(Trait.INTELLIGENCE, i);
                character.setTrait(Trait.AGILITY, a);
                character.setTrait(Trait.LUCK, l);
                fallout.getMessenger().sendMessage(player, "character.setspecial", character.getCharacterName());
                character.save(fallout.getConfigManager().getConfig(ConfigType.CHARACTER).getConfigurationSection("Characters." + playerName));
                fallout.getConfigManager().getConfigAccessor(ConfigType.CHARACTER).saveConfig();
            }
        } else {
            fallout.getMessenger().sendMessage(player, "error.character.doesntexist");
        }
    }
}
