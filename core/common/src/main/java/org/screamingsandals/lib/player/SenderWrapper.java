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

package org.screamingsandals.lib.player;

import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.Permission;

import java.util.Locale;

/**
 * A sender wrapper.
 */
public interface SenderWrapper extends CommandSenderWrapper {
    /**
     * Attempts to dispatch a command as this sender.
     *
     * @param command the command
     */
    void tryToDispatchCommand(String command);

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean hasPermission(Permission permission) {
        return PlayerMapper.hasPermission(this, permission);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean isPermissionSet(Permission permission) {
        return PlayerMapper.isPermissionSet(this, permission);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default Locale getLocale() {
        return Locale.US;
    }
}