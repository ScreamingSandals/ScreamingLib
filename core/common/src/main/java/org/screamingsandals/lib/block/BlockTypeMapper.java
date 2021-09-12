package org.screamingsandals.lib.block;

import lombok.Getter;
import org.screamingsandals.lib.ItemBlockIdsRemapper;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.key.*;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@AbstractService
public abstract class BlockTypeMapper extends AbstractTypeMapper<BlockTypeHolder> {

    private static final Pattern RESOLUTION_PATTERN = Pattern.compile("^(((?<namespaced>(?:([A-Za-z][A-Za-z0-9_.\\-]*):)?[A-Za-z][A-Za-z0-9_.\\-/ ]*)(?<blockState>:\\d*|\\[[^]]*])?)|((?<id>\\d+)(?::)?(?<data>\\d+)?))$");
    protected BidirectionalConverter<BlockTypeHolder> blockTypeConverter = BidirectionalConverter.<BlockTypeHolder>build()
            .registerP2W(BlockTypeHolder.class, i -> i);
    @Getter
    protected final Map<Predicate<NamespacedMappingKey>, Pair<Function<Byte, Map<String, String>>, Function<Map<String, String>, Byte>>> blockDataTranslators = new HashMap<>();

    private static BlockTypeMapper blockTypeMapper;

    protected BlockTypeMapper() {
        if (blockTypeMapper != null) {
            throw new UnsupportedOperationException("BlockTypeMapper is already initialized.");
        }

        blockTypeMapper = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.BLOCK)
    @OfMethodAlternative(value = BlockTypeHolder.class, methodName = "ofOptional")
    public static Optional<BlockTypeHolder> resolve(Object materialObject) {
        if (blockTypeMapper == null) {
            throw new UnsupportedOperationException("BlockTypeMapper is not initialized yet.");
        }
        if (materialObject == null) {
            return Optional.empty();
        }

        return blockTypeMapper.blockTypeConverter.convertOptional(materialObject).or(() -> {
            var material = materialObject.toString().trim();

            var matcher = RESOLUTION_PATTERN.matcher(material);

            if (matcher.matches()) {
                if (matcher.group("namespaced") != null) {

                    var namespaced = NamespacedMappingKey.of(matcher.group("namespaced"));

                    Integer data = null;
                    Map<String, String> flatteningData = null;
                    var blockState = matcher.group("blockState");
                    if (blockState != null) {
                        try {
                            data = Integer.parseInt(blockState);
                        } catch (NumberFormatException ignored) {
                            // blockState don't have to be number
                            if (blockState.startsWith("[") && blockState.startsWith("]")) {
                                var map = blockTypeMapper.getDataFromString(blockState.toLowerCase());
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
                            return Optional.of(blockTypeMapper.normalize(blockTypeMapper.mapping.get(namespacedDurability)));
                        } else if (blockTypeMapper.mapping.containsKey(namespaced)) {
                            var holder = blockTypeMapper.mapping.get(namespaced);
                            return Optional.of(blockTypeMapper.normalize(holder.withLegacyData(data.byteValue())));
                        }
                    } else if (flatteningData != null) {
                        var namespacedFlattening = ComplexMappingKey.of(namespaced, StringMapMappingKey.of(flatteningData));

                        if (blockTypeMapper.mapping.containsKey(namespacedFlattening)) {
                            return Optional.of(blockTypeMapper.normalize(blockTypeMapper.mapping.get(namespacedFlattening)));
                        } else if (blockTypeMapper.mapping.containsKey(namespaced)) {
                            var holder = blockTypeMapper.mapping.get(namespaced);
                            return Optional.of(blockTypeMapper.normalize(holder.withFlatteningData(flatteningData)));
                        }
                    } else if (blockTypeMapper.mapping.containsKey(namespaced)) {
                        return Optional.of(blockTypeMapper.normalize(blockTypeMapper.mapping.get(namespaced)));
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
                            return Optional.of(blockTypeMapper.normalize(blockTypeMapper.mapping.get(keyWithData)));
                        } else if (blockTypeMapper.mapping.containsKey(key)) {
                            var holder = blockTypeMapper.mapping.get(key);
                            return Optional.of(blockTypeMapper.normalize(holder.withLegacyData((byte) data)));
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            return Optional.empty();
        });
    }

    public static BlockTypeHolder colorize(BlockTypeHolder holder, String color) {
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

    public static <T> T convertBlockTypeHolder(BlockTypeHolder holder, Class<T> newType) {
        if (blockTypeMapper == null) {
            throw new UnsupportedOperationException("BlockTypeMapper is not initialized yet.");
        }
        return blockTypeMapper.blockTypeConverter.convert(holder, newType);
    }

    public Map<MappingKey, BlockTypeHolder> getUNSAFE_mapping() {
        return mapping;
    }

    @Override
    public void mapAlias(String mappingKey, String alias) {
        super.mapAlias(mappingKey, alias);
    }

    protected abstract String getDataFromMap(BlockTypeHolder material);

    protected abstract Map<String, String> getDataFromString(String data);

    protected abstract BlockTypeHolder normalize(BlockTypeHolder abnormal);

    public abstract String getStateDataFromMap(Map<String, String> map);

    protected abstract boolean isLegacy();
}
