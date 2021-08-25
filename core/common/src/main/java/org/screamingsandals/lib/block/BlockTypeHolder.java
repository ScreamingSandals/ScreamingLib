package org.screamingsandals.lib.block;

import lombok.*;
import lombok.experimental.Accessors;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.Arrays;
import java.util.Optional;

@Accessors(fluent = true)
@Data
@RequiredArgsConstructor
public class BlockTypeHolder implements ComparableWrapper {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static BlockTypeHolder cachedAir;

    private final String platformName;
    @With
    @Deprecated
    private final byte legacyData;

    public BlockTypeHolder(String platformName) {
        this(platformName, (byte) 0);
    }

    @Override
    public <T> T as(Class<T> type) {
        return BlockTypeMapper.convertBlockTypeHolder(this, type);
    }

    public boolean isAir() {
        return is(air(), "minecraft:cave_air", "minecraft:void_air");
    }

    @CustomAutocompletion(CustomAutocompletion.Type.BLOCK)
    @Override
    public boolean is(Object object) {
        return equals(ofOptional(object).orElse(null));
    }

    @CustomAutocompletion(CustomAutocompletion.Type.BLOCK)
    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.BLOCK)
    public static BlockTypeHolder of(Object type) {
        return ofOptional(type).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.BLOCK)
    @SuppressWarnings("AlternativeMethodAvailable")
    public static Optional<BlockTypeHolder> ofOptional(Object type) {
        if (type instanceof BlockTypeHolder) {
            return Optional.of((BlockTypeHolder) type);
        }
        return BlockTypeMapper.resolve(type);
    }

    public static BlockTypeHolder air() {
        if (cachedAir == null) {
            cachedAir = of("minecraft:air");
        }
        return cachedAir;
    }
}
