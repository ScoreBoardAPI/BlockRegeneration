package me.scoreboardapi.blockregeneration.utils

import net.md_5.bungee.api.ChatColor
import java.util.regex.Pattern

object ColorUtils {

    fun translate(str: String?): String {
        var message = str
        val pattern = Pattern.compile("#[a-fA-F0-9]{6}")
        var matcher = pattern.matcher(message)
        while (matcher.find()) {
            val hexCode = message!!.substring(matcher.start(), matcher.end())
            val replaceSharp = hexCode.replace('#', 'x')
            val ch = replaceSharp.toCharArray()
            val builder = StringBuilder("")
            for (c in ch) builder.append("&$c")
            message = message.replace(hexCode, builder.toString())
            matcher = pattern.matcher(message)
        }
        return ChatColor.translateAlternateColorCodes('&', message)
    }

    fun formatDouble(double: Double): String {
        return String.format("%,.2f", double)
    }

    fun translateList(str: MutableList<String>): MutableList<String> {
        val list: MutableList<String> = arrayListOf()
        str.forEach { s -> list.add(translate(s)) }
        return list
    }
}
