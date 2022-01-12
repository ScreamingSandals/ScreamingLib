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

package org.screamingsandals.lib.minestom.player;

import net.kyori.adventure.audience.Audience;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.player.SenderWrapper;
import org.screamingsandals.lib.utils.BasicWrapper;

public class MinestomCommandSender extends BasicWrapper<CommandSender> implements SenderWrapper {
    public MinestomCommandSender(CommandSender wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public void tryToDispatchCommand(String command) {
        MinecraftServer.getCommandManager().execute(wrappedObject, command);
    }

    @Override
    public Type getType() {
        if (wrappedObject instanceof Player) {
            return Type.PLAYER;
        } else if (wrappedObject instanceof ConsoleSender) {
            return Type.CONSOLE;
        }
        return Type.UNKNOWN;
    }

    @Override
    public void sendMessage(String message) {
        wrappedObject.sendMessage(message);
    }

    @Override
    public String getName() {
        return (wrappedObject instanceof Player) ? ((Player) wrappedObject).getUsername() : "CONSOLE";
    }

    @Override
    public @NotNull Audience audience() {
        return wrappedObject;
    }

    @Override
    public boolean isOp() {
        return false;
    }

    @Override
    public void setOp(boolean op) {
        // empty stub
    }
}
