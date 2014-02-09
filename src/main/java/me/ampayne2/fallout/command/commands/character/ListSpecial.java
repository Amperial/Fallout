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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that lists the sender's character's SPECIAL traits.
 */
public class ListSpecial extends FOCommand {
    private final Fallout fallout;

    public ListSpecial(Fallout fallout) {
        super(fallout, "listspecial", "Lists your or another fallout character's traits.", "/fo character listspecial [player]", new Permission("fallout.character.listspecial", PermissionDefault.TRUE), 0, 1, true);
        this.fallout = fallout;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Character character = null;
        CharacterManager characterManager = fallout.getCharacterManager();
        if (args.length == 1) {
            if (characterManager.isCharacter(args[0])) {
                character = characterManager.getCharacterByName(args[0]);
            }
        } else {
            if (characterManager.isOwner(player.getName())) {
                character = characterManager.getCharacterByOwner(player.getName());
            }
        }
        if (character != null) {
            int s = character.getTrait(Trait.STRENGTH);
            int p = character.getTrait(Trait.PERCEPTION);
            int e = character.getTrait(Trait.ENDURANCE);
            int c = character.getTrait(Trait.CHARISMA);
            int i = character.getTrait(Trait.INTELLIGENCE);
            int a = character.getTrait(Trait.AGILITY);
            int l = character.getTrait(Trait.LUCK);
            fallout.getMessenger().sendMessage(player, "character.listspecial", character.getCharacterName(), Integer.toString(s), Integer.toString(p), Integer.toString(e), Integer.toString(c), Integer.toString(i), Integer.toString(a), Integer.toString(l));
        } else {
            if (args.length == 1) {
                fallout.getMessenger().sendMessage(player, "error.character.other.doesntexist");
            } else {
                fallout.getMessenger().sendMessage(player, "error.character.own.doesntexist");
            }
        }
    }
}
