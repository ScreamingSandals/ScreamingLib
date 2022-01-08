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

package org.screamingsandals.lib.spectator;

import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

@LimitedVersionSupport(">= 1.15")
public interface StorageNBTComponent extends NBTComponent {
    static StorageNBTComponent.Builder builder() {
        return Spectator.getBackend().storageNBT();
    }

    NamespacedMappingKey storageKey();

    interface Builder extends NBTComponent.Builder<Builder, StorageNBTComponent> {
        Builder storageKey(NamespacedMappingKey storageKey);
    }
}
