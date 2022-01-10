/*
 * Copyright 2022 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.bukkit;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.bukkit.block.BukkitBlockTypeHolder;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.Version;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.WorldHolder;
import org.screamingsandals.lib.world.WorldMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BukkitServer extends Server {
    private final Version currentVersion = Version.extract(Bukkit.getVersion());
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
    public Version getVersion0() {
        return currentVersion;
    }

    @Override
    public String getServerSoftwareVersion0() {
        return Bukkit.getVersion();
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
                .map(WorldMapper::wrapWorld)
                .collect(Collectors.toList());
    }

    @Override
    public void runSynchronously0(Runnable task) {
        Bukkit.getServer().getScheduler().runTask(plugin, task);
    }

    @Override
    public void shutdown0() {
        Bukkit.shutdown();
    }

    @Override
    public Integer getProtocolVersion0() {
        if (SharedConstantsAccessor.getMethodGetProtocolVersion1() != null) {
            return Reflect.fastInvokeResulted(SharedConstantsAccessor.getMethodGetProtocolVersion1()).as(Integer.class);
        }
        return Reflect.getFieldResulted(Reflect.fastInvoke(Bukkit.getServer(), "getServer"), MinecraftServerAccessor.getFieldStatus())
                .fastInvokeResulted(ServerStatusAccessor.getMethodGetVersion1())
                .fastInvokeResulted(ServerStatus_i_VersionAccessor.getMethodGetProtocol1())
                .as(Integer.class);
    }

    @Override
    public String UNSAFE_normalizeSoundKey0(String s) {
        if (UNSAFE_SOUND_CACHE.containsKey(s.toUpperCase())) {
            // TODO: map legacy <-> flattening conversion
            return UNSAFE_SOUND_CACHE.get(s.toUpperCase());
        }
        return super.UNSAFE_normalizeSoundKey0(s);
    }

    @Override
    public void UNSAFE_earlyInitializeLegacySupportAndIgnoreItsUsage0() {
        if (isVersion0(1, 13)) {
            ClassStorage.CB.UNSAFE_EVIL_GET_OUT_getCraftLegacy();
            BukkitBlockTypeHolder.NAG_AUTHOR_ABOUT_LEGACY_METHOD_USED = true;
        }
    }
}
