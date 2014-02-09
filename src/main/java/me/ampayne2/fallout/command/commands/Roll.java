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
package me.ampayne2.fallout.command.commands;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A command that lets you roll for a random number based on a trait's level.
 */
public class Roll extends FOCommand {
    private final Fallout fallout;
    private static final Random RANDOM = new Random();

    public Roll(Fallout fallout) {
        super(fallout, "roll", "Rolls the dice with one of your character's traits.", "/fo roll <trait>", new Permission("fallout.roll", PermissionDefault.TRUE), 1, true);
        this.fallout = fallout;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        Player player = (Player) sender;
        String ownerName = player.getName();
        Trait trait = Trait.fromName(args[0]);
        if (trait == null) {
            fallout.getMessenger().sendMessage(player, "error.roll.notatrait", args[0]);
        } else {
            CharacterManager characterManager = fallout.getCharacterManager();
            if (characterManager.isOwner(ownerName)) {
                Character character = characterManager.getCharacterByOwner(ownerName);
                int rolls = character.getTrait(trait);
                int sides = fallout.getConfigManager().getConfig(ConfigType.PLUGIN).getInt("dicerolls", 20);
                int total = 0;
                for (int i = 0; i < rolls; i++) {
                    total += RANDOM.nextInt(sides + 1) + 1;
                }
                fallout.getMessenger().sendMessage(fallout.getServer(), "roll.broadcast", character.getCharacterName(), "" + total);
            } else {
                fallout.getMessenger().sendMessage(player, "error.character.own.doesntexist");
            }
        }
    }

    @Override
    public List<String> getTabCompleteList(String[] args) {
        if (args.length == 0) {
            return Trait.getTraitNames();
        } else {
            return new ArrayList<>();
        }
    }
}
