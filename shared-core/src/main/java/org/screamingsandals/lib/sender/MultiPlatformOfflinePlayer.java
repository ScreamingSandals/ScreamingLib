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

package org.screamingsandals.lib.sender;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.UUID;

public interface MultiPlatformOfflinePlayer extends Operator, Wrapper {
    @NotNull UUID getUuid();

    /**
     * Gets the player's last name.
     *
     * @return the player's last name
     */
    @Nullable String getLastName();

    /**
     * Gets the first played time.
     *
     * @return the first played time
     */
    long getFirstPlayed();

    /**
     * Gets the last played time.
     *
     * @return the last played time
     */
    long getLastPlayed();

    default boolean hasPlayed() {
        return getFirstPlayed() > 0;
    }

    /**
     * Checks if this player is banned.
     *
     * @return is the player banned?
     */
    boolean isBanned();

    /**
     * Checks if this player is whitelisted.
     *
     * @return is the player whitelisted?
     */
    boolean isWhitelisted();

    /**
     * Checks if this player is online.
     *
     * @return is the player online?
     */
    boolean isOnline();

    /**
     * Sets the whitelisted status for this player.
     *
     * @param whitelisted the new whitelisted status
     */
    void setWhitelisted(boolean whitelisted);
}
