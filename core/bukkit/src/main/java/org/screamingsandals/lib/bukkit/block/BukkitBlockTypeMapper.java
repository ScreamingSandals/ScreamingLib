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

package org.screamingsandals.lib.bukkit.block;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.block.data.BlockData;
import org.bukkit.material.MaterialData;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.block.BlockTypeMapper;
import org.screamingsandals.lib.block.tags.ModernBlockTagBackPorts;
import org.screamingsandals.lib.bukkit.block.tags.BukkitLegacyTagResolution;
import org.screamingsandals.lib.bukkit.tags.KeyedUtils;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BukkitBlockTypeMapper extends BlockTypeMapper {
    private static final Map<Material, List<String>> tagBackPorts = new HashMap<>();

    public BukkitBlockTypeMapper() {
        if (Server.isVersion(1, 13)) {
            blockTypeConverter
                    .registerP2W(Material.class, BukkitBlockTypeHolder::new)
                    .registerP2W(BlockData.class, BukkitBlockTypeHolder::new);

            Arrays.stream(Material.values())
                    .filter(t -> !t.name().startsWith("LEGACY") && t.isBlock())
                    .forEach(material -> {
                        var holder = new BukkitBlockTypeHolder(material);
                        var namespaced = material.getKey();
                        /* In case this is a hybrid server and it actually works correctly (unlike Mohist), we should not assume everything is in minecraft namespace */
                        mapping.put(NamespacedMappingKey.of(namespaced.getNamespace(), namespaced.getKey()), holder);
                        values.add(holder);
                        /* we are probably not able to backport non-minecraft block tags (probably except mineable/* and similar, but we are not able to backport them yet */
                        if (NamespacedKey.MINECRAFT.equals(namespaced.getNamespace())) {
                            var backPorts = ModernBlockTagBackPorts.getPortedTags(holder, s ->
                                KeyedUtils.isTagged(Tag.REGISTRY_BLOCKS, new NamespacedKey("minecraft", s.toLowerCase(Locale.ROOT)), Material.class, material)
                            );
                            if (backPorts != null && !backPorts.isEmpty()) {
                                tagBackPorts.put(material, backPorts);
                            }
                        }
                    });
        } else {
            blockTypeConverter
                    .registerP2W(Material.class, BukkitBlockTypeLegacyHolder::new)
                    .registerP2W(MaterialData.class, BukkitBlockTypeLegacyHolder::new);

            Arrays.stream(Material.values())
                    .filter(Material::isBlock)
                    .forEach(material -> {
                        var holder = new BukkitBlockTypeLegacyHolder(material);
                        /* In legacy we are not able to determine the namespace :( but hybrid servers require java 8 for 1.12.2 and less, so we can't run on them anyway */
                        mapping.put(NamespacedMappingKey.of(material.name()), holder);
                        values.add(holder);
                        var backPorts = BukkitLegacyTagResolution.constructTags(material);
                        if (!backPorts.isEmpty()) {
                            tagBackPorts.put(material, backPorts);
                        }
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
    protected boolean isLegacy() {
        return !Server.isVersion(1, 13);
    }

    public static boolean hasTagInBackPorts(Material material, String tag) {
        return tagBackPorts.containsKey(material) && tagBackPorts.get(material).contains(tag);
    }
}
