package org.screamingsandals.lib.utils;

import org.spongepowered.configurate.ConfigurationNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfigurateUtils {
    public static Map<String,?> toMap(ConfigurationNode node) {
        //noinspection unchecked
        return (Map<String,?>) raw(node);
    }

    public static Object raw(ConfigurationNode node) {
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
