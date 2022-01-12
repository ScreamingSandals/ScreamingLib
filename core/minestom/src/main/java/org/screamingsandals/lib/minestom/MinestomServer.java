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

package org.screamingsandals.lib.minestom;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.Version;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.WorldHolder;
import org.screamingsandals.lib.world.WorldMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MinestomServer extends Server {
    private final Version currentVersion = new Version(MinecraftServer.VERSION_NAME);

    @Override
    public Version getVersion0() {
        return currentVersion;
    }

    @Override
    public String getServerSoftwareVersion0() {
        return MinecraftServer.VERSION_NAME;
    }

    @Override
    public boolean isServerThread0() {
        return true;
    }

    @Override
    public List<PlayerWrapper> getConnectedPlayers0() {
        return MinecraftServer.getConnectionManager().getOnlinePlayers().stream()
                .map(PlayerMapper::wrapPlayer)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayerWrapper> getConnectedPlayersFromWorld0(WorldHolder world) {
        return world.as(Instance.class).getPlayers().stream()
                .map(PlayerMapper::wrapPlayer)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorldHolder> getWorlds0() {
        return MinecraftServer.getInstanceManager().getInstances().stream()
                .map(WorldMapper::wrapWorld)
                .collect(Collectors.toList());
    }

    @Override
    public void runSynchronously0(Runnable task) {
        MinecraftServer.getSchedulerManager().buildTask(task).schedule();
    }

    @Override
    public void shutdown0() {
        MinecraftServer.getServer().stop();
    }

    @Override
    public Integer getProtocolVersion0() {
        return MinecraftServer.PROTOCOL_VERSION;
    }
}
