package org.screamingsandals.lib.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.WorldHolder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BukkitServer extends Server {

    @Override
    public boolean isVersion0(int major, int minor) {
        return Version.isVersion(major, minor);
    }

    @Override
    public boolean isVersion0(int major, int minor, int patch) {
        return Version.isVersion(major, minor, patch);
    }

    @Override
    public boolean isServerThread0() {
        return Bukkit.getServer().isPrimaryThread();
    }

    @Override
    public List<PlayerWrapper> getConnectedPlayers0() {
        return Bukkit.getOnlinePlayers()
                .stream()
                .map(PlayerMapper::wrapPlayer)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayerWrapper> getConnectedPlayersFromWorld0(WorldHolder holder) {
        return holder.as(World.class).getPlayers()
                .stream()
                .map(PlayerMapper::wrapPlayer)
                .collect(Collectors.toList());
    }
}
