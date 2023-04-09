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

package org.screamingsandals.lib.block;

import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.ItemBlockIdsRemapper;
import org.screamingsandals.lib.configurate.BlockTypeHolderSerializer;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ProvidedService;
import org.screamingsandals.lib.utils.key.*;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@ProvidedService
public abstract class BlockTypeMapper extends AbstractTypeMapper<BlockType> {
    private static final @NotNull Pattern RESOLUTION_PATTERN = Pattern.compile("^(((?<namespaced>(?:([A-Za-z][A-Za-z0-9_.\\-]*):)?[A-Za-z][A-Za-z0-9_.\\-/ ]*)(?<blockState>:\\d*|\\[[^]]*])?)|((?<id>\\d+)(?::)?(?<data>\\d+)?))$");
    @Getter
    protected final @NotNull Map<@NotNull Predicate<@NotNull ResourceLocation>, Pair<Function<Byte, Map<String, String>>, Function<Map<String, String>, Byte>>> blockDataTranslators = new HashMap<>();
    protected final @NotNull BidirectionalConverter<BlockType> blockTypeConverter = BidirectionalConverter.<BlockType>build()
            .registerP2W(BlockType.class, i -> i)
            .registerP2W(ConfigurationNode.class, node -> {
                try {
                    return BlockTypeHolderSerializer.INSTANCE.deserialize(BlockTypeMapper.class, node);
                } catch (SerializationException ex) {
                    ex.printStackTrace();
                    return null;
                }
            });

    private static @Nullable BlockTypeMapper blockTypeMapper;
    private static @Nullable BlockType cachedAir;

    @ApiStatus.Internal
    public BlockTypeMapper() {
        if (blockTypeMapper != null) {
            throw new UnsupportedOperationException("BlockTypeMapper is already initialized.");
        }

        blockTypeMapper = this;
    }

    @Contract("null -> null")
    public static @Nullable BlockType resolve(@Nullable Object materialObject) {
        if (blockTypeMapper == null) {
            throw new UnsupportedOperationException("BlockTypeMapper is not initialized yet.");
        }
        if (materialObject == null) {
            return null;
        }

        return blockTypeMapper.blockTypeConverter.convertOptional(materialObject).or(() -> {
            var material = materialObject.toString().trim();

            var matcher = RESOLUTION_PATTERN.matcher(material);

            if (matcher.matches()) {
                if (matcher.group("namespaced") != null) {

                    var namespaced = ResourceLocation.of(matcher.group("namespaced"));

                    Integer data = null;
                    Map<String, String> flatteningData = null;
                    var blockState = matcher.group("blockState");
                    if (blockState != null) {
                        try {
                            data = Integer.parseInt(blockState);
                        } catch (NumberFormatException ignored) {
                            // blockState don't have to be number
                            if (blockState.startsWith("[") && blockState.endsWith("]")) {
                                var map = blockTypeMapper.getDataFromString(blockState.toLowerCase(Locale.ROOT));
                                if (map.containsKey("legacy_data") && map.size() == 1) {
                                    try {
                                        data = Integer.parseInt(map.get("legacy_data"));
                                    } catch (NumberFormatException ignored2) {
                                    }
                                } else {
                                    flatteningData = map;
                                }
                            }
                        }
                    }

                    if (flatteningData != null && blockTypeMapper.isLegacy()) {
                        final var finalFlatteningData = flatteningData;
                        var newData = blockTypeMapper.blockDataTranslators.entrySet()
                                .stream()
                                .filter(predicatePairEntry -> predicatePairEntry.getKey().test(namespaced))
                                .findFirst()
                                .map(predicatePairEntry -> predicatePairEntry.getValue().second().apply(finalFlatteningData))
                                .orElse(null);

                        if (newData != null) {
                            flatteningData = null;
                            data = newData.intValue();
                        }
                        // TODO: attempt to convert it using BlockTypeHolder#withFlatteningData if there's no converter defined in ItemBlockIdsRemapper
                    } else if (data != null && !blockTypeMapper.isLegacy()) {
                        final var finalData = data;
                        var newData = blockTypeMapper.blockDataTranslators.entrySet()
                                .stream()
                                .filter(predicatePairEntry -> predicatePairEntry.getKey().test(namespaced))
                                .findFirst()
                                .map(predicatePairEntry -> predicatePairEntry.getValue().first().apply(finalData.byteValue()))
                                .orElse(null);

                        if (newData != null) {
                            data = null;
                            flatteningData = newData;
                        }
                    }

                    if (data != null) {
                        var namespacedDurability = ComplexMappingKey.of(namespaced, NumericMappingKey.of(data));

                        if (blockTypeMapper.mapping.containsKey(namespacedDurability)) {
                            return Optional.of(blockTypeMapper.mapping.get(namespacedDurability));
                        } else if (blockTypeMapper.mapping.containsKey(namespaced)) {
                            var holder = blockTypeMapper.mapping.get(namespaced);
                            return Optional.of(holder.withLegacyData(data.byteValue()));
                        }
                    } else if (flatteningData != null) {
                        var namespacedFlattening = ComplexMappingKey.of(namespaced, StringMapMappingKey.of(flatteningData));

                        if (blockTypeMapper.mapping.containsKey(namespacedFlattening)) {
                            return Optional.of(blockTypeMapper.mapping.get(namespacedFlattening));
                        } else if (blockTypeMapper.mapping.containsKey(namespaced)) {
                            var holder = blockTypeMapper.mapping.get(namespaced);
                            return Optional.of(holder.withFlatteningData(flatteningData));
                        }
                    } else if (blockTypeMapper.mapping.containsKey(namespaced)) {
                        return Optional.of(blockTypeMapper.mapping.get(namespaced));
                    }
                } else if (matcher.group("id") != null) {
                    try {
                        var id = Integer.parseInt(matcher.group("id"));
                        int data = 0;
                        try {
                            data = Integer.parseInt(matcher.group("data"));
                        } catch (NumberFormatException ignored) {
                        }

                        var keyWithData = ComplexMappingKey.of(NumericMappingKey.of(id), NumericMappingKey.of(data));
                        var key = NumericMappingKey.of(id);

                        if (blockTypeMapper.mapping.containsKey(keyWithData)) {
                            return Optional.of(blockTypeMapper.mapping.get(keyWithData));
                        } else if (blockTypeMapper.mapping.containsKey(key)) {
                            var holder = blockTypeMapper.mapping.get(key);
                            return Optional.of(holder.withLegacyData((byte) data));
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            return Optional.empty();
        }).orElse(null);
    }

    public static @NotNull List<@NotNull BlockType> getValues() {
        if (blockTypeMapper == null) {
            throw new UnsupportedOperationException("BlockTypeMapper is not initialized yet.");
        }
        return Collections.unmodifiableList(blockTypeMapper.values);
    }

    public static @NotNull BlockType colorize(@NotNull BlockType holder, @NotNull String color) {
        if (blockTypeMapper == null) {
            throw new UnsupportedOperationException("BlockTypeMapper is not initialized yet.");
        }
        return ItemBlockIdsRemapper.colorableBlocks.entrySet().stream()
                .filter(c -> c.getKey().test(holder))
                .map(Map.Entry::getValue)
                .findFirst()
                .flatMap(fun -> fun.apply(color))
                .orElse(holder);
    }

    public static @NotNull BlockType getCachedAir() {
        if (cachedAir == null) {
            cachedAir = resolve("minecraft:air");
            Preconditions.checkNotNull(cachedAir, "Could not find block type: minecraft:air");
        }
        return cachedAir;
    }

    public @NotNull Map<@NotNull MappingKey, BlockType> getUNSAFE_mapping() {
        return mapping;
    }

    @Override
    public void mapAlias(@NotNull String mappingKey, @NotNull String alias) {
        super.mapAlias(mappingKey, alias);
    }

    protected abstract @NotNull Map<@NotNull String, String> getDataFromString(@NotNull String data);

    protected abstract boolean isLegacy();
}
