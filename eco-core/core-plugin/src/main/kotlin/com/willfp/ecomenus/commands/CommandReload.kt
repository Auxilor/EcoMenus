package com.willfp.ecomenus.commands

import com.willfp.eco.core.Prerequisite
import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.eco.util.StringUtils
import com.willfp.eco.util.toNiceString
import com.willfp.ecomenus.menus.EcoMenus
import com.willfp.ecomenus.plugin
import org.bukkit.command.CommandSender

object CommandReload : Subcommand(
    plugin,
    "reload",
    "ecomenus.command.reload",
    false
) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        val runnable = Runnable {
            sender.sendMessage(
                plugin.langYml.getMessage("reloaded", StringUtils.FormatOption.WITHOUT_PLACEHOLDERS)
                    .replace("%time%", plugin.reloadWithTime().toNiceString())
                    .replace("%count%", EcoMenus.values().size.toString())
            )
        }
        if (Prerequisite.HAS_FOLIA.isMet)
            plugin.scheduler.runTask(runnable)
        else
            runnable.run()
    }
}
