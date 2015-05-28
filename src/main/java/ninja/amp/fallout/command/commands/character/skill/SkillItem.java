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
import ninja.amp.fallout.menus.events.ItemClickEvent;
import ninja.amp.fallout.menus.items.StaticMenuItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class SkillItem extends StaticMenuItem {
    private CharacterManager characterManager;
    private SkillsMenu menu;
    private Skill skill;

    public SkillItem(Fallout plugin, SkillsMenu menu, Skill skill, String displayName, ItemStack icon, String... lore) {
        super(displayName, icon, lore);

        this.characterManager = plugin.getCharacterManager();
        this.menu = menu;
        this.skill = skill;
    }

    @Override
    public ItemStack getFinalIcon(Player player) {
        ItemStack finalIcon = super.getFinalIcon(player);

        Map<Skill, Integer> skills = menu.getPendingSkills(characterManager.getCharacterByOwner(player.getUniqueId()));
        finalIcon.setAmount(skills.get(skill));

        return finalIcon;
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        Character character = characterManager.getCharacterByOwner(event.getPlayer().getUniqueId());
        Map<Skill, Integer> skills = menu.getPendingSkills(character);

        int totalPoints = character.getLevel() * 5;
        int allocatedPoints = 0;
        for (int i : skills.values()) {
            allocatedPoints += i - 1;
        }

        if (totalPoints > allocatedPoints && skills.get(skill) < 6) {
            skills.put(skill, skills.get(skill) + 1);
        }

        event.setWillUpdate(true);
    }

    /**
     * Gets the Skill represented by the SkillItem.
     *
     * @return The Skill.
     */
    public Skill getSkill() {
        return skill;
    }
}
