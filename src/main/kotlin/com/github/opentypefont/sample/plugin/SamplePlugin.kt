package com.github.opentypefont.sample.plugin

import org.bukkit.plugin.java.JavaPlugin

class SamplePlugin : JavaPlugin() {
    override fun onEnable() {
        logger.info("Plugin enabled.")
    }
}