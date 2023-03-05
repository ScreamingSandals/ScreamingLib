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

package org.screamingsandals.lib.plugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.api.Wrapper;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

import java.nio.file.Path;
import java.util.List;

public interface Plugin extends Wrapper, RawValueHolder {

    @Nullable Object getInstance();

    boolean isEnabled();

    @NotNull String pluginKey();

    @NotNull String name();

    @NotNull String version();

    @Nullable String description();

    @Unmodifiable @NotNull List<@NotNull Contributor> contributors();

    @Unmodifiable @NotNull List<@NotNull Dependency> dependencies();

    @NotNull Path dataFolder();

    interface Contributor {
        @NotNull String name();

        // TODO: add more info if any platform implements them
    }

    interface Dependency {
        @NotNull String pluginKey();

        boolean required();

        @NotNull LoadOrder loadOrder();

        @LimitedVersionSupport("Velocity, Sponge")
        @Nullable String requiredVersion();

        // TODO: add more info if any platform implements them

        enum LoadOrder {
            BEFORE,
            AFTER,
            ANY;
        }
    }
}
