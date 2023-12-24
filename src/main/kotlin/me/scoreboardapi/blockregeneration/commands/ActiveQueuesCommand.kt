package me.scoreboardapi.blockregeneration.commands

import me.blazingtide.commands.annotation.Command
import me.scoreboardapi.blockregeneration.BlockRegeneration
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/**
 * Created by ScoreBoardAPI devscoreboardapi@gmail.com on 5/1/2023.
 */
object ActiveQueuesCommand {
    @Command(
        labels = ["blockregeneration activequeues"],
        description = "View all active queues!",
        permission = "blockregeneration.activequeues"
    )
    fun execute(player: Player) {
        player.sendMessage("${ChatColor.GREEN}Block Regeneration Active Queues:")
        player.sendMessage("")
        player.sendMessage("${ChatColor.GREEN}Exploded Blocks Queued: ${ChatColor.WHITE}${BlockRegeneration.instance.cachedExplodedBlocks.size}")
        player.sendMessage("${ChatColor.GREEN}Mined Blocks Queued: ${ChatColor.WHITE}${BlockRegeneration.instance.cachedMinedBlocks.size}")
        player.sendMessage("")
        player.sendMessage("${ChatColor.GREEN}To force reset all active queues, use: ${ChatColor.WHITE}/blockregeneration forcequeue")
        player.sendMessage("${ChatColor.GREEN}Queue will trigger every 5 minutes.")
    }
}