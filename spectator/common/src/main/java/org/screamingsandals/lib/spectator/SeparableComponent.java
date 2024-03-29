/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.spectator;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

public interface SeparableComponent extends Component {
    @LimitedVersionSupport(">= 1.17")
    @Nullable Component separator();

    @Contract(pure = true)
    @NotNull SeparableComponent withSeparator(@Nullable Component separator);

    interface Builder<B extends Builder<B, C>, C extends SeparableComponent> extends Component.Builder<B, C> {
        @LimitedVersionSupport(">= 1.17")
        @Contract("_ -> this")
        @NotNull B separator(@Nullable Component separator);
    }
}
