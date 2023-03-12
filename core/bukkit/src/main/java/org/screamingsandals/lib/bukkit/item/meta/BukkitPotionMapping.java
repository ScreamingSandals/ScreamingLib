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

package org.screamingsandals.lib.bukkit.item.meta;

import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.screamingsandals.lib.item.meta.PotionMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.ResourceLocation;

import java.util.Arrays;

@Service
public class BukkitPotionMapping extends PotionMapping {

    public BukkitPotionMapping() {
        potionConverter
                .registerP2W(PotionType.class, BukkitPotionHolder::new)
                .registerP2W(PotionData.class, BukkitPotionHolder::new);

        Arrays.stream(PotionType.values()).forEach(potion -> {
            var holder = new BukkitPotionHolder(potion);
            mapping.put(ResourceLocation.of(potion.name()), holder);
            values.add(holder);
            if (potion.isExtendable()) {
                var holder2 = new BukkitPotionHolder(new PotionData(potion, true, false));
                mapping.put(ResourceLocation.of("long_" + potion.name()), holder2);
                values.add(holder2);
            }
            if (potion.isUpgradeable()) {
                var holder3 = new BukkitPotionHolder(new PotionData(potion, false, true));
                mapping.put(ResourceLocation.of("strong_" + potion.name()), holder3);
                values.add(holder3);
            }
        });
    }
}
