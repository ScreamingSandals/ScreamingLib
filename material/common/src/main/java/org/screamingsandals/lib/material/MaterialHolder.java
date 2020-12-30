package org.screamingsandals.lib.material;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public final class MaterialHolder {
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
        return platformName.equals("AIR");
    }
}
