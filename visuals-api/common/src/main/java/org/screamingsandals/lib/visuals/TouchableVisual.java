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

package org.screamingsandals.lib.visuals;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface TouchableVisual<T> extends LocatableVisual<T> {

    /**
     * This is the default click cool down in milliseconds
     */
    long DEFAULT_CLICK_COOL_DOWN = 20L;

    /**
     * Checks if you can interact with this Visual.
     *
     * @return true if this Visual is touchable
     */
    boolean touchable();

    /**
     * Changes interact state for this Visual.
     *
     * @param touchable touchable state
     * @return this Visual
     */
    @Contract("_ -> this")
    @NotNull T touchable(boolean touchable);

    /**
     * Returns if the entity id provided belongs to this Visual. Used for detecting Player interaction.
     *
     * @param entityId the id of the entity
     * @return true if the visual contains the entity id, false otherwise
     */
    boolean hasId(int entityId);

    /**
     *
     * @param delay the amount of time (in milliseconds) the last clicked user has to wait before interacting with this Visual again
     * @return this Visual
     */
    @Contract("_ -> this")
    @NotNull T clickCooldown(long delay);

    /**
     *
     * @return the amount of time (in milliseconds) the last clicked user has to wait before interacting with this Visual again
     */
    long clickCooldown();
}
