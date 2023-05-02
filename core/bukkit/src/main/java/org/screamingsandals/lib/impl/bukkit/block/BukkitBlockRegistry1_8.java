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

package org.screamingsandals.lib.impl.bukkit.block;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.impl.bukkit.block.tags.BukkitLegacyTagResolution;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;

import java.util.*;
import java.util.stream.Collectors;

public class BukkitBlockRegistry1_8 extends BukkitBlockRegistry {
    private static final @NotNull Map<@NotNull Material, List<String>> tagBackPorts = new HashMap<>();
    @Getter
    protected final @NotNull Map<@NotNull ResourceLocation, @NotNull BukkitBlock1_8> ports = new HashMap<>();
    @Getter
    protected final @NotNull Map<@NotNull BukkitBlock1_8, @NotNull ResourceLocation> resourceLocations = new HashMap<>();

    public BukkitBlockRegistry1_8() {
        specialType(Material.class, mat -> mat.isBlock() ? new BukkitBlock1_8(mat) : null);
        specialType(MaterialData.class, data -> data.getItemType().isBlock() ? new BukkitBlock1_8(data) : null);

        // Tag backports

        Arrays.stream(Material.values())
                .filter(Material::isBlock)
                .forEach(material -> {
                    var backPorts = BukkitLegacyTagResolution.constructTags(material);
                    if (!backPorts.isEmpty()) {
                        tagBackPorts.put(material, backPorts);
                    }
                });
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull Block> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                () ->  ports.entrySet().stream(), // this is probably wrong, but whatever :)
                Map.Entry::getValue,
                Map.Entry::getKey,
                (entry, literal) -> entry.getKey().path().contains(literal),
                (entry, namespace) -> entry.getKey().namespace().equals(namespace),
                List.of()
        );
    }

    @Override
    protected @Nullable Block resolveMappingPlatform(@NotNull ResourceLocation location, @Nullable String blockState) {
        var type = resolveMappingPlatform(location);
        if (type != null && blockState != null) {
            if (blockState.startsWith("[") && blockState.endsWith("]")) {
                var values = blockState.substring(1, blockState.length() - 1);
                if (values.isBlank()) {
                    return type;
                }
                var map = Arrays.stream(values.split(","))
                        .map(next -> next.split("="))
                        .collect(Collectors.toMap(next -> next[0], next -> next[1]));

                return type.withStateData(map);
            }

            return null;
        } else {
            return type;
        }
    }

    private @Nullable Block resolveMappingPlatform(@NotNull ResourceLocation location) {
        var material = ports.get(location);
        if (material != null) {
            return material;
        }

        var namespace = location.namespace();
        var path = location.path();
        if ("minecraft".equals(namespace)) {
            if (path.startsWith("legacy/")) {
                // allow special location: minecraft:legacy/enum_name or minecraft:legacy/enum_name/data_value (e.g minecraft:legacy/stone or minecraft:legacy/stone/2)
                var name = path.substring(7);
                var dataSectionIndex = name.indexOf('/');
                if (dataSectionIndex != name.lastIndexOf('/')) {
                    return null; // invalid
                }
                var dataValue = 0;
                if (dataSectionIndex != -1) {
                    try {
                        dataValue = Integer.parseInt(name.substring(dataSectionIndex + 1));
                        name = name.substring(0, dataSectionIndex);
                    } catch (NumberFormatException ignored) {
                        return null; // invalid
                    }
                }

                try {
                    var value = Material.valueOf(name.toUpperCase(Locale.ROOT).replace('-', '_'));
                    if (!value.isBlock()) {
                        return null;
                    }
                    return new BukkitBlock1_8(value, (byte) dataValue);
                } catch (IllegalArgumentException ignored) {
                }
            } else if (path.startsWith("legacy-id/")) {
                // allow special location: minecraft:legacy-id/id or minecraft:legacy-id/id/data_value (e.g minecraft:legacy-id/1 or minecraft:legacy-id/1/2)
                var compoundId = path.substring(10);
                var dataSectionIndex = compoundId.indexOf('/');
                if (dataSectionIndex != compoundId.lastIndexOf('/')) {
                    return null; // invalid
                }
                var stringId = compoundId;
                var dataValue = 0;
                if (dataSectionIndex != -1) {
                    stringId = stringId.substring(0, dataSectionIndex);
                    try {
                        dataValue = Integer.parseInt(compoundId.substring(dataSectionIndex + 1));
                    } catch (NumberFormatException ignored) {
                        return null; // invalid
                    }
                }
                var id = 0;
                try {
                    id = Integer.parseInt(stringId);
                } catch (NumberFormatException ignored) {
                    return null; // invalid
                }

                var mat = Reflect.getMethod(Material.class, "getId", int.class).invokeStatic(id);
                if (mat instanceof Material && ((Material) mat).isBlock()) {
                    return new BukkitBlock1_8((Material) mat, (byte) dataValue);
                }
            }
        } else {
            // A mod in environment with MC 1.8.8-1.12.2 and Java 11+?? How is that even possible? I thought these old mod loaders need explicitly Java 8
            var dataSectionIndex = path.lastIndexOf('/');
            if (dataSectionIndex != -1) {
                try {
                    var dataValue = Integer.parseInt(path.substring(dataSectionIndex + 1));
                    var name = path.substring(0, dataSectionIndex);
                    var compoundName = namespace + "_" + name;

                    try {
                        var value = Material.valueOf(compoundName.toUpperCase(Locale.ROOT).replace('-', '_'));
                        if (!value.isBlock()) {
                            return null;
                        }
                        return new BukkitBlock1_8(value, (byte) dataValue);
                    } catch (IllegalArgumentException ignored) {
                    }

                    // does not seem legal, but let's try that
                    compoundName = namespace + ":" + name;

                    try {
                        var value = Material.valueOf(compoundName.toUpperCase(Locale.ROOT).replace('-', '_'));
                        if (!value.isBlock()) {
                            return null;
                        }
                        return new BukkitBlock1_8(value, (byte) dataValue);
                    } catch (IllegalArgumentException ignored) {
                    }
                } catch (NumberFormatException ignored) {
                    // not a data value
                }


                var compoundName = namespace + "_" + path;

                try {
                    var value = Material.valueOf(compoundName.toUpperCase(Locale.ROOT).replace('-', '_'));
                    if (!value.isBlock()) {
                        return null;
                    }
                    return new BukkitBlock1_8(value);
                } catch (IllegalArgumentException ignored) {
                }

                // does not seem legal, but let's try that
                compoundName = namespace + ":" + path;

                try {
                    var value = Material.valueOf(compoundName.toUpperCase(Locale.ROOT).replace('-', '_'));
                    if (!value.isBlock()) {
                        return null;
                    }
                    return new BukkitBlock1_8(value);
                } catch (IllegalArgumentException ignored) {
                }
            }
        }

        return null;
    }

    public static boolean hasTagInBackPorts(@NotNull Material material, @NotNull String tag) {
        return tagBackPorts.containsKey(material) && tagBackPorts.get(material).contains(tag);
    }
}
