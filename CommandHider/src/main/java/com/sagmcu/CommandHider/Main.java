package com.sagmcu.CommandHider;

import org.bukkit.plugin.java.JavaPlugin;

import com.sagmcu.CommandHider.commands.ReloadCommand;
import com.sagmcu.CommandHider.utils.ChatColorTranslator;
import com.sagmcu.CommandHider.utils.TabCompletionFilter;
import com.sagmcu.CommandHider.bstats.Metrics;

public class Main extends JavaPlugin {
    private TabCompletionFilter tabCompletionFilter;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        tabCompletionFilter = new TabCompletionFilter(this);
        getServer().getPluginManager().registerEvents(tabCompletionFilter, this);
        getCommand("commandhider").setExecutor(new ReloadCommand(this));
        int pluginId = 24801;
        @SuppressWarnings("unused")
        Metrics metrics = new Metrics(this, pluginId);
        String enabledMessage = ChatColorTranslator.translate("&aCommandHider &7has been &aenabled&7! &6:D");
        this.getServer().getConsoleSender().sendMessage(enabledMessage);
    }

    @Override
    public void onDisable() {
        String disabledMessage = ChatColorTranslator.translate("&cCommandHider &7has been &cdisabled&7! :(");
        this.getServer().getConsoleSender().sendMessage(disabledMessage);
    }

    public TabCompletionFilter getTabCompletionFilter() {
        return tabCompletionFilter;
    }
}