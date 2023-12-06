package eu.filtastisch.buildessentials.commands.warp;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.menu.SGMenu;
import de.thesourcecoders.capi.spigot.ItemBuilder;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import eu.filtastisch.buildessentials.BuildEssentials;
import eu.filtastisch.buildessentials.utils.warp.Warp;
import eu.filtastisch.buildessentials.utils.warp.WarpManager;
import eu.filtastisch.buildessentials.utils.warp.WarpToolbarBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

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
                ArgumentSuggestions.strings(info -> WarpManager.getWarpNames().toArray(new String[0]))
        ));
        this.warpCommand.executes((sender, args) -> {
            if (sender instanceof Player){
                Player p = (Player) sender;

                String warpName = (String) args.get("Warp Name");
                Warp warp = WarpManager.getWarp(warpName);
                if (warp == null){
                    p.sendMessage(Component.text(plugin.getPrefix() + "§cWarp §e" + warpName + "§c existiert nicht!"));
                    return;
                }
                this.handleTeleport(WarpManager.getWarp(warpName), p);

                assert warpName != null;
                TextComponent message = Component.text(this.plugin.getPrefix())
                        .append(Component.text("Du wurdest nach ").color(TextColor.color(0xC34A)))
                        .append(Component.text(warpName).hoverEvent(HoverEvent.showText(
                                Component.text().append(
                                        Component.text()
                                                .append(Component.text("Creator: ").color(TextColor.color(0x16C3B0)))
                                                .append(Component.text(Objects.requireNonNull(Bukkit.getOfflinePlayer(warp.getCreator()).getName())).color(TextColor.color(0xB4A407)))
                                                .appendNewline()
                                ).append(
                                        Component.text()
                                                .append(Component.text("Location: ").color(TextColor.color(0x16C3B0)))
                                                .appendNewline()
                                ).append(
                                        Component.text()
                                                .append(Component.text("   ├ World: ").color(TextColor.color(0x16C3B0)))
                                                .append(Component.text(warp.getLocation().getWorld().getName()).color(TextColor.color(0xC34A)))
                                                .appendNewline()
                                ).append(
                                        Component.text()
                                                .append(Component.text("   ├ X: ").color(TextColor.color(0x16C3B0)))
                                                .append(Component.text(warp.getLocation().getBlockX()).color(TextColor.color(0xB4A407)))
                                                .appendNewline()
                                ).append(
                                        Component.text()
                                                .append(Component.text("   ├ Y: ").color(TextColor.color(0x16C3B0)))
                                                .append(Component.text(warp.getLocation().getBlockY()).color(TextColor.color(0xB4A407)))
                                                .appendNewline()
                                ).append(
                                        Component.text()
                                                .append(Component.text("   └ Z: ").color(TextColor.color(0x16C3B0)))
                                                .append(Component.text(warp.getLocation().getBlockZ()).color(TextColor.color(0xB4A407)))
                                )
                        )).color(TextColor.color(0xB4A407)))
                        .append(Component.text(" teleportiert!").color(TextColor.color(0xC34A)));

                p.sendMessage(message);
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
                this.openWarpGUI(p);
            }
        });
    }

    public void configSetWarpCommand(){
        this.setWarpCommand = new CommandAPICommand("setwarp");
        this.setWarpCommand.withPermission("buildessentials.command.setwarp");
        this.setWarpCommand.withArguments(new StringArgument("Warp Name"));
        this.setWarpCommand.withOptionalArguments(new StringArgument("Permission to Use"));
        this.setWarpCommand.executes((sender, args) -> {
            if (sender instanceof Player){
                Player p = (Player) sender;
                String warpName = (String) args.get("Warp Name");
                String permission = (String) args.get("Permission to Use");
                if (permission == null)
                    permission = "buildessentials.command.warp";
                String returnMessage = WarpManager.safeAddWarp(warpName, p.getLocation(), permission, p.getUniqueId()) ?
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
                TextComponent builder = Component.text(this.plugin.getPrefix());
                TextComponent state;
                if (WarpManager.deleteWarp(warpName)) {
                    assert warpName != null;
                    state = builder
                            .append(Component.text("Warp ").color(TextColor.color(0xC34A)))
                            .append(Component.text(warpName).color(TextColor.color(0xB4A407)))
                            .append(Component.text(" wurde gelöscht!").color(TextColor.color(0xC34A)));
                } else {
                    assert warpName != null;
                    state = builder
                            .append(Component.text("Warp ").color(TextColor.color(0x9D3025)))
                            .append(Component.text(warpName).color(TextColor.color(0xB4A407)))
                            .append(Component.text(" existiert nicht!").color(TextColor.color(0x9D3025)));
                }
                p.sendMessage(state);
            }
        });
    }

    public void openWarpGUI(Player p){
        this.plugin.getOpenWarpGUIList().add(p.getUniqueId());
        SGMenu menu = this.plugin.getSpiGUI().create("§aEternal Warp GUI", 6);
        menu.setAutomaticPaginationEnabled(true);
        menu.setRowsPerPage(5);

        menu.setToolbarBuilder(new WarpToolbarBuilder());

        WarpManager.getAllWarps().forEach(c -> {
            SGButton warp = new SGButton(new ItemBuilder(Material.GRASS_BLOCK)
                    .setName(c.getWarpName())
                    .setLore(
                            "§bCreator: §6" + Bukkit.getOfflinePlayer(c.getCreator()).getName(),
                            "§bLocation: ",
                            "   §7├ §aWorld: §6" + c.getLocation().getWorld().getName(),
                            "   §7├ §aX: §6" + c.getLocation().getBlockX(),
                            "   §7├ §aY: §6" + c.getLocation().getBlockY(),
                            "   §7└ §aZ: §6" + c.getLocation().getBlockZ()
                    )
                    .create()
            ).withListener((InventoryClickEvent e) -> {
                this.handleTeleport(c, p);
                p.sendMessage(Component.text(plugin.getPrefix() + "§aDu wurdest nach §e" + c.getWarpName() + "§a teleportiert!"));
            });
            if (p.hasPermission(c.getPermission())) {
                menu.addButton(warp);
            }
        });

        p.openInventory(menu.getInventory());
    }

    public void handleTeleport(Warp warp, Player p){
        if (p.hasPermission(warp.getPermission()))
            p.teleport(warp.getLocation());
    }

}
