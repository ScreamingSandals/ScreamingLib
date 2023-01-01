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

package org.screamingsandals.lib.item;

import org.screamingsandals.lib.utils.Replaceable;

/**
 * Represents an item view, not the item itself.
 *
 * You are able to change the viewed item of this view. If the current platform supports mutability of items, the old item will be updated.
 */
// TODO: find all events that can use ItemView instead of Item (Bukkit sometimes doesn't offer setStack method but the ItemStack is mutable and the vanilla one is changed)
public interface ItemView extends Item, Replaceable<Item> {
    void changeAmount(int amount);
}
