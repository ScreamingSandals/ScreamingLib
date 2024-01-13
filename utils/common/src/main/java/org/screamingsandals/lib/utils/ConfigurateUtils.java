/*
 * Copyright 2024 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
            node.childrenList().forEach(configurationNode ->
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
