package eu.filtastisch.buildessentials.commands.gamemode;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class GamemodeCommands {
    public GamemodeCommands() {
        this.registerOtherCommands();
        this.registerGamemodeCommand();
    }

    public void registerGamemodeCommand(){
        new CommandAPICommand("gm")
                .withPermission("buildessentials.command.gm")
                .withArguments(new StringArgument("gamemode")
                        .replaceSuggestions(ArgumentSuggestions.strings(
                                "creative", "1", "spectator", "2", "adventure", "3", "survival", "0"
                        )))
                .withOptionalArguments(new PlayerArgument("player"))
                .executes((sender, args) -> {
                    String gamemode = (String) args.get("gamemode");
                    Player otherPlayer = (Player) args.get("player");
                    if (otherPlayer == null){
                        if (sender instanceof Player){
                            assert gamemode != null;
                            Player p = setPlayerGamemode((Player) sender, gamemode);
                            p.sendMessage("§dEternalBuild §7| §aGamemode updated to §e" + p.getGameMode().name());
                        }
                    } else {
                        assert gamemode != null;
                        if (gamemode.equalsIgnoreCase("creative") || gamemode.equalsIgnoreCase("1")){
                            otherPlayer.setGameMode(GameMode.CREATIVE);
                        } else if (gamemode.equalsIgnoreCase("spectator") || gamemode.equalsIgnoreCase("2")){
                            otherPlayer.setGameMode(GameMode.SPECTATOR);
                        } else if (gamemode.equalsIgnoreCase("adventure") || gamemode.equalsIgnoreCase("3")){
                            otherPlayer.setGameMode(GameMode.ADVENTURE);
                        } else if (gamemode.equalsIgnoreCase("survival") || gamemode.equalsIgnoreCase("0")){
                            otherPlayer.setGameMode(GameMode.SURVIVAL);
                        }
                        sender.sendMessage("§dEternalBuild §7| §aGamemode of §e" + otherPlayer.getName()+ "§a updated to §e" + otherPlayer.getGameMode().name());
                    }
                }).register();
    }

    private static Player setPlayerGamemode(Player sender, String gamemode) {
        assert gamemode != null;
        if (gamemode.equalsIgnoreCase("creative") || gamemode.equalsIgnoreCase("1")){
            sender.setGameMode(GameMode.CREATIVE);
        } else if (gamemode.equalsIgnoreCase("spectator") || gamemode.equalsIgnoreCase("2")){
            sender.setGameMode(GameMode.SPECTATOR);
        } else if (gamemode.equalsIgnoreCase("adventure") || gamemode.equalsIgnoreCase("3")){
            sender.setGameMode(GameMode.ADVENTURE);
        } else if (gamemode.equalsIgnoreCase("survival") || gamemode.equalsIgnoreCase("0")){
            sender.setGameMode(GameMode.SURVIVAL);
        }
        return sender;
    }

    public void registerOtherCommands() {
        new CommandAPICommand("gmc")
                .withOptionalArguments(new PlayerArgument("player"))
                .withPermission("buildessentials.command.gamemode.creative")
                .executes((sender, args) -> {
                    Player target = (Player) args.get("player");
                    if (target == null) {
                        if (sender instanceof Player p) {
                            p.setGameMode(GameMode.CREATIVE);
                            p.sendMessage("§dEternalBuild §7| §aGamemode updated to §e" + p.getGameMode().name());
                        }
                    } else {
                        target.setGameMode(GameMode.CREATIVE);
                        sender.sendMessage("§dEternalBuild §7| §aGamemode of §e" + target.getName()+ "§a updated to §e" + target.getGameMode().name());
                    }
                }).register();

        new CommandAPICommand("gms")
                .withOptionalArguments(new PlayerArgument("player"))
                .withPermission("buildessentials.command.gamemode.survival")
                .executes((sender, args) -> {
                    Player target = (Player) args.get("player");
                    if (target == null) {
                        if (sender instanceof Player p) {
                            p.setGameMode(GameMode.SURVIVAL);
                            p.sendMessage("§dEternalBuild §7| §aGamemode updated to §e" + p.getGameMode().name());
                        }
                    } else {
                        target.setGameMode(GameMode.SURVIVAL);
                        sender.sendMessage("§dEternalBuild §7| §aGamemode of §e" + target.getName()+ "§a updated to §e" + target.getGameMode().name());
                    }
                }).register();

        new CommandAPICommand("gma")
                .withOptionalArguments(new PlayerArgument("player"))
                .withPermission("buildessentials.command.gamemode.adventure")
                .executes((sender, args) -> {
                    Player target = (Player) args.get("player");
                    if (target == null) {
                        if (sender instanceof Player p) {
                            p.setGameMode(GameMode.ADVENTURE);
                            p.sendMessage("§dEternalBuild §7| §aGamemode updated to §e" + p.getGameMode().name());
                        }
                    } else {
                        target.setGameMode(GameMode.ADVENTURE);
                        sender.sendMessage("§dEternalBuild §7| §aGamemode of §e" + target.getName()+ "§a updated to §e" + target.getGameMode().name());
                    }
                }).register();

        new CommandAPICommand("gmspec")
                .withOptionalArguments(new PlayerArgument("player"))
                .withPermission("buildessentials.command.gamemode.spectator")
                .executes((sender, args) -> {
                    Player target = (Player) args.get("player");
                    if (target == null) {
                        if (sender instanceof Player p) {
                            p.setGameMode(GameMode.SPECTATOR);
                            p.sendMessage("§dEternalBuild §7| §aGamemode updated to §e" + p.getGameMode().name());
                        }
                    } else {
                        target.setGameMode(GameMode.SPECTATOR);
                        sender.sendMessage("§dEternalBuild §7| §aGamemode of §e" + target.getName()+ "§a updated to §e" + target.getGameMode().name());
                    }
                }).register();
    }

}
