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

package org.screamingsandals.lib.impl.bukkit.cloud;

import cloud.commandframework.CommandTree;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.player.Players;
import org.screamingsandals.lib.sender.CommandSender;

import java.util.function.Function;

public class PaperScreamingCloudManager extends PaperCommandManager<CommandSender> {
    /**
     * Construct a new Paper command manager
     *
     * @param owningPlugin       Plugin that is constructing the manager
     * @param commandCoordinator Coordinator provider
     */
    public PaperScreamingCloudManager(@NotNull Plugin owningPlugin,
                                      @NotNull Function<CommandTree<CommandSender>, CommandExecutionCoordinator<CommandSender>> commandCoordinator) throws Exception {
        super(owningPlugin, commandCoordinator,
                sender -> {
                    if (sender instanceof Player) {
                        return new BukkitPlayer((Player) sender);
                    }
                    return Players.wrapSender(sender);
                }, sender -> {
                    if (sender.getType() == CommandSender.Type.PLAYER) {
                        return sender.as(Player.class);
                    }
                    return sender.as(org.bukkit.command.CommandSender.class);
                });

        if (queryCapability(CloudBukkitCapabilities.BRIGADIER)) {
            try {
                registerBrigadier();
                brigadierManager().setNativeNumberSuggestions(false);
            } catch (BrigadierFailureException ignored) {
                // Commodore is missing, so no brigadier, but let it at least work
            }
        }

        if (queryCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            registerAsynchronousCompletions();
        }
    }
}
