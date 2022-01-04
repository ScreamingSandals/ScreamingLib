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

package org.screamingsandals.lib.container;

import org.screamingsandals.lib.player.PlayerWrapper;

/**
 * An interface representing an openable inventory.
 */
public interface Openable {
    /**
     * Opens this inventory to the supplied player.
     *
     * @param wrapper the player
     */
    void openInventory(PlayerWrapper wrapper);
}
