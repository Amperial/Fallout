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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

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

    @EventHandler
    public void onPlayerClick(PlayerInteractEntityEvent event) {
        if (event.getPlayer().isSneaking() && event.getRightClicked() instanceof Player) {
            String clickedName = ((Player) event.getRightClicked()).getName();

            CharacterManager characterManager = fallout.getCharacterManager();
            if (characterManager.isOwner(clickedName)) {
                event.getPlayer().performCommand("fo character listspecial " + characterManager.getCharacterByOwner(clickedName).getCharacterName());
            }
        }
    }
}
