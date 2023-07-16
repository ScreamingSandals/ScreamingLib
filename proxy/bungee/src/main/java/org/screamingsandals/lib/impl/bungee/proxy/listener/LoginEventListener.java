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

package org.screamingsandals.lib.impl.bungee.proxy.listener;

import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.EventExecutionOrder;
import org.screamingsandals.lib.impl.bungee.event.AbstractBungeeEventHandlerFactory;

import java.util.function.Consumer;

public class LoginEventListener extends AbstractBungeeEventHandlerFactory<LoginEvent, BungeePlayerLoginEvent> {
    public LoginEventListener(@NotNull Plugin plugin, @NotNull PluginManager pluginManager) {
        super(LoginEvent.class, BungeePlayerLoginEvent.class, plugin, pluginManager);
    }

    @Override
    protected @Nullable BungeePlayerLoginEvent wrapEvent(@NotNull LoginEvent event, @NotNull EventExecutionOrder priority) {
        return new BungeePlayerLoginEvent(event);
    }

    @Override
    protected @NotNull Listener constructLowestPriorityHandler(@NotNull Consumer<@NotNull LoginEvent> handler) {
        return new Listener() {
            @EventHandler(priority = net.md_5.bungee.event.EventPriority.LOWEST)
            public void onEvent(@NotNull LoginEvent event) {
                handler.accept(event);
            }
        };
    }

    @Override
    protected @NotNull Listener constructLowPriorityHandler(@NotNull Consumer<@NotNull LoginEvent> handler) {
        return new Listener() {
            @EventHandler(priority = net.md_5.bungee.event.EventPriority.LOW)
            public void onEvent(@NotNull LoginEvent event) {
                handler.accept(event);
            }
        };
    }

    @Override
    protected @NotNull Listener constructNormalPriorityHandler(@NotNull Consumer<@NotNull LoginEvent> handler) {
        return new Listener() {
            @EventHandler(priority = net.md_5.bungee.event.EventPriority.NORMAL)
            public void onEvent(@NotNull LoginEvent event) {
                handler.accept(event);
            }
        };
    }

    @Override
    protected @NotNull Listener constructHighPriorityHandler(@NotNull Consumer<@NotNull LoginEvent> handler) {
        return new Listener() {
            @EventHandler(priority = net.md_5.bungee.event.EventPriority.HIGH)
            public void onEvent(@NotNull LoginEvent event) {
                handler.accept(event);
            }
        };
    }

    @Override
    protected @NotNull Listener constructHighestPriorityHandler(@NotNull Consumer<@NotNull LoginEvent> handler) {
        return new Listener() {
            @EventHandler(priority = net.md_5.bungee.event.EventPriority.HIGHEST)
            public void onEvent(@NotNull LoginEvent event) {
                handler.accept(event);
            }
        };
    }

    @Override
    protected @NotNull Listener constructMonitorPriorityHandler(@NotNull Consumer<@NotNull LoginEvent> handler) {
        return new Listener() {
            @EventHandler(priority = 127)
            public void onEvent(@NotNull LoginEvent event) {
                handler.accept(event);
            }
        };
    }
}
