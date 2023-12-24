package me.scoreboardapi.blockregeneration.listeners

import me.scoreboardapi.blockregeneration.BlockRegeneration
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerBucketEmptyEvent
import org.bukkit.event.player.PlayerBucketFillEvent

/**
 * Created by ScoreBoardAPI devscoreboardapi@gmail.com on 4/23/2023.
 */
class BlockListener : Listener {

    private fun getNearbyEntities(location: Location, radius: Double): MutableList<Entity> {
        val entities = mutableListOf<Entity>()

        location.world!!.entities.forEach { entity ->
            if (entity.location.world != location.world) return@forEach
            if (entity is Player) return@forEach

            if (entity.location.distance(location) <= radius * radius) {
                entities.add(entity)
            }
        }

        return entities
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (event.player.isOp && event.player.gameMode == GameMode.CREATIVE) return
        if (event.block == null) return

        if (event.block.type == Material.OBSIDIAN ||
            event.block.type == Material.RESPAWN_ANCHOR ||
            event.block.type == Material.COBWEB ||
            event.block.type == Material.END_CRYSTAL ||
            event.block.type == Material.GLOWSTONE
        ) {

            BlockRegeneration.instance.placedRegenBlocks.add(event.block.location)

            if (BlockRegeneration.instance.blacklistedWorlds.contains(event.block.world.name)) return

            Bukkit.getServer().scheduler.runTaskLater(
                BlockRegeneration.instance,
                Runnable {
                    if (BlockRegeneration.instance.placedRegenBlocks.contains(event.block.location)) {
                        BlockRegeneration.instance.placedRegenBlocks.remove(event.block.location)
                        event.block.type = Material.AIR
                    }

                    val entities = getNearbyEntities(event.block.location, 3.0)
                    entities.forEach { if (it.type == EntityType.ENDER_CRYSTAL) it.remove() }
                },
                20 * 10L
            )

        } else {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onEmpty(event: PlayerBucketEmptyEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onFill(event: PlayerBucketFillEvent) {
        event.isCancelled = true
    }
}