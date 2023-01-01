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

package org.screamingsandals.lib.bukkit.entity.damage;

import org.bukkit.event.entity.EntityDamageEvent;
import org.screamingsandals.lib.entity.damage.DamageCauseMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitDamageCauseMapping extends DamageCauseMapping {
    public BukkitDamageCauseMapping() {
        damageCauseConverter
                .registerP2W(EntityDamageEvent.DamageCause.class, BukkitDamageCauseHolder::new);

        Arrays.stream(EntityDamageEvent.DamageCause.values()).forEach(damageCause -> {
            var holder = new BukkitDamageCauseHolder(damageCause);
            mapping.put(NamespacedMappingKey.of(damageCause.name()), holder);
            values.add(holder);
        });
    }
}
