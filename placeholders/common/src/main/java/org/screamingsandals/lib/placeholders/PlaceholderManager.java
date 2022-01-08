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

package org.screamingsandals.lib.placeholders;

import org.screamingsandals.lib.sender.MultiPlatformOfflinePlayer;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.function.Supplier;

@AbstractService
public abstract class PlaceholderManager {

    private static PlaceholderManager placeholderManager;

    public static void init(Supplier<PlaceholderManager> placeholderManagerSupplier) {
        if (placeholderManager != null) {
            throw new UnsupportedOperationException("PlaceholderManager is already initialized!");
        }
        placeholderManager = placeholderManagerSupplier.get();
    }

    public static void registerExpansion(PlaceholderExpansion expansion) {
        if (placeholderManager == null) {
            throw new UnsupportedOperationException("PlaceholderManager is not initialized yet!");
        }
        placeholderManager.registerExpansion0(expansion);
    }

    public abstract void registerExpansion0(PlaceholderExpansion expansion);

    public static String resolveString(MultiPlatformOfflinePlayer player, String message) {
        if (placeholderManager == null) {
            throw new UnsupportedOperationException("PlaceholderManager is not initialized yet!");
        }
        return placeholderManager.resolveString0(player, message);
    }

    public abstract String resolveString0(MultiPlatformOfflinePlayer player, String message);

    public static boolean isInitialized() {
        return placeholderManager != null;
    }
}
