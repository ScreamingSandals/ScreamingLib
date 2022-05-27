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

package org.screamingsandals.lib.cloud;

import cloud.commandframework.CommandManager;
import cloud.commandframework.CommandTree;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.function.Function;

@AbstractService
public abstract class CloudConstructor {
    private static CloudConstructor cloudConstructor;

    @ApiStatus.Internal
    public CloudConstructor() {
        if (cloudConstructor != null) {
            throw new UnsupportedOperationException("CloudConstructor has been already initialized!");
        }

        cloudConstructor = this;
    }

    public static CommandManager<CommandSenderWrapper> construct(Function<CommandTree<CommandSenderWrapper>, CommandExecutionCoordinator<CommandSenderWrapper>> commandCoordinator) throws Exception {
        if (cloudConstructor == null) {
            throw new UnsupportedOperationException("CloudConstructor is not initialized yet!");
        }
        return cloudConstructor.construct0(commandCoordinator);
    }

    public abstract CommandManager<CommandSenderWrapper> construct0(Function<CommandTree<CommandSenderWrapper>, CommandExecutionCoordinator<CommandSenderWrapper>> commandCoordinator) throws Exception;
}