package me.scoreboardapi.blockregeneration.commands

import me.blazingtide.commands.annotation.Command
import me.scoreboardapi.blockregeneration.BlockRegeneration
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/**
 * Created by ScoreBoardAPI devscoreboardapi@gmail.com on 6/4/2023.
 */
object QueueSetTimeCommand {
    @Command(
        labels = ["blockregeneration setqueuetime"],
        usage = "<seconds>",
        description = "Set the time it takes to regen the queue!",
        permission = "blockregeneration.setqueuetime"
    )
    fun execute(player: Player, time: String) {

        try {
            Integer.parseInt(time)
        } catch (e: NumberFormatException) {
            player.sendMessage("${ChatColor.RED}' $time ' is not a valid number.")
            return
        }


        if (time.toLong() < 0) {
            player.sendMessage("${ChatColor.RED}You cannot set the queue time to a negative number!")
            return
        }

        BlockRegeneration.instance.config.set("QueueTime", time.toLong())
        BlockRegeneration.instance.saveConfig()
        BlockRegeneration.instance.reloadConfig()

        player.sendMessage("${ChatColor.GREEN}Successfully set the queue time to ${ChatColor.YELLOW}$time ${ChatColor.YELLOW}seconds!")
    }
}