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

package org.screamingsandals.lib.impl.velocity.proxy;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.adventure.spectator.audience.adapter.AdventureAdapter;
import org.screamingsandals.lib.impl.adventure.spectator.audience.adapter.AdventurePlayerAdapter;
import org.screamingsandals.lib.proxy.ProxiedPlayerWrapper;
import org.screamingsandals.lib.proxy.ProxiedSenderWrapper;
import org.screamingsandals.lib.sender.permissions.*;
import org.screamingsandals.lib.spectator.audience.PlayerAudience;
import org.screamingsandals.lib.spectator.audience.adapter.Adapter;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Locale;

public class VelocityProxiedSenderWrapper extends BasicWrapper<CommandSource> implements ProxiedSenderWrapper {
    public VelocityProxiedSenderWrapper(@NotNull CommandSource wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull Type getType() {
        return wrappedObject instanceof Player ? Type.PLAYER : (wrappedObject instanceof ConsoleCommandSource ? Type.CONSOLE : Type.UNKNOWN);
    }

    @Override
    public boolean hasPermission(@NotNull Permission permission) {
        if (permission instanceof SimplePermission) {
            if (isPermissionSet(permission)) {
                return wrappedObject.hasPermission(((SimplePermission) permission).getPermissionString());
            } else {
                return ((SimplePermission) permission).isDefaultAllowed();
            }
        } else if (permission instanceof AndPermission) {
            return ((AndPermission) permission).getPermissions().stream().allMatch(this::hasPermission);
        } else if (permission instanceof OrPermission) {
            return ((OrPermission) permission).getPermissions().stream().anyMatch(this::hasPermission);
        } else if (permission instanceof PredicatePermission) {
            return permission.hasPermission(this);
        }
        return false;
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission permission) {
        if (permission instanceof SimplePermission) {
            return wrappedObject.getPermissionValue(((SimplePermission) permission).getPermissionString()) != Tristate.UNDEFINED;
        }
        return true;
    }

    @Override
    public @NotNull String getName() {
        return "CONSOLE";
    }

    @Override
    public @NotNull Locale getLocale() {
        return Locale.US;
    }

    @Override
    public @NotNull Adapter adapter() {
        if (wrappedObject instanceof Player && this instanceof ProxiedPlayerWrapper) {
            return new AdventurePlayerAdapter(wrappedObject, (PlayerAudience) this);
        /*} else if (wrappedObject instanceof ConsoleCommandSource && TODO) {
            return new AdventureConsoleAdapter(wrappedObject, (ConsoleAudience) this);*/
        }
        return new AdventureAdapter(wrappedObject, this);
    }
}
