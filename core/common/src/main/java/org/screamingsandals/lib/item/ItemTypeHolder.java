package org.screamingsandals.lib.item;

import lombok.*;
import lombok.experimental.Accessors;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.Arrays;
import java.util.Optional;

@Accessors(fluent = true)
@Data
@RequiredArgsConstructor
public class ItemTypeHolder implements ComparableWrapper {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static ItemTypeHolder cachedAir;

    private final String platformName;
    @With
    private final short durability;

    public ItemTypeHolder(String platformName) {
        this(platformName, (short) 0);
    }

    @Override
    public <T> T as(Class<T> type) {
        return ItemTypeMapper.convertItemTypeHolder(this, type);
    }

    public boolean isAir() {
        return equals(air());
    }

    public int getMaxStackSize() {
        return ItemTypeMapper.getMaxStackSize(this);
    }

    public ItemTypeHolder colorize(String color) {
        return ItemTypeMapper.colorize(this, color);
    }

    public Optional<BlockTypeHolder> block() {
        return ItemTypeMapper.getBlock(this);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    @Override
    public boolean is(Object object) {
        return equals(ofOptional(object).orElse(null));
    }

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    public static ItemTypeHolder of(Object type) {
        return ofOptional(type).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    @SuppressWarnings("AlternativeMethodAvailable")
    public static Optional<ItemTypeHolder> ofOptional(Object type) {
        if (type instanceof ItemTypeHolder) {
            return Optional.of((ItemTypeHolder) type);
        }
        return ItemTypeMapper.resolve(type);
    }

    public static ItemTypeHolder air() {
        if (cachedAir == null) {
            cachedAir = of("minecraft:air");
        }
        return cachedAir;
    }
}
