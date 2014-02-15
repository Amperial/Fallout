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
package me.ampayne2.fallout;

import me.ampayne2.fallout.characters.CharacterManager;
import me.ampayne2.fallout.characters.Skill;
import me.ampayne2.fallout.utils.ArmorMaterial;
import me.ampayne2.fallout.utils.ArmorType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Handles various fallout events.
 */
public class FOListener implements Listener {
    private Fallout fallout;

    /**
     * Creates a new FOListener.
     *
     * @param fallout The {@link me.ampayne2.fallout.Fallout} instance.
     */
    public FOListener(Fallout fallout) {
        this.fallout = fallout;

        Bukkit.getPluginManager().registerEvents(this, fallout);
    }

    /**
     * Lists the SPECIAL traits of the character of a player right clicked while crouching.
     */
    @EventHandler
    public void onPlayerClickPlayer(PlayerInteractEntityEvent event) {
        if (event.getPlayer().isSneaking() && event.getRightClicked() instanceof Player) {
            String clickedName = ((Player) event.getRightClicked()).getName();

            CharacterManager characterManager = fallout.getCharacterManager();
            if (characterManager.isOwner(clickedName)) {
                event.getPlayer().performCommand("fo character listspecial " + characterManager.getCharacterByOwner(clickedName).getCharacterName());
            }
        }
    }

    /**
     * Stops players with characters who don't have the armor skill from right clicking to equip diamond armor.
     */
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerClickArmor(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack itemStack = player.getItemInHand();
            if (itemStack != null) {
                Material material = itemStack.getType();
                if (ArmorType.isArmor(material) && ArmorMaterial.getArmorMaterial(material).equals(ArmorMaterial.DIAMOND) && ArmorType.getArmorType(material).canEquip(player)) {
                    String ownerName = player.getName();
                    CharacterManager characterManager = fallout.getCharacterManager();
                    if (characterManager.isOwner(ownerName) && !characterManager.getCharacterByOwner(ownerName).hasSkill(Skill.ARMOR)) {
                        event.setCancelled(true);
                        player.updateInventory();
                        fallout.getMessenger().sendMessage(player, "error.character.skills.missingrequired", Skill.ARMOR.getName());
                    }
                }
            }
        }
    }

    /**
     * Stops players with characters who don't have the armor skill from manually equipping diamond armor.
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            boolean moving = false;
            ClickType clickType = event.getClick();
            if (event.isLeftClick() || event.isRightClick()) {
                if (event.isShiftClick()) {
                    ItemStack item = event.getCurrentItem();
                    if (item != null) {
                        Material material = item.getType();
                        moving = ArmorType.isArmor(material) && ArmorMaterial.getArmorMaterial(material).equals(ArmorMaterial.DIAMOND) && ArmorType.getArmorType(material).canEquip(player);
                    }
                } else if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
                    ItemStack item = event.getCursor();
                    if (item != null) {
                        Material material = item.getType();
                        moving = ArmorType.isArmor(material) && ArmorMaterial.getArmorMaterial(material).equals(ArmorMaterial.DIAMOND);
                    }
                }
            }
            if (moving) {
                String ownerName = player.getName();
                CharacterManager characterManager = fallout.getCharacterManager();
                if (characterManager.isOwner(ownerName) && !characterManager.getCharacterByOwner(ownerName).hasSkill(Skill.ARMOR)) {
                    event.setCancelled(true);
                    fallout.getMessenger().sendMessage(player, "error.character.skills.missingrequired", Skill.ARMOR.getName());
                }
            }
        }
    }

    /**
     * Stops players with characters who don't have the armor skill from manually equipping diamond armor.
     */
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getWhoClicked() instanceof Player && event.getOldCursor() != null) {
            Material material = event.getOldCursor().getType();
            if (ArmorType.isArmor(material) && ArmorMaterial.getArmorMaterial(material).equals(ArmorMaterial.DIAMOND)) {
                Player player = (Player) event.getWhoClicked();
                String ownerName = player.getName();
                CharacterManager characterManager = fallout.getCharacterManager();
                if (characterManager.isOwner(ownerName) && !characterManager.getCharacterByOwner(ownerName).hasSkill(Skill.ARMOR)) {
                    event.setCancelled(true);
                    fallout.getMessenger().sendMessage(player, "error.character.skills.missingrequired", Skill.ARMOR.getName());
                }
            }
        }
    }
}
