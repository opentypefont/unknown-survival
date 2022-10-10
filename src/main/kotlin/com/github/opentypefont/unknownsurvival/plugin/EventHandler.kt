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

import com.destroystokyo.paper.event.server.PaperServerListPingEvent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerEditBookEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.server.TabCompleteEvent
import org.bukkit.util.NumberConversions
import java.util.*

class EventHandler : Listener {
    @EventHandler
    fun onPlayerLogin(event: PlayerLoginEvent) {
        if (event.result == PlayerLoginEvent.Result.KICK_FULL) {
            event.allow()
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onAsyncPlayerChat(event: AsyncPlayerChatEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerCommandPreprocess(event: PlayerCommandPreprocessEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.joinMessage = null

        if (!event.player.hasPlayedBefore()) {
            event.player.teleport(getSpawnLocation(event.player.name))
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        event.quitMessage = null
    }

    @EventHandler
    fun onTabComplete(event: TabCompleteEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onPaperServerListPing(event: PaperServerListPingEvent) {
        val calendar = Calendar.getInstance()

        event.numPlayers = calendar.get(Calendar.MINUTE)
        event.maxPlayers = calendar.get(Calendar.HOUR_OF_DAY)
        event.playerSample.clear()
    }

    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        if (event.isBedSpawn || event.isAnchorSpawn) return

        event.respawnLocation = getSpawnLocation(event.player.name)
    }

    @EventHandler
    fun onPlayerKick(event: PlayerKickEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        event.deathMessage = "${ChatColor.RED}누군가 죽었습니다."
    }

    @EventHandler(ignoreCancelled = true)
    fun onSignChange(event: SignChangeEvent) {
        event.lines.forEachIndexed { index, _ ->
            event.setLine(index, "?")
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerBookEdit(event: PlayerEditBookEvent) {
        val meta = event.newBookMeta

        meta.pages.map {
            it.replace(".".toRegex(), "?")
        }
        meta.title.let {
            event.newBookMeta.title = it?.replace(".".toRegex(), "?")
        }

        event.newBookMeta = meta
    }

    // https://github.com/noonmaru/aimless/blob/424bad6c00f5fa0f08f07326bcb3efaf797bad6c/src/main/kotlin/com/github/noonmaru/aimless/plugin/EventListener.kt#L134
    private fun getSpawnLocation(name: String): Location {
        val seed = name.hashCode()
        val random = Random(seed.toLong() xor 0x19940423)
        val world = Bukkit.getWorlds().first()
        val border = world.worldBorder
        val size = border.size / 2.0

        val x = random.nextDouble() * size - size / 2.0
        val z = random.nextDouble() * size - size / 2.0
        val block = world.getHighestBlockAt(NumberConversions.floor(x), NumberConversions.floor(z))

        return block.location.add(0.5, 1.0, 0.5)
    }
}