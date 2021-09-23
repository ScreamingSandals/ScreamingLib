package org.screamingsandals.lib.item;

import org.screamingsandals.lib.ItemBlockIdsRemapper;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.key.ComplexMappingKey;
import org.screamingsandals.lib.utils.key.MappingKey;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.utils.key.NumericMappingKey;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@AbstractService
public abstract class ItemTypeMapper extends AbstractTypeMapper<ItemTypeHolder> {

    private static final Pattern RESOLUTION_PATTERN = Pattern.compile("^(((?<namespaced>(?:([A-Za-z][A-Za-z0-9_.\\-]*):)?[A-Za-z][A-Za-z0-9_.\\-/ ]*)(?::)?(?<durability>\\d+)?)|((?<id>\\d+)(?::)?(?<data>\\d+)?))$");
    protected BidirectionalConverter<ItemTypeHolder> itemTypeConverter = BidirectionalConverter.<ItemTypeHolder>build()
            .registerP2W(ItemTypeHolder.class, i -> i);

    private static ItemTypeMapper itemTypeMapper;

    protected ItemTypeMapper() {
        if (itemTypeMapper != null) {
            throw new UnsupportedOperationException("ItemTypeMapper is already initialized.");
        }

        itemTypeMapper = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    @OfMethodAlternative(value = ItemTypeHolder.class, methodName = "ofOptional")
    public static Optional<ItemTypeHolder> resolve(Object materialObject) {
        if (itemTypeMapper == null) {
            throw new UnsupportedOperationException("ItemTypeMapper is not initialized yet.");
        }
        if (materialObject == null) {
            return Optional.empty();
        }

        return itemTypeMapper.itemTypeConverter.convertOptional(materialObject).or(() -> {
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

                        if (itemTypeMapper.mapping.containsKey(namespacedDurability)) {
                            return Optional.of(itemTypeMapper.mapping.get(namespacedDurability));
                        } else if (itemTypeMapper.mapping.containsKey(namespaced)) {
                            var holder = itemTypeMapper.mapping.get(namespaced);
                            return Optional.of(holder.withDurability(data.shortValue()));
                        }
                    } else if (itemTypeMapper.mapping.containsKey(namespaced)) {
                        return Optional.of(itemTypeMapper.mapping.get(namespaced));
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

                        if (itemTypeMapper.mapping.containsKey(keyWithData)) {
                            return Optional.of(itemTypeMapper.mapping.get(keyWithData));
                        } else if (itemTypeMapper.mapping.containsKey(key)) {
                            var holder = itemTypeMapper.mapping.get(key);
                            return Optional.of(holder.withDurability((short) data));
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            return Optional.empty();
        });
    }

    @OfMethodAlternative(value = ItemTypeHolder.class, methodName = "all")
    public static List<ItemTypeHolder> getValues() {
        if (itemTypeMapper == null) {
            throw new UnsupportedOperationException("ItemTypeMapper is not initialized yet.");
        }
        return Collections.unmodifiableList(itemTypeMapper.values);
    }

    public static ItemTypeHolder colorize(ItemTypeHolder holder, String color) {
        if (itemTypeMapper == null) {
            throw new UnsupportedOperationException("ItemTypeMapper is not initialized yet.");
        }
        return ItemBlockIdsRemapper.colorableItems.entrySet().stream()
                .filter(c -> c.getKey().test(holder))
                .map(Map.Entry::getValue)
                .findFirst()
                .flatMap(fun -> fun.apply(color))
                .orElse(holder);
    }

    public static <T> T convertItemTypeHolder(ItemTypeHolder holder, Class<T> newType) {
        if (itemTypeMapper == null) {
            throw new UnsupportedOperationException("ItemTypeMapper is not initialized yet.");
        }
        return itemTypeMapper.itemTypeConverter.convert(holder, newType);
    }

    public Map<MappingKey, ItemTypeHolder> getUNSAFE_mapping() {
        return mapping;
    }

    @Override
    public void mapAlias(String mappingKey, String alias) {
        super.mapAlias(mappingKey, alias);
    }

    public static Optional<BlockTypeHolder> getBlock(ItemTypeHolder typeHolder) {
        if (itemTypeMapper == null) {
            throw new UnsupportedOperationException("ItemTypeMapper is not initialized yet.");
        }
        return itemTypeMapper.getBlock0(typeHolder);
    }

    protected abstract Optional<BlockTypeHolder> getBlock0(ItemTypeHolder typeHolder);

    public static int getMaxStackSize(ItemTypeHolder materialHolder) {
        if (itemTypeMapper == null) {
            throw new UnsupportedOperationException("ItemTypeMapper is not initialized yet.");
        }
        return itemTypeMapper.getMaxStackSize0(materialHolder);
    }

    protected abstract int getMaxStackSize0(ItemTypeHolder materialHolder);
}
