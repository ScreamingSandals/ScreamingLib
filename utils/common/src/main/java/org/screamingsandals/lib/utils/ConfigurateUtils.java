package org.screamingsandals.lib.utils;

import lombok.experimental.UtilityClass;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class ConfigurateUtils {

    @SuppressWarnings("unchecked")
    public Map<String,?> toMap(ConfigurationNode node) {
        return (Map<String,?>) raw(node);
    }

    public Object raw(ConfigurationNode node) {
        if (node.isList()) {
            var list = new ArrayList<>();
            node.childrenMap().forEach((key, configurationNode) ->
                    list.add(raw(configurationNode))
            );
            return list;
        } else if (node.isMap()) {
            var map = new HashMap<String,Object>();
            node.childrenMap().forEach((key, configurationNode) ->
                map.put(key.toString(), raw(configurationNode))
            );
            return map;
        } else {
            return node.raw();
        }
    }
}
