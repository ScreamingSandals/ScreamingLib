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

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.bukkit.utils.Version;
import org.screamingsandals.lib.item.meta.PotionRegistry;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.ServiceInitializer;

@Service
public abstract class BukkitPotionRegistry extends PotionRegistry {
    public static final boolean IS_POTION_SUPPORTED = Version.isVersion(1, 9);


    @ServiceInitializer
    public static @NotNull BukkitPotionRegistry init() {
        if (IS_POTION_SUPPORTED) {
            return new BukkitPotionRegistry1_9();
        } else {
            return new BukkitPotionRegistry1_8();
        }
    }
}
