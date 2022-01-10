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

package org.screamingsandals.lib.bukkit.block;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.material.MaterialData;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.block.BlockTypeMapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BukkitBlockTypeMapper extends BlockTypeMapper {
    private final Map<String, Map<String, String>> defaultFlatteningBlockDataCache = new HashMap<>();

    public BukkitBlockTypeMapper() {
        if (Server.isVersion(1, 13)) {
            blockTypeConverter
                    .registerP2W(Material.class, BukkitBlockTypeHolder::new)
                    .registerP2W(BlockData.class, BukkitBlockTypeHolder::new);

            Arrays.stream(Material.values())
                    .filter(t -> !t.name().startsWith("LEGACY") && t.isBlock())
                    .forEach(material -> {
                        var holder = new BukkitBlockTypeHolder(material);
                        mapping.put(NamespacedMappingKey.of(material.name()), holder);
                        values.add(holder);
                    });
        } else {
            blockTypeConverter
                    .registerP2W(Material.class, BukkitBlockTypeLegacyHolder::new)
                    .registerP2W(MaterialData.class, BukkitBlockTypeLegacyHolder::new);

            Arrays.stream(Material.values())
                    .filter(Material::isBlock)
                    .forEach(material -> {
                        var holder = new BukkitBlockTypeLegacyHolder(material);
                        mapping.put(NamespacedMappingKey.of(material.name()), holder);
                        values.add(holder);
                    });
        }
    }

    protected Map<String, String> getDataFromString(String data) {
        Preconditions.checkNotNull(data, "Data cannot be null!");
        if (data.contains("[") && data.contains("]")) {
            final var values = data.substring(data.indexOf("[") + 1, data.lastIndexOf("]"));
            if (values.isEmpty()) {
                return Map.of();
            }
            return Arrays.stream(values.split(","))
                    .map(next -> next.split("="))
                    .collect(Collectors.toMap(next -> next[0], next1 -> next1[1]));
        }
        return Map.of();
    }

    @Override
    protected BlockTypeHolder normalize(BlockTypeHolder abnormal) {
        try {
            if (Server.isVersion(1, 13)) {
                var cache = defaultFlatteningBlockDataCache.get(abnormal.platformName());
                if (cache == null) {
                    cache = getDataFromString(abnormal.as(Material.class).createBlockData().getAsString());
                    defaultFlatteningBlockDataCache.put(abnormal.platformName(), cache);
                }
                if (cache.isEmpty()) {
                    return abnormal;
                }
                var flatteningData = abnormal.flatteningData();
                if (flatteningData != null && !flatteningData.isEmpty()) {
                    var clone = new HashMap<>(flatteningData);
                    cache.forEach((s, s2) -> {
                        if (!clone.containsKey(s)) {
                            clone.put(s, s2);
                        }
                    });
                    return abnormal.withFlatteningData(clone);
                }
                return abnormal.withFlatteningData(cache);
            } else {
                // non-flattening versions don't have flattening data
                return abnormal.withFlatteningData(null);
            }
        } catch (Exception ignored) {}
        return abnormal;
    }

    @Override
    protected boolean isLegacy() {
        return !Server.isVersion(1, 13);
    }
}
