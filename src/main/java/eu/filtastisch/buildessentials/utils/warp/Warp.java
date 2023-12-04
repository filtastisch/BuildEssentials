package eu.filtastisch.buildessentials.utils.warp;

import lombok.Getter;
import org.bukkit.Location;

@Getter
public class Warp {

    private final Location location;
    private final String warpName;

    public Warp(Location location, String warpName) {
        this.location = location;
        this.warpName = warpName;
    }

}
