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
package ninja.amp.fallout;

import ninja.amp.fallout.characters.Character;
import ninja.amp.fallout.characters.CharacterManager;
import ninja.amp.fallout.characters.Perk;
import ninja.amp.fallout.characters.Race;
import ninja.amp.fallout.message.FOMessage;
import ninja.amp.fallout.utils.ArmorMaterial;
import ninja.amp.fallout.utils.ArmorType;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
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
    private Fallout plugin;

    /**
     * Creates a new FOListener.
     *
     * @param plugin The {@link Fallout} instance.
     */
    public FOListener(Fallout plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Attempts to load the player's character.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getCharacterManager().loadCharacter(event.getPlayer());
    }

    /**
     * Unloads the player's character from the character manager if the player has a character.
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        CharacterManager characterManager = plugin.getCharacterManager();
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

            CharacterManager characterManager = plugin.getCharacterManager();
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
//        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
//            Player player = event.getPlayer();
//            ItemStack itemStack = player.getItemInHand();
//            if (itemStack != null) {
//                Material material = itemStack.getType();
//                if (ArmorType.isArmor(material) && ArmorType.getArmorType(material).canEquip(player)) {
//                    UUID playerId = player.getUniqueId();
//                    CharacterManager characterManager = plugin.getCharacterManager();
//                    if (characterManager.isOwner(playerId)) {
//                        Character character = characterManager.getCharacterByOwner(playerId);
//                        if (!ArmorMaterial.getArmorMaterial(material).equals(ArmorMaterial.LEATHER) && character.getRace().equals(Race.SUPER_MUTANT)) { // Super Mutants can only wear leather armor
//                            event.setCancelled(true);
//                            player.updateInventory();
//                            plugin.getMessenger().sendMessage(player, FOMessage.RACE_ONLYLEATHER);
//                        } else if (ArmorMaterial.getArmorMaterial(material).equals(ArmorMaterial.DIAMOND) && !character.hasPerk(Perk.ARMOR)) { // Wearing diamond armor requires the ARMOR perk
//                            event.setCancelled(true);
//                            player.updateInventory();
//                            plugin.getMessenger().sendMessage(player, FOMessage.PERK_MISSINGREQUIRED, Perk.ARMOR.getName());
//                        }
//                    }
//                }
//            }
//        }
    }

    /**
     * Stops players with characters who don't have the armor perk from manually equipping diamond armor.
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
//        if (event.getWhoClicked() instanceof Player) {
//            Player player = (Player) event.getWhoClicked();
//            ClickType clickType = event.getClick();
//            if (event.isLeftClick() || event.isRightClick()) {
//                ItemStack itemStack = null;
//                if (event.isShiftClick()) {
//                    itemStack = event.getCurrentItem(); // Shift clicking will move the item in the slot
//                } else if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
//                    itemStack = event.getCursor(); // Normal clicking will move the item in the cursor
//                }
//                if (itemStack != null) {
//                    Material material = itemStack.getType();
//                    if (ArmorType.isArmor(material)) {
//                        if (event.isShiftClick() && !ArmorType.getArmorType(material).canEquip(player)) { // The armor won't actually go into the armor slot, don't cancel
//                            return;
//                        }
//                        UUID playerId = player.getUniqueId();
//                        CharacterManager characterManager = plugin.getCharacterManager();
//                        if (characterManager.isOwner(playerId)) {
//                            Character character = characterManager.getCharacterByOwner(playerId);
//                            if (!ArmorMaterial.getArmorMaterial(material).equals(ArmorMaterial.LEATHER) && character.getRace().equals(Race.SUPER_MUTANT)) { // Super Mutants can only wear leather armor
//                                event.setCancelled(true);
//                                plugin.getMessenger().sendMessage(player, FOMessage.RACE_ONLYLEATHER);
//                            } else if (ArmorMaterial.getArmorMaterial(material).equals(ArmorMaterial.DIAMOND) && !character.hasPerk(Perk.ARMOR)) { // Wearing diamond armor requires the ARMOR perk
//                                event.setCancelled(true);
//                                plugin.getMessenger().sendMessage(player, FOMessage.PERK_MISSINGREQUIRED, Perk.ARMOR.getName());
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }

    /**
     * Stops players with characters who don't have the armor perk from manually equipping diamond armor.
     */
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
//        if (event.getWhoClicked() instanceof Player && event.getOldCursor() != null) {
//            Material material = event.getOldCursor().getType();
//            if (ArmorType.isArmor(material)) {
//                Player player = (Player) event.getWhoClicked();
//                UUID playerId = player.getUniqueId();
//                CharacterManager characterManager = plugin.getCharacterManager();
//                if (characterManager.isOwner(playerId)) {
//                    Character character = characterManager.getCharacterByOwner(playerId);
//                    if (!ArmorMaterial.getArmorMaterial(material).equals(ArmorMaterial.LEATHER) && character.getRace().equals(Race.SUPER_MUTANT)) { // Super Mutants can only wear leather armor
//                        event.setCancelled(true);
//                        plugin.getMessenger().sendMessage(player, FOMessage.RACE_ONLYLEATHER);
//                    } else if (ArmorMaterial.getArmorMaterial(material).equals(ArmorMaterial.DIAMOND) && !character.hasPerk(Perk.ARMOR)) { // Wearing diamond armor requires the ARMOR perk
//                        event.setCancelled(true);
//                        plugin.getMessenger().sendMessage(player, FOMessage.PERK_MISSINGREQUIRED, Perk.ARMOR.getName());
//                    }
//                }
//            }
//        }
    }

    /**
     * Stops mobs from dropping exp to prevent exp farming.
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Creature && plugin.getConfig().getBoolean("PreventMobsDroppingExp", true)) {
            event.setDroppedExp(0);
        }
    }

    /**
     * Stops diamond armor from being crafted.
     */
    @EventHandler
    public void onDiamondArmorCraft(CraftItemEvent event) {
        if (plugin.getConfig().getBoolean("PreventCraftingDiamondArmor", true)) {
            Material material = event.getRecipe().getResult().getType();
            if (ArmorType.isArmor(material) && ArmorMaterial.getArmorMaterial(material).equals(ArmorMaterial.DIAMOND)) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Destroys the {@link ninja.amp.fallout.FOListener}.
     */
    public void destroy() {
        HandlerList.unregisterAll(this);
    }
}
