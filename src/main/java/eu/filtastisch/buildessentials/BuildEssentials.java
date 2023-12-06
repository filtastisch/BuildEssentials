package eu.filtastisch.buildessentials;

import com.samjakob.spigui.SpiGUI;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.menu.SGMenu;
import com.samjakob.spigui.toolbar.SGToolbarBuilder;
import com.samjakob.spigui.toolbar.SGToolbarButtonType;
import de.thesourcecoders.capi.spigot.GuiManager;
import de.thesourcecoders.capi.spigot.SpigotConfig;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import eu.filtastisch.buildessentials.commands.gamemode.GamemodeCommands;
import eu.filtastisch.buildessentials.commands.warp.WarpCommand;
import eu.filtastisch.buildessentials.utils.warp.Warp;
import eu.filtastisch.buildessentials.utils.warp.WarpManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public final class BuildEssentials extends JavaPlugin {

    @Getter
    private static BuildEssentials instance;

    @Getter
    private SpigotConfig warpsConfig;

    @Getter
    private final String prefix = "§dEternalBuild §7| ";

    @Getter
    private List<UUID> openWarpGUIList;

    @Getter
    private SpiGUI spiGUI;

    @Override
    public void onLoad() {
        instance = this;
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).verboseOutput(true));
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();
        this.loadValues();
        this.loadCommands();
        this.loadConfig();
        this.loadWarps();
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        this.safeWarps();
    }

    public void loadCommands(){
        new GamemodeCommands();
        new WarpCommand();
    }

    public void loadValues(){
        this.openWarpGUIList = new ArrayList<>();
        this.spiGUI = new SpiGUI(this);

    }

    public void loadConfig(){
        this.warpsConfig = new SpigotConfig(this, "warps.yml", true).register();
    }

    public void loadWarps(){
        if (this.warpsConfig.getConfigurationSection("warps") == null)
            return;
        for (String warpKey : this.warpsConfig.getConfigurationSection("warps").getKeys(false)){
            if (this.warpsConfig.getLocation("warps." + warpKey) == null)
                return;
            String addedWarp = WarpManager
                    .addWarp(warpKey,
                            this.warpsConfig.getLocation("warps." + warpKey + ".location"),
                            this.warpsConfig.getString("warps." + warpKey + ".permission"),
                            this.warpsConfig.getString("warps." + warpKey + ".creator"))
                    ? "Warp " + warpKey + " wurde geladen"
                    : "Warp " + warpKey + " konnte nicht geladen werden";
            Bukkit.getLogger().log(Level.INFO, addedWarp);
        }
    }

    public void safeWarps(){
        WarpManager.getAllWarps().forEach(c -> {
            this.warpsConfig.setLocation("warps." + c.getWarpName(), c.getLocation());
        });
        this.warpsConfig.save();
    }
}
