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
package ninja.amp.fallout.radiation;

import ninja.amp.fallout.util.Expression;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Stores the information about a fallout radiation zone.
 *
 * @author Austin Payne
 */
public class Zone {

    private Location center;
    private int radius;
    private int radiusSquared;
    private String strength;
    private boolean enabled;

    /**
     * Creates a zone.
     *
     * @param center   The center of the zone
     * @param radius   The radius of the zone
     * @param strength The equation used to find the strength of the zone based on distance
     */
    public Zone(Location center, int radius, String strength) {
        this.center = center;
        this.radius = radius;
        this.radiusSquared = radius * radius;
        this.strength = strength;
        this.enabled = true;
    }

    /**
     * Creates a zone, loading it from a configuration section.
     *
     * @param section The configuration section
     * @throws Exception If the section is formatted incorrectly or does not represent a complete zone
     */
    public Zone(ConfigurationSection section) throws Exception {
        if (section.isString("center")) {
            String[] location = section.getString("center").split(":");
            World world;
            int x, y, z;
            try {
                x = Integer.parseInt(location[1]);
                y = Integer.parseInt(location[2]);
                z = Integer.parseInt(location[3]);
                world = Bukkit.getWorld(location[0]);
            } catch (Exception e) {
                throw new Exception("Invalid center location");
            }
            if (world == null) {
                throw new Exception("Invalid world");
            }
            this.center = new Location(world, x, y, z);
        } else {
            throw new Exception("Missing or invalid center location");
        }
        if (section.isInt("radius")) {
            this.radius = section.getInt("radius");
            this.radiusSquared = radius * radius;
        } else {
            throw new Exception("Missing or invalid radius");
        }
        if (section.isString("strength")) {
            this.strength = section.getString("strength");
        } else {
            throw new Exception("Missing or invalid strength");
        }
        if (section.isBoolean("enabled")) {
            this.enabled = section.getBoolean("enabled");
        } else {
            throw new Exception("Missing or invalid enabled");
        }
    }

    /**
     * Gets the zone's center.
     *
     * @return The zone's center
     */
    public Location getCenter() {
        return center;
    }

    /**
     * Gets the zone's radius.
     *
     * @return The zone's radius
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Checks if the zone is enabled.
     *
     * @return {@code true} if the zone is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Gets the zone's strength at a certain distance from the center.
     *
     * @param distance The distance from the center of the zone
     * @return The zone's strength
     */
    public int getStrength(int distance) {
        String expression = strength.replace("d", Integer.toString(distance));

        return Expression.evaluate(expression);
    }

    /**
     * Checks if a location is within and affected by the zone.
     *
     * @param location The location
     * @return {@code true} if the location is within the zone's radius
     */
    public boolean isAffected(Location location) {
        return center.getWorld().equals(location.getWorld()) && center.distanceSquared(location) <= radiusSquared;
    }

    /**
     * Saves the zone to a configuration section.
     *
     * @param section The configuration section
     */
    public void save(ConfigurationSection section) {
        section.set("center", center.getWorld().getName() + ":" + center.getBlockX() + ":" + center.getBlockY() + ":" + center.getBlockZ());
        section.set("radius", radius);
        section.set("strength", strength);
        section.set("enabled", enabled);
    }

}
