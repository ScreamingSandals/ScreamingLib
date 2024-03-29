/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.impl.velocity.cloud;

import cloud.commandframework.CommandManager;
import cloud.commandframework.CommandTree;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.cloud.CloudConstructor;
import org.screamingsandals.lib.sender.CommandSender;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.impl.velocity.proxy.VelocityProxiedPlayerMapper;

import java.util.function.Function;

@Service
@ServiceDependencies(dependsOn = VelocityProxiedPlayerMapper.class)
@RequiredArgsConstructor
public class VelocityCloudConstructor extends CloudConstructor {
    private final @NotNull PluginContainer plugin;
    private final @NotNull ProxyServer proxyServer;

    @Override
    public @NotNull CommandManager<CommandSender> construct0(@NotNull Function<CommandTree<CommandSender>, CommandExecutionCoordinator<CommandSender>> commandCoordinator) {
        return new VelocityScreamingCloudManager(plugin, proxyServer, commandCoordinator);
    }
}
