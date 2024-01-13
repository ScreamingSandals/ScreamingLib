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

package org.screamingsandals.lib.impl.bungee.proxy;

import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.bungee.spectator.audience.adapter.BungeeAdapter;
import org.screamingsandals.lib.impl.bungee.spectator.audience.adapter.BungeePlayerAdapter;
import org.screamingsandals.lib.proxy.ProxiedPlayer;
import org.screamingsandals.lib.proxy.ProxiedSender;
import org.screamingsandals.lib.sender.permissions.*;
import org.screamingsandals.lib.spectator.audience.PlayerAudience;
import org.screamingsandals.lib.spectator.audience.adapter.Adapter;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Locale;

public class BungeeProxiedSender extends BasicWrapper<CommandSender> implements ProxiedSender {
    public BungeeProxiedSender(@NotNull CommandSender wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull Type getType() {
        return wrappedObject instanceof net.md_5.bungee.api.connection.ProxiedPlayer ? Type.PLAYER : Type.UNKNOWN;
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
            return wrappedObject.getPermissions().contains(((SimplePermission) permission).getPermissionString());
        }
        return true;
    }

    @Override
    public @NotNull String getName() {
        return wrappedObject.getName();
    }

    @Override
    public @NotNull Locale getLocale() {
        return Locale.US;
    }

    @Override
    public @NotNull Adapter adapter() {
        if (wrappedObject instanceof net.md_5.bungee.api.connection.ProxiedPlayer && this instanceof ProxiedPlayer) {
            return new BungeePlayerAdapter((net.md_5.bungee.api.connection.ProxiedPlayer) wrappedObject, (PlayerAudience) this);
        /*} else if (source instanceof ConsoleCommandSource && TODO) {
            return new BungeeConsoleAudience(wrappedObject, (ConsoleAudience) this);*/
        }
        return new BungeeAdapter(wrappedObject, this);
    }
}
