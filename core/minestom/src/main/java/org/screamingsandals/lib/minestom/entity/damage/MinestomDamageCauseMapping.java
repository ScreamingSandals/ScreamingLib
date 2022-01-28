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

package org.screamingsandals.lib.minestom.entity.damage;

import net.minestom.server.entity.damage.DamageType;
import org.screamingsandals.lib.entity.damage.DamageCauseMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.stream.Stream;

@Service
public class MinestomDamageCauseMapping extends DamageCauseMapping {
    public MinestomDamageCauseMapping() {
        damageCauseConverter
                .registerP2W(DamageType.class, MinestomDamageCauseHolder::new)
                .registerW2P(DamageType.class, damageCauseHolder -> new DamageType(damageCauseHolder.platformName()));

        Stream.of(
                DamageType.GRAVITY,
                DamageType.ON_FIRE,
                DamageType.VOID
        ).forEach(damageCause -> {
            var holder = new MinestomDamageCauseHolder(damageCause);
            mapping.put(NamespacedMappingKey.of(damageCause.getIdentifier()), holder);
            values.add(holder);
        });
        Stream.of(
                new MinestomSpecialDamageCauseHolder("entity_source"), // EntityDamage class
                new MinestomSpecialDamageCauseHolder("projectile_source") // EntityProjectileDamage class
        ).forEach(holder -> {
            mapping.put(NamespacedMappingKey.of(holder.platformName()), holder);
            values.add(holder);
        });
    }
}