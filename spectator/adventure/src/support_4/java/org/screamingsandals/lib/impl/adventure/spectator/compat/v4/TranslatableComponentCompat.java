/*
 * Copyright 2026 ScreamingSandals
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

package org.screamingsandals.lib.impl.adventure.spectator.compat.v4;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@UtilityClass
public class TranslatableComponentCompat {
    @SuppressWarnings({"deprecation"})
    public static @NotNull List<Component> arguments(@NotNull TranslatableComponent component) {
        return component.args();
    }

    @SuppressWarnings({"deprecation"})
    public static @NotNull TranslatableComponent arguments(@NotNull TranslatableComponent component, @NotNull List<? extends ComponentLike> args) {
        return component.args(args);
    }

    @SuppressWarnings({"deprecation"})
    public static void builderArguments(TranslatableComponent.@NotNull Builder builder, @NotNull List<? extends ComponentLike> args) {
        builder.args(args);
    }
}
