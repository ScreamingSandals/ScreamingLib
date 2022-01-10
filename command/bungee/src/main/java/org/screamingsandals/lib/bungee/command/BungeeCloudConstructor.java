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

package org.screamingsandals.lib.bungee.command;

import cloud.commandframework.CommandManager;
import cloud.commandframework.CommandTree;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import net.md_5.bungee.api.plugin.Plugin;
import org.screamingsandals.lib.bungee.proxy.BungeeProxiedPlayerMapper;
import org.screamingsandals.lib.command.CloudConstructor;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.function.Function;

@Service(dependsOn = {
        BungeeProxiedPlayerMapper.class
})
public class BungeeCloudConstructor extends CloudConstructor {
    private final Plugin plugin;

    public static void init(Plugin plugin) {
        CloudConstructor.init(() -> new BungeeCloudConstructor(plugin));
    }

    public BungeeCloudConstructor(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandManager<CommandSenderWrapper> construct0(Function<CommandTree<CommandSenderWrapper>, CommandExecutionCoordinator<CommandSenderWrapper>> commandCoordinator) {
        return new BungeeScreamingCloudManager(plugin, commandCoordinator);
    }
}
