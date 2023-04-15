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

package org.screamingsandals.lib.placeholders.hooks;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.placeholders.PlaceholderExpansion;
import org.screamingsandals.lib.sender.MultiPlatformOfflinePlayer;

import java.util.ArrayList;
import java.util.List;

public class DummyHook extends AbstractPAPILikePlaceholder {
    private final @NotNull List<@NotNull PlaceholderExpansion> expansions = new ArrayList<>();

    @Override
    public void register(@NotNull PlaceholderExpansion expansion) {
        if (!expansions.contains(expansion)) {
            expansions.add(expansion);
        }
    }

    @Override
    protected boolean has(@NotNull String identifier) {
        return expansions.stream().anyMatch(placeholderExpansion -> placeholderExpansion.getIdentifier().equals(identifier));
    }

    @Override
    protected @Nullable String resolve(@Nullable MultiPlatformOfflinePlayer player, @NotNull String identifier, @NotNull String parameters) {
        final var expansion = expansions.stream().filter(placeholderExpansion -> placeholderExpansion.getIdentifier().equals(identifier)).findFirst();
        if (expansion.isEmpty()) {
            return null;
        }

        var r = expansion.get().onRequest(player, parameters);
        return r != null ? r.toLegacy() : null;
    }
}
