package eu.filtastisch.buildessentials.commands.warp;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import eu.filtastisch.buildessentials.BuildEssentials;
import eu.filtastisch.buildessentials.utils.warp.WarpManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;

import java.util.Collections;

public class WarpCommand {

    private final BuildEssentials plugin = BuildEssentials.getInstance();

    private CommandAPICommand warpCommand, warpsCommand, setWarpCommand, delWarpCommand;

    public WarpCommand() {
        this.loadCommands();
        this.registerCommands();
    }

    public void registerCommands(){
        this.warpCommand.register();
        this.warpsCommand.register();
        this.setWarpCommand.register();
        this.delWarpCommand.register();
    }

    public void loadCommands(){
        this.configWarpCommand();
        this.configWarpsCommand();
        this.configSetWarpCommand();
        this.configDelWarpCommand();
    }

    public void configWarpCommand(){
        this.warpCommand = new CommandAPICommand("warp");
        this.warpCommand.withPermission("buildessentials.command.warp");
        this.warpCommand.withArguments(new StringArgument("Warp Name").replaceSuggestions(
                ArgumentSuggestions.strings(warp -> WarpManager.getWarpNames().toArray(new String[0]))
        ));
        this.warpCommand.executes((sender, args) -> {
            if (sender instanceof Player){
                Player p = (Player) sender;
                String warpName = (String) args.get("Warp Name");
                p.teleport(WarpManager.getWarp(warpName).getLocation());
            }
        });
    }

    public void configWarpsCommand(){
        this.warpsCommand = new CommandAPICommand("warps");
        this.warpsCommand.withPermission("buildessentials.command.warps");
        this.warpsCommand.withAliases("warplist", "warpl");
        this.warpsCommand.executes((sender, args) -> {
            if (sender instanceof Player){
                Player p = (Player) sender;
                WarpManager.getWarpNames().forEach(p::sendMessage);
            }
        });
    }

    public void configSetWarpCommand(){
        this.setWarpCommand = new CommandAPICommand("setwarp");
        this.setWarpCommand.withPermission("buildessentials.command.setwarp");
        this.setWarpCommand.withArguments(new StringArgument("Warp Name"));
        this.setWarpCommand.executes((sender, args) -> {
            if (sender instanceof Player){
                Player p = (Player) sender;
                String warpName = (String) args.get("Warp Name");
                String returnMessage = WarpManager.safeAddWarp(warpName, p.getLocation()) ?
                        plugin.getPrefix() + "§aWarp §e" + warpName + "§a wurde gesetzt!" :
                        plugin.getPrefix() + "§cWarp §e" + warpName + "§c existiert bereits!";
                p.sendMessage(Component.text(returnMessage));
            }
        });
    }

    public void configDelWarpCommand(){
        this.delWarpCommand = new CommandAPICommand("delwarp");
        this.delWarpCommand.withPermission("buildessentials.command.delwarp");
        this.delWarpCommand.withArguments(new StringArgument("Warp Name").replaceSuggestions(
                ArgumentSuggestions.strings(WarpManager.getWarpNames())
        ));
        this.delWarpCommand.executes((sender, args) -> {
            if (sender instanceof Player){
                Player p = (Player) sender;
                String warpName = (String) args.get("Warp Name");
                String returnMessage = WarpManager.deleteWarp(warpName) ?
                        plugin.getPrefix() + "§aWarp §e" + warpName + "§a wurde gelöscht!" :
                        plugin.getPrefix() + "§cWarp §e" + warpName + "§c existiert nicht!";
                p.sendMessage(Component.text(returnMessage));
            }
        });
    }

}
