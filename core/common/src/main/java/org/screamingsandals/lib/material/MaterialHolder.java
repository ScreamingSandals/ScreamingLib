package org.screamingsandals.lib.material;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Arrays;
import java.util.Optional;

@Data
@RequiredArgsConstructor
@ConfigSerializable
public final class MaterialHolder implements Wrapper {
    private final String platformName;
    private final int durability;

    public MaterialHolder(String platformName) {
        this(platformName, 0);
    }

    public MaterialHolder newDurability(int durability) {
        return new MaterialHolder(platformName, durability);
    }

    public <T> T as(Class<T> theClass) {
        return MaterialMapping.convertMaterialHolder(this, theClass);
    }

    public boolean isAir() {
        return is("AIR", "CAVE_AIR", "VOID_AIR");
    }

    /**
     * Compares the material and the object
     *
     * @param material Object that represents material
     * @return true if specified material is the same as this
     */
    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    public boolean is(Object material) {
        return equals(MaterialMapping.resolve(material).orElse(null));
    }

    /**
     * Compares the material and the objects
     *
     * @param materials Array of objects that represents material
     * @return true if at least one of the specified objects is same as this
     */
    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    public boolean is(Object... materials) {
        return Arrays.stream(materials).anyMatch(this::is);
    }

    public boolean isBlock() {
        return MaterialMapping.isBlock(this);
    }

    public boolean isItem() {
        return MaterialMapping.isItem(this);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    public static MaterialHolder of(Object material) {
        return ofOptional(material).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    public static Optional<MaterialHolder> ofOptional(Object material) {
        if (material instanceof MaterialHolder) {
            return Optional.of((MaterialHolder) material);
        }
        return MaterialMapping.resolve(material);
    }
}
