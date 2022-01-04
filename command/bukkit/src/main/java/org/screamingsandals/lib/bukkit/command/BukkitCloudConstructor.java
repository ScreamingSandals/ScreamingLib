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

package org.screamingsandals.lib.bukkit.command;

import cloud.commandframework.CommandManager;
import cloud.commandframework.CommandTree;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.player.BukkitPlayerMapper;
import org.screamingsandals.lib.command.CloudConstructor;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.function.Function;

@Service(dependsOn = {
        BukkitPlayerMapper.class
})
public class BukkitCloudConstructor extends CloudConstructor {
    private final Plugin plugin;

    public static void init(Plugin plugin, Controllable controllable) {
        CloudConstructor.init(() -> new BukkitCloudConstructor(plugin));
    }

    public BukkitCloudConstructor(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandManager<CommandSenderWrapper> construct0(Function<CommandTree<CommandSenderWrapper>, CommandExecutionCoordinator<CommandSenderWrapper>> commandCoordinator) throws Exception {
        return new PaperScreamingCloudManager(plugin, commandCoordinator);
    }
}
