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
import ninja.amp.fallout.menus.ItemMenu;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Inventory menu used for viewing and increasing a player's skill levels.
 */
public class SkillsMenu extends ItemMenu {
    private CharacterManager characterManager;
    private Map<Character, Map<Skill, Integer>> pendingSkills = new HashMap<>();

    @SuppressWarnings("deprecation")
    public SkillsMenu(Fallout plugin) {
        super("Skill Allocation", Size.FIVE_LINE, plugin);

        this.characterManager = plugin.getCharacterManager();

        setItem(41, new SkillsConfirmItem(plugin, this));
        setItem(40, new SkillPointsItem(plugin, this));
        setItem(39, new SkillsCancelItem(plugin, this));

        ItemStack icon = new ItemStack(Material.WOOL, 1, DyeColor.LIME.getWoolData());
        setItem(0, new SkillItem(plugin, this, Skill.BIG_GUNS, "Big Guns", icon));
        setItem(1, new SkillItem(plugin, this, Skill.CONVENTIONAL_GUNS, "Conventional Guns", icon));
        setItem(2, new SkillItem(plugin, this, Skill.ENERGY_WEAPONS, "Energy Weapons", icon));
        setItem(3, new SkillItem(plugin, this, Skill.MELEE_WEAPONS, "Melee Weapons", icon));

        setItem(5, new SkillItem(plugin, this, Skill.LOCKPICKING, "Lockpicking", icon));
        setItem(6, new SkillItem(plugin, this, Skill.SNEAK, "Sneak", icon));

        setItem(8, new SkillItem(plugin, this, Skill.SPEECH, "Speech", icon));

        setItem(18, new SkillItem(plugin, this, Skill.EXPLOSIVES, "Explosives", icon));
        setItem(19, new SkillItem(plugin, this, Skill.UNARMED, "Unarmed", icon));

        setItem(21, new SkillItem(plugin, this, Skill.FIRST_AID, "First Aid", icon));
        setItem(22, new SkillItem(plugin, this, Skill.SURGERY, "Surgery", icon));
        setItem(23, new SkillItem(plugin, this, Skill.REPAIR, "Repair", icon));

        setItem(25, new SkillItem(plugin, this, Skill.SCIENCE, "Science", icon));
        setItem(26, new SkillItem(plugin, this, Skill.LOGICAL_THINKING, "Logical Thinking", icon));

        fillEmptySlots();
    }

    @Override
    public void open(Player player) {
        Character character = characterManager.getCharacterByOwner(player.getUniqueId());
        pendingSkills.put(character, new HashMap<>(character.getSkills()));

        super.open(player);
    }

    /**
     * Gets the pending skill levels of a character.
     *
     * @param character The character.
     * @return The character's modified skill levels.
     */
    public Map<Skill, Integer> getPendingSkills(Character character) {
        return pendingSkills.get(character);
    }

    /**
     * Resets a character's pending skill levels.
     *
     * @param character The character.
     */
    public void resetPendingSkills(Character character) {
        pendingSkills.remove(character);
    }
}
