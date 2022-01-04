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

package org.screamingsandals.lib.event.player;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.utils.*;

public interface SPlayerCraftItemEvent extends SPlayerInventoryClickEvent {

    // Who tf called this method getClickedInventoryContainer()?
    @Nullable
    @Deprecated
    default Container getClickedInventoryContainer() {
        return getClickedInventory();
    }

    // That's the same thing like getInventory()
    @Deprecated
    default Container getCraftInventory() {
        return getInventory();
    }

    Recipe getRecipe();

    // TODO: we should create proper Recipe API
    interface Recipe extends Wrapper, RawValueHolder {

        Item getResult();
    }
}
