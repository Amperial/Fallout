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

import ninja.amp.fallout.Fallout;
import ninja.amp.fallout.characters.Character;
import ninja.amp.fallout.characters.CharacterManager;
import ninja.amp.fallout.characters.Skill;
import ninja.amp.fallout.menus.items.StaticMenuItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;

public class SkillPointsItem extends StaticMenuItem {
    private CharacterManager characterManager;
    private SkillsMenu menu;
    private ItemStack noPoints;

    public SkillPointsItem(Fallout plugin, SkillsMenu menu) {
        super(ChatColor.AQUA + "Allocation Points",
                new ItemStack(Material.DIAMOND),
                "The skill points",
                "currently available",
                "for allocation");

        this.characterManager = plugin.getCharacterManager();
        this.menu = menu;

        noPoints = new ItemStack(Material.STONE_BUTTON);
        setNameAndLore(noPoints, "No Allocation Points", new ArrayList<String>());
    }

    @Override
    public ItemStack getFinalIcon(Player player) {
        ItemStack finalIcon = super.getFinalIcon(player);

        Character character = characterManager.getCharacterByOwner(player.getUniqueId());
        Map<Skill, Integer> skills = menu.getPendingSkills(character.getOwnerId());

        int totalPoints = character.getLevel() * 5;
        int allocatedPoints = 0;
        for (int i : skills.values()) {
            allocatedPoints += i - 1;
        }

        if (totalPoints > allocatedPoints) {
            finalIcon.setAmount(totalPoints - allocatedPoints);
        } else {
            finalIcon = noPoints;
        }

        return finalIcon;
    }

}
