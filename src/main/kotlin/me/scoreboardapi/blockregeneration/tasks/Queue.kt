package me.scoreboardapi.blockregeneration.tasks

import me.scoreboardapi.blockregeneration.BlockRegeneration
import me.scoreboardapi.blockregeneration.utils.ColorUtils
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material

/**
 * Created by ScoreBoardAPI devscoreboardapi@gmail.com on 4/22/2023.
 */
class Queue : Runnable {

    override fun run() {
        if (BlockRegeneration.instance.cachedExplodedBlocks.isEmpty() && BlockRegeneration.instance.cachedMinedBlocks.isEmpty()) return
        val cachedMinedBlock = BlockRegeneration.instance.cachedMinedBlocks
        val cachedExplodedBlock = BlockRegeneration.instance.cachedExplodedBlocks

        if (cachedExplodedBlock.isNotEmpty() && cachedExplodedBlock.first() != null) {
            BlockRegeneration.instance.resetBlock(cachedExplodedBlock, true)
            BlockRegeneration.instance.cachedExplodedBlocks.remove(cachedExplodedBlock.first())
    }

        if (cachedMinedBlock.isNotEmpty()) {
            repeat(cachedMinedBlock.size) { BlockRegeneration.instance.resetBlock(cachedMinedBlock, false) }
            BlockRegeneration.instance.cachedMinedBlocks.clear()
        }

        BlockRegeneration.instance.config.getStringList("Queue.Broadcast")
            .forEach { Bukkit.broadcastMessage(ColorUtils.translate(it)) }
    }
}