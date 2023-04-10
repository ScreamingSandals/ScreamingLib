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

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.Attribute;
import org.screamingsandals.lib.nms.accessors.AttributeInstanceAccessor;

public class BukkitAttributes1_8 extends BukkitAttributes {
    public BukkitAttributes1_8() {
        // TODO: Item attributes
    }

    @Override
    protected @Nullable Attribute wrapAttribute0(@Nullable Object attribute) {
        if (AttributeInstanceAccessor.getType() != null && AttributeInstanceAccessor.getType().isInstance(attribute)) {
            return new BukkitAttribute1_8(attribute);
        }
        return null;
    }
}
