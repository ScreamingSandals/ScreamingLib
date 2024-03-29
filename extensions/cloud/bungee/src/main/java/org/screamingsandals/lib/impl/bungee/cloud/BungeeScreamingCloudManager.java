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

package org.screamingsandals.lib.impl.bungee.cloud;

import cloud.commandframework.CommandTree;
import cloud.commandframework.bungee.BungeeCommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.bungee.proxy.BungeeProxiedPlayer;
import org.screamingsandals.lib.impl.bungee.proxy.BungeeProxiedSender;
import org.screamingsandals.lib.proxy.ProxiedSender;
import org.screamingsandals.lib.sender.CommandSender;

import java.util.function.Function;

public class BungeeScreamingCloudManager extends BungeeCommandManager<CommandSender> {
    /**
     * Construct a new Bungee command manager
     *
     * @param owningPlugin       Plugin that is constructing the manager
     * @param commandCoordinator Coordinator provider
     */
    public BungeeScreamingCloudManager(@NotNull Plugin owningPlugin,
                                       @NotNull Function<CommandTree<CommandSender>, CommandExecutionCoordinator<CommandSender>> commandCoordinator) {
        super(owningPlugin, commandCoordinator, sender -> {
            if (sender instanceof ProxiedPlayer) {
                return new BungeeProxiedPlayer((ProxiedPlayer) sender);
            }
            return new BungeeProxiedSender(sender);
        }, sender -> {
            if (sender.getType() == ProxiedSender.Type.PLAYER) {
                return sender.as(ProxiedPlayer.class);
            }
            return sender.as(net.md_5.bungee.api.CommandSender.class);
        });
    }
}
