package me.scoreboardapi.blockregeneration.listeners

import me.scoreboardapi.blockregeneration.BlockRegeneration
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.type.RespawnAnchor
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.block.BlockPhysicsEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.player.PlayerInteractEvent

/**
 * Created by ScoreBoardAPI devscoreboardapi@gmail.com on 4/22/2023.
 */
class CacheListener : Listener {

    @EventHandler
    fun onEntityExplode(event: EntityExplodeEvent) {
        if (BlockRegeneration.instance.blacklistedWorlds.contains(event.entity.world.name)) return

        val map = mutableMapOf<Location, Material>()

        event.blockList().forEach { block ->
            if (block == null) return
            if (block.type == Material.AIR) return@forEach
            if (block.type == Material.FIRE) return@forEach

            if (block.type == Material.OBSIDIAN) return
            if (block.type == Material.RESPAWN_ANCHOR) return
            if (block.type == Material.COBWEB) return
            if (block.type == Material.GLOWSTONE) return

            map[block.location] = block.type
        }

        BlockRegeneration.instance.cachedExplodedBlocks.add(map)
        event.yield = 0.0F
    }

    @EventHandler
    fun onBlockExplode(event: BlockExplodeEvent) {
        if (BlockRegeneration.instance.blacklistedWorlds.contains(event.block.world.name)) return

        val map = mutableMapOf<Location, Material>()

        if (event.blockList().isEmpty()) return

        event.blockList().forEach { block ->
            if (block == null) return
            if (block.type == Material.AIR) return@forEach
            if (block.type == Material.FIRE) return@forEach

            if (block.type == Material.OBSIDIAN) return@forEach
            if (block.type == Material.RESPAWN_ANCHOR) return@forEach
            if (block.type == Material.COBWEB) return@forEach
            if (block.type == Material.GLOWSTONE) return@forEach

            
            map[block.location] = block.type
        }

        BlockRegeneration.instance.cachedExplodedBlocks.add(map)
        event.yield = 0.0F
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        if (event.player.isOp && event.player.gameMode == GameMode.CREATIVE) return
        if (BlockRegeneration.instance.blacklistedWorlds.contains(event.player.world.name)) return

        val map = mutableMapOf<Location, Material>()
        val block = event.block

        if (block.type == Material.AIR) return
        if (block.type == Material.OBSIDIAN) return
        if (block.type == Material.RESPAWN_ANCHOR) return
        if (block.type == Material.COBWEB) return
        if (block.type == Material.GLOWSTONE) return


        map[block.location] = block.type

        BlockRegeneration.instance.cachedMinedBlocks.add(map)

        event.isDropItems = false
    }

    @EventHandler
    fun onEntityDamageEvent(event: EntityDamageEvent) {
        if (event.entity !is Player) return
        if (event.cause != EntityDamageEvent.DamageCause.SUFFOCATION) return

        event.entity.teleport(getHighestBlockAbove(event.entity.location).location.add(0.0, 1.0, 0.0))
    }

    private fun getHighestBlockAbove(location: Location): Block {
        var block = location.block

        while (block.type == Material.AIR && block.y > 0) block = block.getRelative(BlockFace.DOWN)

        return block
    }
}