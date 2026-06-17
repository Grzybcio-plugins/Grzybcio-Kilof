package pl.grzybcio.kilof;

import org.bukkit.plugin.java.JavaPlugin;
import pl.grzybcio.kilof.commands.KilofCommand;
import pl.grzybcio.kilof.listeners.BlockBreakListener;
import pl.grzybcio.kilof.utils.KilofManager;

public class GrzybcioKilof extends JavaPlugin {

    private static GrzybcioKilof instance;
    private KilofManager kilofManager;

    @Override
    public void onEnable() {
        instance = this;
        
        // Zapisz domyślną konfigurację
        saveDefaultConfig();
        
        // Inicjalizacja managera kilofa
        kilofManager = new KilofManager(this);
        
        // Rejestracja komend
        KilofCommand kilofCommand = new KilofCommand(this);
        getCommand("grzybcio-kilof").setExecutor(kilofCommand);
        getCommand("grzybcio-kilof").setTabCompleter(kilofCommand);
        
        // Rejestracja listenerów
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        
        getLogger().info("GrzybcioKilof został włączony!");
    }

    @Override
    public void onDisable() {
        getLogger().info("GrzybcioKilof został wyłączony!");
    }

    public static GrzybcioKilof getInstance() {
        return instance;
    }

    public KilofManager getKilofManager() {
        return kilofManager;
    }
    
    public void reloadConfiguration() {
        reloadConfig();
        kilofManager = new KilofManager(this);
    }
}

