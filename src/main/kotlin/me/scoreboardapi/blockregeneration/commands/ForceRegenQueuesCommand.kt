package me.scoreboardapi.blockregeneration.commands

import me.blazingtide.commands.annotation.Command
import me.scoreboardapi.blockregeneration.BlockRegeneration
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/**
 * Created by ScoreBoardAPI devscoreboardapi@gmail.com on 4/23/2023.
 */
object ForceRegenQueuesCommand {

    @Command(
        labels = ["blockregeneration forcequeue"],
        description = "Forcefully reset all active block queues!",
        permission = "blockregeneration.forcequeue"
    )
    fun execute(player: Player) {
        BlockRegeneration.instance.resetBlock(BlockRegeneration.instance.cachedExplodedBlocks, false)
        BlockRegeneration.instance.resetBlock(BlockRegeneration.instance.cachedMinedBlocks, false)

        BlockRegeneration.instance.cachedExplodedBlocks.clear()
        BlockRegeneration.instance.cachedMinedBlocks.clear()

        player.sendMessage("${ChatColor.GREEN}Successfully reset all active block queues!")
    }
}