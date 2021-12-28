package org.screamingsandals.lib.block;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.particle.ParticleData;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.*;

/**
 * Class representing a <strong>block</strong> material.
 *
 * Use {@link org.screamingsandals.lib.item.ItemTypeHolder} for item materials.
 */
@SuppressWarnings("AlternativeMethodAvailable")
public interface BlockTypeHolder extends ComparableWrapper, ParticleData {

    String platformName();

    @Deprecated
    byte legacyData();

    @Deprecated
    @Contract(value = "_ -> new", pure = true)
    BlockTypeHolder withLegacyData(byte legacyData);

    /**
     * NOTE: This method does not work in legacy environments yet!
     */
    @Unmodifiable
    Map<String, String> flatteningData();

    /**
     * NOTE: This method does not work in legacy environments yet!
     */
    @Contract(value = "_ -> new", pure = true)
    BlockTypeHolder withFlatteningData(Map<String, String> flatteningData);

    /**
     * NOTE: This method does not work in legacy environments yet!
     */
    @Contract(value = "_, _ -> new", pure = true)
    BlockTypeHolder with(String attribute, String value);

    /**
     * NOTE: This method does not work in legacy environments yet!
     */
    @Contract(value = "_, _ -> new", pure = true)
    BlockTypeHolder with(String attribute, int value);

    /**
     * NOTE: This method does not work in legacy environments yet!
     */
    @Contract(value = "_, _ -> new", pure = true)
    BlockTypeHolder with(String attribute, boolean value);

    @Contract(value = "_ -> new", pure = true)
    default BlockTypeHolder colorize(String color) {
        return BlockTypeMapper.colorize(this, color);
    }

    /**
     * NOTE: This method does not work in legacy environments yet!
     */
    Optional<String> get(String attribute);

    /**
     * NOTE: This method does not work in legacy environments yet!
     */
    Optional<Integer> getInt(String attribute);

    /**
     * NOTE: This method does not work in legacy environments yet!
     */
    Optional<Boolean> getBoolean(String attribute);

    default boolean isAir() {
        return isSameType(air(), "minecraft:cave_air", "minecraft:void_air");
    }

    boolean isSolid();

    boolean isTransparent();

    boolean isFlammable();

    boolean isBurnable();

    boolean isOccluding();

    boolean hasGravity();

    @CustomAutocompletion(CustomAutocompletion.Type.BLOCK)
    boolean isSameType(Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.BLOCK)
    boolean isSameType(Object... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.BLOCK)
    @Override
    boolean is(Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.BLOCK)
    @Override
    boolean is(Object... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.BLOCK)
    static BlockTypeHolder of(Object type) {
        return ofOptional(type).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.BLOCK)
    static Optional<BlockTypeHolder> ofOptional(Object type) {
        if (type instanceof BlockTypeHolder) {
            return Optional.of((BlockTypeHolder) type);
        }
        return BlockTypeMapper.resolve(type);
    }

    static BlockTypeHolder air() {
        return BlockTypeMapper.getCachedAir();
    }

    static List<BlockTypeHolder> all() {
        return BlockTypeMapper.getValues();
    }
}
