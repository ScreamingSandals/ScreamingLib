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

package org.screamingsandals.lib.minestom.item.meta;

import net.minestom.server.potion.PotionType;
import org.screamingsandals.lib.item.meta.PotionMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

@Service
public class MinestomPotionMapping extends PotionMapping {
    public MinestomPotionMapping() {
        potionConverter
                .registerW2P(PotionType.class, e -> PotionType.fromNamespaceId(e.platformName()))
                .registerP2W(PotionType.class, MinestomPotionHolder::new);

        PotionType.values().forEach(potion -> {
            final var holder = new MinestomPotionHolder(potion);
            mapping.put(NamespacedMappingKey.of(potion.name()), holder);
            values.add(holder);
        });
    }
}
