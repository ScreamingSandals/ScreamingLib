/*
 * Copyright 2023 ScreamingSandals
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

package org.screamingsandals.lib.velocity.cloud;

import cloud.commandframework.CommandTree;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.velocity.VelocityCommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.proxy.ProxiedSenderWrapper;
import org.screamingsandals.lib.sender.CommandSender;
import org.screamingsandals.lib.velocity.proxy.VelocityProxiedPlayerWrapper;
import org.screamingsandals.lib.velocity.proxy.VelocityProxiedSenderWrapper;

import java.util.function.Function;

public class VelocityScreamingCloudManager extends VelocityCommandManager<CommandSender> {

    /**
     * Construct a new Velocity command manager
     *
     * @param plugin             Container for the owning plugin. Nullable for backwards compatibility
     * @param proxyServer        ProxyServer instance
     * @param commandCoordinator Coordinator provider
     */
    public VelocityScreamingCloudManager(@NotNull PluginContainer plugin,
                                         @NotNull ProxyServer proxyServer,
                                         @NotNull Function<CommandTree<CommandSender>, CommandExecutionCoordinator<CommandSender>> commandCoordinator) {
        super(plugin, proxyServer, commandCoordinator, sender -> {
            if (sender instanceof Player) {
                return new VelocityProxiedPlayerWrapper((Player) sender);
            }
            return new VelocityProxiedSenderWrapper(sender);
        }, sender -> {
            if (sender.getType() == ProxiedSenderWrapper.Type.PLAYER) {
                return sender.as(Player.class);
            }
            return sender.as(CommandSource.class);
        });
    }
}
