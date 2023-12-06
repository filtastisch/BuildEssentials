package eu.filtastisch.buildessentials.utils.warp;

import eu.filtastisch.buildessentials.BuildEssentials;
import lombok.Getter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class WarpManager {

    @Getter
    private static final List<Warp> allWarps = new ArrayList<>();

    public static List<String> getWarpNames(){
        return allWarps.stream().map(Warp::getWarpName).collect(Collectors.toList());
    }

    public static boolean addWarp(String name, Location loc, String perm, String uuid){
        if (exists(name))
            return false;

        allWarps.add(new Warp(loc, name, perm, UUID.fromString(uuid)));
        return true;
    }

    public static boolean safeAddWarp(String name, Location loc, String perm, UUID uuid){
        if (exists(name))
            return false;

        allWarps.add(new Warp(loc, name, perm, uuid));
        BuildEssentials.getInstance().getWarpsConfig().setLocation("warps." + name + ".location", loc);
        BuildEssentials.getInstance().getWarpsConfig().set("warps." + name + ".creator", uuid.toString());
        BuildEssentials.getInstance().getWarpsConfig().set("warps." + name + ".permission", perm);
        BuildEssentials.getInstance().getWarpsConfig().save();
        return true;
    }

    public static boolean deleteWarp(String name){
        if (!exists(name))
            return false;
        BuildEssentials.getInstance().getWarpsConfig().set("warps", null);
        allWarps.remove(getWarp(name));
        BuildEssentials.getInstance().safeWarps();
        return true;
    }

    public static Warp getWarp(String name){
        return allWarps.stream().filter(c -> c.getWarpName().equals(name)).findFirst().orElse(null);
    }

    public static boolean exists(String name){
        return allWarps.stream().anyMatch(c -> c.getWarpName().equals(name));
    }

}
