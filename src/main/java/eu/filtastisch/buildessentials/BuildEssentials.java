package eu.filtastisch.buildessentials;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import eu.filtastisch.buildessentials.commands.gamemode.GamemodeCommands;
import org.bukkit.plugin.java.JavaPlugin;

public final class BuildEssentials extends JavaPlugin {

    private static BuildEssentials instance;

    @Override
    public void onLoad() {
        instance = this;
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).verboseOutput(true));
        this.loadCommands();
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }

    public void loadCommands(){
        new GamemodeCommands();
    }

    public static BuildEssentials getInstance() {
        return instance;
    }
}
