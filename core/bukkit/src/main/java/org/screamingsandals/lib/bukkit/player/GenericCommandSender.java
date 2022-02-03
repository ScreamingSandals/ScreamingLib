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

package org.screamingsandals.lib.bukkit.player;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.bukkit.BukkitCore;
import org.screamingsandals.lib.player.SenderWrapper;
import org.screamingsandals.lib.spectator.audience.adapter.Adapter;
import org.screamingsandals.lib.utils.BasicWrapper;

public class GenericCommandSender extends BasicWrapper<CommandSender> implements SenderWrapper {
    public GenericCommandSender(CommandSender wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public Type getType() {
        if (wrappedObject instanceof Player) {
            return Type.PLAYER;
        } else if (wrappedObject instanceof ConsoleCommandSender) {
            return Type.CONSOLE;
        } else {
            return Type.UNKNOWN;
        }
    }

    @Override
    public void sendMessage(String message) {
        wrappedObject.sendMessage(message);
    }

    @Override
    public String getName() {
        return wrappedObject.getName();
    }

    @Override
    public boolean isOp() {
        return wrappedObject.isOp();
    }

    @Override
    public void setOp(boolean op) {
        wrappedObject.setOp(op);
    }

    @Override
    public void tryToDispatchCommand(String command) {
        Bukkit.dispatchCommand(wrappedObject, command);
    }

    @Override
    @NotNull
    public Adapter adapter() {
        return BukkitCore.getSpectatorBackend().adapter(this, wrappedObject);
    }
}
