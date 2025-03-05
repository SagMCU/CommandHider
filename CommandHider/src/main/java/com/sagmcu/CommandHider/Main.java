package com.sagmcu.CommandHider;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sagmcu.CommandHider.commands.ReloadCommand;
import com.sagmcu.CommandHider.filters.LegacyTabCompletionFilter;
import com.sagmcu.CommandHider.filters.TabCompletionFilter;
import com.sagmcu.CommandHider.utils.ChatColorTranslator;
import com.sagmcu.CommandHider.bstats.Metrics;

public class Main extends JavaPlugin {
    private Object tabFilter;
    private String serverVersion;
    private ChatColorTranslator chatColorTranslator;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        // Detect server version
        serverVersion = getServer().getBukkitVersion().split("-")[0];
        chatColorTranslator = new ChatColorTranslator(this);
        // Load the appropriate filter based on version
        if (isModernVersion()) {
            tabFilter = new TabCompletionFilter(this);
            getServer().getPluginManager().registerEvents((TabCompletionFilter) tabFilter, this);
        } else {
            // For 1.8-1.12, check for ProtocolLib and use LegacyTabCompletionFilter
            Plugin protocolLib = getServer().getPluginManager().getPlugin("ProtocolLib");
            if (protocolLib != null && protocolLib.isEnabled()) {
                tabFilter = new LegacyTabCompletionFilter(this);
                getLogger().info("ProtocolLib detected. Enabling tab completion filtering for 1.8 - 1.12.");
            } else {
                tabFilter = null; // Disable filtering if ProtocolLib is missing
                getLogger().warning("ProtocolLib not found or not enabled. Tab completion filtering for 1.8 - 1.12 will not work.");
            }
        }

        getCommand("commandhider").setExecutor(new ReloadCommand(this));
        int pluginId = 24801;
        @SuppressWarnings("unused")
        Metrics metrics = new Metrics(this, pluginId);
        String enabledMessage = chatColorTranslator.translate("&aCommandHider &7has been &aenabled&7! &6:D");
        this.getServer().getConsoleSender().sendMessage(enabledMessage);
    }

    @Override
    public void onDisable() {
        String disabledMessage = chatColorTranslator.translate("&cCommandHider &7has been &cdisabled&7! :(");
        this.getServer().getConsoleSender().sendMessage(disabledMessage);
    }

    public Object getTabFilter() {
        return tabFilter;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public boolean isModernVersion() {
        try {
            String[] versionParts = serverVersion.split("\\.");
            int major = Integer.parseInt(versionParts[0]);
            int minor = Integer.parseInt(versionParts[1]);
            return major > 1 || (major == 1 && minor >= 13);
        } catch (Exception e) {
            return false; // Default to legacy if parsing fails
        }
    }

    // Helper method to reload config for the active filter
    public void reloadTabFilterConfig() {
        if (isModernVersion()) {
            ((TabCompletionFilter) tabFilter).reloadConfig();
        } else {
            ((LegacyTabCompletionFilter) tabFilter).reloadConfig();
        }
    }

    public ChatColorTranslator getChatColorTranslator() { // Add getter
        return chatColorTranslator;
    }
}
