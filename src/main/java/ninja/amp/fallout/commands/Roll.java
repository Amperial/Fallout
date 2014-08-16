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
package ninja.amp.fallout.commands;

import ninja.amp.amplib.command.Command;
import ninja.amp.fallout.Fallout;
import ninja.amp.fallout.characters.Character;
import ninja.amp.fallout.characters.CharacterManager;
import ninja.amp.fallout.characters.Special;
import ninja.amp.fallout.characters.Trait;
import ninja.amp.fallout.message.FOMessage;
import ninja.amp.fallout.utils.ArmorMaterial;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * A command that lets you roll for a random number based on a trait's level.
 */
public class Roll extends Command {
    private final Fallout fallout;
    private static final Random RANDOM = new Random();

    public Roll(Fallout fallout) {
        super(fallout, "roll");
        setDescription("Rolls the dice with one of your character's traits.");
        setCommandUsage("/fo roll <trait>");
        setPermission(new Permission("fallout.roll", PermissionDefault.TRUE));
        setArgumentRange(1, 1);
        this.fallout = fallout;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        Player player = (Player) sender;
        UUID playerId = player.getUniqueId();
        Trait trait = Trait.fromName(args[0]);
        if (trait == null) {
            fallout.getMessenger().sendMessage(player, FOMessage.ROLL_NOTATRAIT, args[0]);
        } else {
            CharacterManager characterManager = fallout.getCharacterManager();
            if (characterManager.isOwner(playerId)) {
                Character character = characterManager.getCharacterByOwner(playerId);
                int roll = RANDOM.nextInt(fallout.getConfig().getInt("DiceSides", 20) + 1) + 1;
                roll += Trait.getRollModifier(character.getSpecial().get(trait));
                if (ArmorMaterial.isWearingFullSet(player)) {
                    Special armorModifier = ArmorMaterial.getArmorMaterial(player.getInventory().getHelmet().getType()).getRollModifier();
                    roll += armorModifier.get(trait);
                }
                fallout.getMessenger().sendMessage(fallout.getServer(), FOMessage.ROLL_BROADCAST, character.getCharacterName(), roll, trait.getName());
            } else {
                fallout.getMessenger().sendMessage(player, FOMessage.CHARACTER_NOTOWNER);
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
