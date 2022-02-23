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

package org.screamingsandals.lib.minestom.item;

import net.minestom.server.item.ItemStack;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemView;

public class MinestomItemView extends MinestomItem implements ItemView {
    public MinestomItemView(ItemStack wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public void changeAmount(int amount) {
        // empty stub
    }

    @Override
    public void replace(Item replaceable) {
        // empty stub
    }
}
