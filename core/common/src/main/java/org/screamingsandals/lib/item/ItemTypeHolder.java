package org.screamingsandals.lib.item;

import lombok.*;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.particle.ParticleData;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.ProtoItemType;
import org.screamingsandals.lib.utils.ProtoWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
@Accessors(fluent = true)
public interface ItemTypeHolder extends ComparableWrapper, ProtoWrapper<ProtoItemType>, ParticleData {
    String platformName();

    /**
     * Use renamed form
     */
    @Deprecated(forRemoval = true)
    default short durability() {
        return forcedDurability();
    }

    @Deprecated
    @LimitedVersionSupport("<= 1.12.2")
    short forcedDurability();

    default boolean isAir() {
        return equals(air());
    }

    int maxStackSize();

    /**
     * Use renamed form
     */
    @Deprecated(forRemoval = true)
    default ItemTypeHolder withDurability(short durability) {
        return withForcedDurability(durability);
    }

    @Deprecated
    @LimitedVersionSupport("<= 1.12.2")
    @Contract(value = "_ -> new", pure = true)
    ItemTypeHolder withForcedDurability(short durability);

    /**
     * Use fluent variant!!
     */
    @Deprecated(forRemoval = true)
    default int getMaxStackSize() {
        return maxStackSize();
    }

    default ItemTypeHolder colorize(String color) {
        return ItemTypeMapper.colorize(this, color);
    }

    Optional<BlockTypeHolder> block();

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    @Override
    boolean is(Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    @Override
    boolean is(Object... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    static ItemTypeHolder of(Object type) {
        return ofOptional(type).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    static Optional<ItemTypeHolder> ofOptional(Object type) {
        if (type instanceof ItemTypeHolder) {
            return Optional.of((ItemTypeHolder) type);
        }
        return ItemTypeMapper.resolve(type);
    }

    static ItemTypeHolder air() {
        return ItemTypeMapper.getCachedAir();
    }

    static List<ItemTypeHolder> all() {
        return ItemTypeMapper.getValues();
    }

    @Override
    default ProtoItemType asProto() {
        return ProtoItemType.newBuilder()
                .setPlatformName(platformName())
                .setDurability(durability())
                .build();
    }
}
