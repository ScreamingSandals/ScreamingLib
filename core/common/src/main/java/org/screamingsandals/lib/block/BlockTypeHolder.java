package org.screamingsandals.lib.block;

import lombok.*;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.particle.ParticleData;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.*;

@Accessors(fluent = true)
@Data
@RequiredArgsConstructor
public class BlockTypeHolder implements ComparableWrapper, ParticleData {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static BlockTypeHolder cachedAir;

    private final String platformName;
    @With(onMethod_=@Deprecated)
    @Getter(onMethod_=@Deprecated)
    private final byte legacyData;
    @With
    @Unmodifiable
    private final Map<String, String> flatteningData;

    public BlockTypeHolder(String platformName) {
        this(platformName, (byte) 0);
    }

    public BlockTypeHolder(String platformName, @Deprecated byte legacyData) {
        this(platformName, legacyData, null);
    }

    public BlockTypeHolder(String platformName, @Unmodifiable Map<String, String> flatteningData) {
        this(platformName, (byte) 0, flatteningData);
    }

    public BlockTypeHolder with(String attribute, String value) {
        return new BlockTypeHolder(platformName, legacyData, Map.copyOf(new HashMap<>() {
            {
                if (flatteningData != null) {
                    putAll(flatteningData);
                }
                put(attribute, value);
            }
        }));
    }

    public BlockTypeHolder with(String attribute, int value) {
        return new BlockTypeHolder(platformName, legacyData, Map.copyOf(new HashMap<>() {
            {
                if (flatteningData != null) {
                    putAll(flatteningData);
                }
                put(attribute, String.valueOf(value));
            }
        }));
    }

    public BlockTypeHolder with(String attribute, boolean value) {
        return new BlockTypeHolder(platformName, legacyData, Map.copyOf(new HashMap<>() {
            {
                if (flatteningData != null) {
                    putAll(flatteningData);
                }
                put(attribute, String.valueOf(value));
            }
        }));
    }

    public BlockTypeHolder colorize(String color) {
        return BlockTypeMapper.colorize(this, color);
    }

    @SuppressWarnings("unchecked")
    public Optional<String> get(String attribute) {
        return flatteningData != null ? Optional.ofNullable(flatteningData.get(attribute)) : Optional.empty();
    }

    public Optional<Integer> getInt(String attribute) {
        if (flatteningData != null) {
            var attr = flatteningData.get(attribute);
            if (attr != null) {
                try {
                    return Optional.of(Integer.parseInt(attr));
                } catch (NumberFormatException ignored) { }
            }
        }
        return Optional.empty();
    }

    public Optional<Boolean> getBoolean(String attribute) {
        if (flatteningData != null) {
            var attr = flatteningData.get(attribute);
            if (attr != null) {
                return Optional.of(Boolean.parseBoolean(attr));
            }
        }
        return Optional.empty();
    }

    @Override
    public <T> T as(Class<T> type) {
        return BlockTypeMapper.convertBlockTypeHolder(this, type);
    }

    public boolean isAir() {
        return isSameType(air(), "minecraft:cave_air", "minecraft:void_air");
    }

    @CustomAutocompletion(CustomAutocompletion.Type.BLOCK)
    public boolean isSameType(Object object) {
        return ofOptional(object).map(h -> h.platformName.equals(platformName)).orElse(false);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.BLOCK)
    public boolean isSameType(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::isSameType);
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

    public static List<BlockTypeHolder> all() {
        return BlockTypeMapper.getValues();
    }
}
