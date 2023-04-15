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

package org.screamingsandals.lib.annotation.constants;

import com.squareup.javapoet.ClassName;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class Classes {
    @UtilityClass
    public static class SLib {
        public static final @NotNull ClassName PLUGINS_SERVICE = ClassName.get("org.screamingsandals.lib.plugin", "Plugins");
        public static final @NotNull ClassName PLUGIN = ClassName.get("org.screamingsandals.lib.plugin", "Plugin");
        public static final @NotNull ClassName CONTROLLABLE = ClassName.get("org.screamingsandals.lib.utils", "Controllable");
        public static final @NotNull ClassName CONTROLLABLE_IMPL = ClassName.get("org.screamingsandals.lib.impl.utils", "ControllableImpl");
        public static final @NotNull ClassName CORE = ClassName.get("org.screamingsandals.lib", "Core");
        public static final @NotNull ClassName PROXY_CORE = ClassName.get("org.screamingsandals.lib.proxy", "ProxyCore");
        public static final @NotNull ClassName LOGGER_WRAPPER = ClassName.get("org.screamingsandals.lib.utils.logger", "LoggerWrapper");
        public static final @NotNull ClassName JUL_LOGGER_WRAPPER = ClassName.get("org.screamingsandals.lib.impl.utils.logger", "JULLoggerWrapper");
        public static final @NotNull ClassName SLF4J_LOGGER_WRAPPER = ClassName.get("org.screamingsandals.lib.impl.utils.logger", "Slf4jLoggerWrapper");
        public static final @NotNull ClassName DUAL_LOGGER_WRAPPER = ClassName.get("org.screamingsandals.lib.impl.utils.logger", "DualLoggerWrapper");
        public static final @NotNull ClassName REFLECT = ClassName.get("org.screamingsandals.lib.utils.reflect", "Reflect");
        public static final @NotNull ClassName SERIALIZERS = ClassName.get("org.screamingsandals.lib.configurate", "SLibSerializers");
        public static final @NotNull ClassName SPECTATOR_SERIALIZERS = ClassName.get("org.screamingsandals.lib.spectator.configurate", "SpectatorSerializers");
        public static final @NotNull ClassName EVENT_MANAGER = ClassName.get("org.screamingsandals.lib.event", "EventManager");
        public static final @NotNull ClassName EVENT_HANDLER = ClassName.get("org.screamingsandals.lib.event", "EventHandler");
        public static final @NotNull ClassName EVENT_PRIORITY = ClassName.get("org.screamingsandals.lib.event", "EventPriority");
        public static final @NotNull ClassName EVENT = ClassName.get("org.screamingsandals.lib.event", "Event");
        public static final @NotNull ClassName SERVICE_MANAGER = ClassName.get("org.screamingsandals.lib.plugin", "ServiceManager");
        public static final @NotNull ClassName BUKKIT_PLUGIN = ClassName.get("org.screamingsandals.lib.impl.bukkit.plugin", "BukkitPlugin");
        public static final @NotNull ClassName BUNGEE_PLUGIN = ClassName.get("org.screamingsandals.lib.impl.bungee.plugin", "BungeePlugin");
        public static final @NotNull ClassName VELOCITY_PLUGIN = ClassName.get("org.screamingsandals.lib.impl.velocity.plugin", "VelocityPlugin");
    }

    @UtilityClass
    public static class Configurate {
        public static final @NotNull ClassName CONFIGURATION_LOADER = ClassName.get("org.spongepowered.configurate.loader", "ConfigurationLoader");
    }

    @UtilityClass
    public static class Bukkit {
        public static final @NotNull ClassName PLUGIN = ClassName.get("org.bukkit.plugin", "Plugin");
        public static final @NotNull ClassName PLUGIN_BASE = ClassName.get("org.bukkit.plugin", "PluginBase");
        public static final @NotNull ClassName JAVA_PLUGIN = ClassName.get("org.bukkit.plugin.java", "JavaPlugin");
    }

    @UtilityClass
    public static class Velocity {
        public static final @NotNull ClassName PLUGIN_MANAGER = ClassName.get("com.velocitypowered.api.plugin", "PluginManager");
        public static final @NotNull ClassName PLUGIN_CONTAINER = ClassName.get("com.velocitypowered.api.plugin", "PluginContainer");
        public static final @NotNull ClassName PROXY_SERVER = ClassName.get("com.velocitypowered.api.proxy", "ProxyServer");
        public static final @NotNull ClassName SUBSCRIBE = ClassName.get("com.velocitypowered.api.event", "Subscribe");
        public static final @NotNull ClassName PROXY_INIT_EVENT = ClassName.get("com.velocitypowered.api.event.proxy", "ProxyInitializeEvent");
        public static final @NotNull ClassName PROXY_SHUTDOWN_EVENT = ClassName.get("com.velocitypowered.api.event.proxy", "ProxyShutdownEvent");
    }

    @UtilityClass
    public static class Bungee {
        public static final @NotNull ClassName PLUGIN = ClassName.get("net.md_5.bungee.api.plugin", "Plugin");
    }

    @UtilityClass
    public static class Minestom {
        public static final @NotNull ClassName EXTENSION = ClassName.get("net.minestom.server.extensions", "Extension");
    }

    @UtilityClass
    public static class Guice {
        public static final @NotNull ClassName INJECTOR = ClassName.get("com.google.inject", "Injector");
        public static final @NotNull ClassName INJECT = ClassName.get("com.google.inject", "Inject");
    }

    @UtilityClass
    public static class Slf4j {
        public static final @NotNull ClassName LOGGER = ClassName.get("org.slf4j", "Logger");
    }

    @UtilityClass
    public static class Java {
        public static final @NotNull ClassName NIO_PATH = ClassName.get(java.nio.file.Path.class);
        public static final @NotNull ClassName FILE = ClassName.get(java.io.File.class);
        public static final @NotNull ClassName OBJECT = ClassName.get(java.lang.Object.class);
    }
}
