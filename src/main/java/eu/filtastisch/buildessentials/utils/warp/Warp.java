package eu.filtastisch.buildessentials.utils.warp;

import lombok.Getter;
import org.bukkit.Location;

import java.util.UUID;

@Getter
public class Warp {

    private final Location location;
    private final String warpName, permission;
    private final UUID creator;

    public Warp(Location location, String warpName, String permission, UUID creator) {
        this.location = location;
        this.warpName = warpName;
        this.permission = permission;
        this.creator = creator;
    }

}
