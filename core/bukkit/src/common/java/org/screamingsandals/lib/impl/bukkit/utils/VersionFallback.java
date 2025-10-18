/*
 * Copyright 2025 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.ext.takenaka.platform.MapperPlatform;
import org.screamingsandals.lib.impl.ext.takenaka.platform.MapperPlatforms;
import org.screamingsandals.lib.impl.nms.accessors.server.MinecraftServerAccessor;
import org.screamingsandals.lib.impl.nms.accessors.server.MinecraftServerMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@UtilityClass
public class VersionFallback {
    public static void applyFallbackIfNeeded(@NotNull String pluginName) {
        String[] mappingNamespaces = MapperPlatforms.getCurrentPlatform().getMappingNamespaces();
        String version = MapperPlatforms.getCurrentPlatform().getVersion();

        if (MinecraftServerAccessor.TYPE.get() != null) {
            // everything is fine, we support this version
            Bukkit.getLogger().info("[" + pluginName + "] Loaded NMS modules for " + version + " in namespaces " + Arrays.toString(mappingNamespaces));
            return;
        }

        List<String> usedNamespaces = Arrays.asList(mappingNamespaces);

        String latest = MinecraftServerMapping.MAPPING.getMappings().entrySet().stream()
                .filter(entry -> usedNamespaces.stream().anyMatch(e -> entry.getValue().containsKey(e)))
                .map(Map.Entry::getKey)
                .max(VersionFallback::compareVersions)
                .orElse(null);

        if (latest == null) {
            // so, everything is broken ig
            return;
        }

        if (compareVersions(version, latest) <= 0) {
            // not newer, unsupported!
            Bukkit.getLogger().severe("[" + pluginName + "] Version " + version + " is incompatible with this plugin!");
            return;
        }

        MapperPlatforms.setCurrentPlatform(MapperPlatform.create(latest, MapperPlatforms.getCurrentPlatform().getClassLoader(), mappingNamespaces));
        Bukkit.getLogger().warning("[" + pluginName + "] ==========================");
        Bukkit.getLogger().warning("[" + pluginName + "] This Minecraft version (" + version + ") is newer than the latest supported version (" + latest + ").");
        Bukkit.getLogger().warning("[" + pluginName + "] Attempting to use the latest known mappings. This is only safe if the newer Minecraft version is a bugfix release.");
        Bukkit.getLogger().warning("[" + pluginName + "] Unless we confirm compatibility, do NOT run this version and wait for an update.");
        Bukkit.getLogger().warning("[" + pluginName + "] ==========================");
        Bukkit.getLogger().info("[" + pluginName + "] Loaded fallback NMS modules for " + latest + " in namespaces " + Arrays.toString(mappingNamespaces));
    }

    private static int compareVersions(String v1, String v2) {
        String[] parts1 = v1.split("\\.");
        String[] parts2 = v2.split("\\.");
        int length = Math.max(parts1.length, parts2.length);

        for (int i = 0; i < length; i++) {
            int num1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int num2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;
            if (num1 != num2) {
                return Integer.compare(num1, num2);
            }
        }
        return 0;
    }
}