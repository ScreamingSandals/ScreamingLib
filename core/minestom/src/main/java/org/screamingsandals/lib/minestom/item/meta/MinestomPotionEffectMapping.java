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

import net.minestom.server.potion.CustomPotionEffect;
import net.minestom.server.potion.PotionEffect;
import org.screamingsandals.lib.item.meta.PotionEffectMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Objects;

@Service
public class MinestomPotionEffectMapping extends PotionEffectMapping {
    public MinestomPotionEffectMapping() {
        potionEffectConverter
                .registerP2W(PotionEffect.class, potionEffect -> new MinestomPotionEffectHolder(new CustomPotionEffect(
                        (byte) potionEffect.id(),
                        (byte) 0,
                        0,
                        false,
                        false,
                        false
                )))
                .registerP2W(CustomPotionEffect.class, MinestomPotionEffectHolder::new)
                .registerW2P(CustomPotionEffect.class, potionEffectHolder -> new CustomPotionEffect(
                        (byte) Objects.requireNonNull(PotionEffect.fromNamespaceId(potionEffectHolder.platformName()), "Potion effect id " + potionEffectHolder.platformName() + " not found").id(),
                        (byte) potionEffectHolder.amplifier(),
                        potionEffectHolder.duration(),
                        potionEffectHolder.ambient(),
                        potionEffectHolder.particles(),
                        potionEffectHolder.icon()
                ));

        PotionEffect.values().forEach(potionEffect -> {
            final var holder = new MinestomPotionEffectHolder(new CustomPotionEffect(
                    (byte) potionEffect.id(),
                    (byte) 0,
                    0,
                    false,
                    false,
                    false
            ));
            mapping.put(NamespacedMappingKey.of(potionEffect.name()), holder);
            values.add(holder);
        });
    }
}
