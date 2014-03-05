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

import me.ampayne2.fallout.characters.Character;
import me.ampayne2.fallout.characters.CharacterManager;
import me.ampayne2.fallout.characters.Perk;
import me.ampayne2.fallout.utils.ArmorMaterial;
import me.ampayne2.fallout.utils.ArmorType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

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
     * Attempts to load the player's character.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        fallout.getCharacterManager().loadCharacter(event.getPlayer());
    }

    /**
     * Unloads the player's character from the character manager if the player has a character.
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        CharacterManager characterManager = fallout.getCharacterManager();
        Character character = characterManager.getCharacterByOwner(event.getPlayer().getUniqueId());
        if (character != null) {
            characterManager.unloadCharacter(character);
        }
    }

    /**
     * Lists the SPECIAL traits of the character of a player right clicked while crouching.
     */
    @EventHandler
    public void onPlayerClickPlayer(PlayerInteractEntityEvent event) {
        if (event.getPlayer().isSneaking() && event.getRightClicked() instanceof Player) {
            UUID clickedId = event.getRightClicked().getUniqueId();

            CharacterManager characterManager = fallout.getCharacterManager();
            if (characterManager.isOwner(clickedId)) {
                event.getPlayer().performCommand("fo character listspecial " + characterManager.getCharacterByOwner(clickedId).getCharacterName());
            }
        }
    }

    /**
     * Stops players with characters who don't have the armor perk from right clicking to equip diamond armor.
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
                    UUID playerId = player.getUniqueId();
                    CharacterManager characterManager = fallout.getCharacterManager();
                    if (characterManager.isOwner(playerId) && !characterManager.getCharacterByOwner(playerId).hasPerk(Perk.ARMOR)) {
                        event.setCancelled(true);
                        player.updateInventory();
                        fallout.getMessenger().sendMessage(player, "error.character.perks.missingrequired", Perk.ARMOR.getName());
                    }
                }
            }
        }
    }

    /**
     * Stops players with characters who don't have the armor perk from manually equipping diamond armor.
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
                UUID playerId = player.getUniqueId();
                CharacterManager characterManager = fallout.getCharacterManager();
                if (characterManager.isOwner(playerId) && !characterManager.getCharacterByOwner(playerId).hasPerk(Perk.ARMOR)) {
                    event.setCancelled(true);
                    fallout.getMessenger().sendMessage(player, "error.character.perks.missingrequired", Perk.ARMOR.getName());
                }
            }
        }
    }

    /**
     * Stops players with characters who don't have the armor perk from manually equipping diamond armor.
     */
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getWhoClicked() instanceof Player && event.getOldCursor() != null) {
            Material material = event.getOldCursor().getType();
            if (ArmorType.isArmor(material) && ArmorMaterial.getArmorMaterial(material).equals(ArmorMaterial.DIAMOND)) {
                Player player = (Player) event.getWhoClicked();
                UUID playerId = player.getUniqueId();
                CharacterManager characterManager = fallout.getCharacterManager();
                if (characterManager.isOwner(playerId) && !characterManager.getCharacterByOwner(playerId).hasPerk(Perk.ARMOR)) {
                    event.setCancelled(true);
                    fallout.getMessenger().sendMessage(player, "error.character.perks.missingrequired", Perk.ARMOR.getName());
                }
            }
        }
    }

    /**
     * Stops mobs from dropping exp to prevent exp farming.
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Creature && fallout.getConfig().getBoolean("PreventMobsDroppingExp", true)) {
            event.setDroppedExp(0);
        }
    }

    /**
     * Stops diamond armor from being crafted.
     */
    @EventHandler
    public void onDiamondArmorCraft(CraftItemEvent event) {
        if (fallout.getConfig().getBoolean("PreventCraftingDiamondArmor", true)) {
            Material material = event.getRecipe().getResult().getType();
            if (ArmorType.isArmor(material) && ArmorMaterial.getArmorMaterial(material).equals(ArmorMaterial.DIAMOND)) {
                event.setCancelled(true);
            }
        }
    }
}
