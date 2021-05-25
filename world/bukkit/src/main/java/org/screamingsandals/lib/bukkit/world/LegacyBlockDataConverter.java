package org.screamingsandals.lib.bukkit.world;

import lombok.experimental.UtilityClass;
import org.bukkit.material.MaterialData;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class LegacyBlockDataConverter {
    public Map<String, Object> convertMaterialData(MaterialData data) {
        var map = new HashMap<String, Object>();
        // TODO: Write convertion from legacy data to flattening
        return map;
    }
}
