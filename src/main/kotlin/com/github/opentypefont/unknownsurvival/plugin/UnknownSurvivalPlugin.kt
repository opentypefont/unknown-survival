/*
 * Copyright (C) 2022 Giyoung Ryu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.opentypefont.unknownsurvival.plugin

import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Giyoung Ryu
 */
class UnknownSurvivalPlugin : JavaPlugin() {
    override fun onEnable() {
        server.spawnRadius = 1
        Bukkit.getWorlds().first().apply {
            spawnLocation = getHighestBlockAt(0, 0).location
            worldBorder.center = spawnLocation
            worldBorder.size = 30000.0
        }

        for (world in Bukkit.getWorlds()) {
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
            world.setGameRule(GameRule.LOG_ADMIN_COMMANDS, false)
            world.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, false)
            world.setGameRule(GameRule.REDUCED_DEBUG_INFO, true)
        }

        server.pluginManager.registerEvents(EventHandler(), this)
    }
}