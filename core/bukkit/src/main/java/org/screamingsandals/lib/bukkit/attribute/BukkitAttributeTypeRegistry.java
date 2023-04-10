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

package org.screamingsandals.lib.bukkit.attribute;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.attribute.AttributeTypeRegistry;;
import org.screamingsandals.lib.bukkit.utils.Version;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.ServiceInitializer;

@Service
public abstract class BukkitAttributeTypeRegistry extends AttributeTypeRegistry {
    @ServiceInitializer
    public static @NotNull BukkitAttributeTypeRegistry init() {
        if (Version.isVersion(1, 16)) {
            return new BukkitAttributeTypeRegistry1_16();
        } else if (Version.isVersion(1, 9)) {
            return new BukkitAttributeTypeRegistry1_9();
        } else {
            return new BukkitAttributeTypeRegistry1_8();
        }
    }
}
