package me.scoreboardapi.blockregeneration

import me.blazingtide.commands.Commands
import me.scoreboardapi.blockregeneration.commands.ActiveQueuesCommand
import me.scoreboardapi.blockregeneration.commands.ForceRegenQueuesCommand
import me.scoreboardapi.blockregeneration.commands.QueueSetTimeCommand
import me.scoreboardapi.blockregeneration.listeners.BlockListener
import me.scoreboardapi.blockregeneration.listeners.CacheListener
import me.scoreboardapi.blockregeneration.tasks.Queue
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin

/**
 * Created by ScoreBoardAPI devscoreboardapi@gmail.com on 4/22/2023.
 */
class BlockRegeneration : JavaPlugin() {

    val cachedExplodedBlocks = mutableListOf<MutableMap<Location, Material>>()
    val cachedMinedBlocks = mutableListOf<MutableMap<Location, Material>>()

    val blacklistedWorlds: MutableList<String> = config.getStringList("BlacklistedWorlds")
    val placedRegenBlocks: MutableList<Location> = mutableListOf()

    companion object {
        @JvmStatic
        lateinit var instance: BlockRegeneration
    }

    override fun onEnable() {
        instance = this

        saveDefaultConfig()

        server.scheduler.runTaskTimer(this, Queue(), 0L, 20 * this.config.getLong("QueueTime"))

        server.pluginManager.registerEvents(CacheListener(), this)
        server.pluginManager.registerEvents(BlockListener(), this)

        Commands.registerAnnotations(ForceRegenQueuesCommand)
        Commands.registerAnnotations(ActiveQueuesCommand)
        Commands.registerAnnotations(QueueSetTimeCommand)
    }

    override fun onDisable() {
        if (cachedExplodedBlocks.isNotEmpty()) resetBlock(cachedExplodedBlocks, false)
        if (cachedMinedBlocks.isNotEmpty()) resetBlock(cachedMinedBlocks, false)

        cachedExplodedBlocks.clear()
        cachedMinedBlocks.clear()

        if (placedRegenBlocks.isNotEmpty()) placedRegenBlocks.forEach { it.world!!.getBlockAt(it).type = Material.AIR }
    }

    fun resetBlock(list: MutableList<MutableMap<Location, Material>>, queued: Boolean) {
        if (list.isEmpty()) return

        if (queued) list.first()
            .forEach { it.key.world!!.getBlockAt(it.key).setType(it.value, false) } //queues and resets first element
        else list.forEach { block ->
            block.forEach {
                it.key.world!!.getBlockAt(it.key).setType(it.value, false)
            }
        } //resets the whole list
    }
}