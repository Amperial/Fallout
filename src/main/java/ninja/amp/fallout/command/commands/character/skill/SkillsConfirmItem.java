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
import ninja.amp.fallout.characters.Skill;
import ninja.amp.fallout.menus.events.ItemClickEvent;
import ninja.amp.fallout.menus.items.StaticMenuItem;
import ninja.amp.fallout.message.FOMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class SkillsConfirmItem extends StaticMenuItem {
    private Fallout plugin;
    private SkillsMenu menu;

    public SkillsConfirmItem(Fallout plugin, SkillsMenu menu) {
        super(ChatColor.GREEN + "Confirm Skill Allocation",
                new ItemStack(Material.EMERALD_BLOCK),
                "THIS IS PERMANENT");

        this.plugin = plugin;
        this.menu = menu;
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        Character character = plugin.getCharacterManager().getCharacterByOwner(playerId);
        Map<Skill, Integer> skills = menu.getPendingSkills(playerId);

        for (Map.Entry<Skill, Integer> skill : skills.entrySet()) {
            character.setSkillLevel(skill.getKey(), skill.getValue());
        }
        plugin.getCharacterManager().saveCharacter(character);
        plugin.getMessenger().sendMessage(player, FOMessage.SKILLS_CONFIRM);

        menu.resetPendingSkills(playerId);
        event.setWillClose(true);
    }
}
