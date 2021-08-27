package org.screamingsandals.lib.block;

import org.screamingsandals.lib.ItemBlockIdsRemapper;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.key.ComplexMappingKey;
import org.screamingsandals.lib.utils.key.MappingKey;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.utils.key.NumericMappingKey;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@AbstractService
public abstract class BlockTypeMapper extends AbstractTypeMapper<BlockTypeHolder> {

    private static final Pattern RESOLUTION_PATTERN = Pattern.compile("^(((?<namespaced>(?:([A-Za-z][A-Za-z0-9_.\\-]*):)?[A-Za-z][A-Za-z0-9_.\\-/ ]*)(?::)?(?<durability>\\d+)?)|((?<id>\\d+)(?::)?(?<data>\\d+)?))$");
    protected BidirectionalConverter<BlockTypeHolder> blockTypeConverter = BidirectionalConverter.<BlockTypeHolder>build()
            .registerP2W(BlockTypeHolder.class, i -> i);

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
                    try {
                        data = Integer.parseInt(matcher.group("durability"));
                    } catch (NumberFormatException ignored) {
                    }

                    if (data != null) {
                        var namespacedDurability = ComplexMappingKey.of(namespaced, NumericMappingKey.of(data));

                        if (blockTypeMapper.mapping.containsKey(namespacedDurability)) {
                            return Optional.of(blockTypeMapper.mapping.get(namespacedDurability));
                        } else if (blockTypeMapper.mapping.containsKey(namespaced)) {
                            var holder = blockTypeMapper.mapping.get(namespaced);
                            return Optional.of(holder.withLegacyData(data.byteValue()));
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
}
