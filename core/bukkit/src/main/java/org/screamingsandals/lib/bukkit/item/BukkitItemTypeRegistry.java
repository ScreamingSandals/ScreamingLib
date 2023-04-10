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

package org.screamingsandals.lib.bukkit.item;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.bukkit.utils.Version;
import org.screamingsandals.lib.item.ItemTypeRegistry;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.ServiceInitializer;

@Service
public abstract class BukkitItemTypeRegistry extends ItemTypeRegistry {
    @ServiceInitializer
    public static @NotNull BukkitItemTypeRegistry init() {
        if (Version.isVersion(1, 14)) {
            return new BukkitItemTypeRegistry1_14();
        } else if (Version.isVersion(1, 13)) {
            return new BukkitItemTypeRegistry1_13();
        } else {
            return new BukkitItemTypeRegistry1_8();
        }
    }
}
