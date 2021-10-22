package org.screamingsandals.lib.bukkit;

import io.netty.channel.ChannelFuture;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.bukkit.world.BukkitWorldHolder;
import org.screamingsandals.lib.nms.accessors.MinecraftServerAccessor;
import org.screamingsandals.lib.nms.accessors.ServerConnectionListenerAccessor;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.WorldHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BukkitServer extends Server {
    private final Plugin plugin;

    private final Map<String, String> UNSAFE_SOUND_CACHE = new HashMap<>();

    {
        try {
            if (Reflect.hasMethod(Sound.class, "getKey")) {
                for (var v : Sound.values()) {
                    if (v.getKey().namespace().equals("minecraft")) {
                        UNSAFE_SOUND_CACHE.put(v.name(), v.getKey().value());
                    }
                }
            } else {
                for (var v : Sound.values()) {
                    var craftSound = Reflect.getMethod(ClassStorage.CB.CraftSound, "getSound", Sound.class).invokeStatic(v);
                    UNSAFE_SOUND_CACHE.put(v.name(), craftSound.toString());
                }
            }
        } catch (Throwable ignored) {
        }
    }

    @Override
    public String getVersion0() {
        if (Version.PATCH_VERSION == 0) {
            return Version.MAJOR_VERSION + "." + Version.MINOR_VERSION;
        }
        return Version.MAJOR_VERSION + "." + Version.MINOR_VERSION + "." + Version.PATCH_VERSION;
    }

    @Override
    public String getServerSoftwareVersion0() {
        return Bukkit.getVersion();
    }

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

    @Override
    public List<WorldHolder> getWorlds0() {
        return Bukkit.getWorlds().stream()
                .map(BukkitWorldHolder::new)
                .collect(Collectors.toList());
    }

    @Override
    public void runSynchronously0(Runnable task) {
        Bukkit.getServer().getScheduler().runTask(plugin, task);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ChannelFuture> getConnections0() {
        return (List<ChannelFuture>) Reflect.fastInvokeResulted(Bukkit.getServer(), "getServer")
                .getFieldResulted(MinecraftServerAccessor.getFieldConnection())
                .getFieldResulted(ServerConnectionListenerAccessor.getFieldChannels())
                .raw();
    }

    @Override
    public String UNSAFE_normalizeSoundKey0(String s) {
        if (UNSAFE_SOUND_CACHE.containsKey(s.toUpperCase())) {
            // TODO: map legacy <-> flattening conversion
            return UNSAFE_SOUND_CACHE.get(s.toUpperCase());
        }
        return super.UNSAFE_normalizeSoundKey0(s);
    }
}
