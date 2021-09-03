package dev._2lstudios.worldsentinel.region;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class RegionPlayerManager {
    private final Map<String, RegionPlayer> regionPlayers;

    public RegionPlayerManager() {
        this.regionPlayers = new HashMap<String, RegionPlayer>();
    }

    public void addPlayer(final Player player) {
        this.regionPlayers.put(player.getName(), new RegionPlayer());
    }

    public void load() {
        Bukkit.getOnlinePlayers().forEach(this::addPlayer);
    }

    public void removePlayer(final Player player) {
        this.regionPlayers.remove(player.getName());
    }

    public RegionPlayer getPlayer(final Player player) {
        return this.regionPlayers.getOrDefault(player.getName(), null);
    }
}
