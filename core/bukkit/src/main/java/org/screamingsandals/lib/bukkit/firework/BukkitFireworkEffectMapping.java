/*
 * Copyright 2023 ScreamingSandals
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

package org.screamingsandals.lib.bukkit.firework;

import org.bukkit.FireworkEffect;
import org.screamingsandals.lib.firework.FireworkEffectMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitFireworkEffectMapping extends FireworkEffectMapping {
    public BukkitFireworkEffectMapping() {
        fireworkEffectConverter
                .registerP2W(FireworkEffect.Type.class, BukkitFireworkEffectHolder::new)
                .registerP2W(FireworkEffect.class, BukkitFireworkEffectHolder::new);

        Arrays.stream(FireworkEffect.Type.values()).forEach(fireworkEffectType -> {
            var holder = new BukkitFireworkEffectHolder(fireworkEffectType);
            mapping.put(NamespacedMappingKey.of(fireworkEffectType.name()), holder);
            values.add(holder);
        });
    }
}
