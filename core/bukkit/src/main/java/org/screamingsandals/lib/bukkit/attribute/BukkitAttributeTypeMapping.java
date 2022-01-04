/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.bukkit.attribute;

import org.bukkit.attribute.Attribute;
import org.screamingsandals.lib.attribute.AttributeTypeMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.AttributeMappingKey;

import java.util.Arrays;

@Service
public class BukkitAttributeTypeMapping extends AttributeTypeMapping {

    public BukkitAttributeTypeMapping() {
        attributeTypeConverter
                .registerP2W(Attribute.class, BukkitAttributeTypeHolder::new);

        Arrays.stream(Attribute.values()).forEach(attr -> {
            var holder = new BukkitAttributeTypeHolder(attr);
            mapping.put(AttributeMappingKey.of(attr.name()), holder);
            values.add(holder);
        });
    }
}
