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

package org.screamingsandals.lib.spectator.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.*;

@UtilityClass
public class ComponentUtils {
    public static @NotNull Component linear(final @NotNull ComponentBuilderApplicable @NotNull... applicables) {
        final int length = applicables.length;
        if (length == 0) {
            return Component.empty();
        }
        if (length == 1) {
            if (applicables[0] instanceof ComponentLike) {
                return ((ComponentLike) applicables[0]).asComponent();
            }
            throw new IllegalArgumentException("Not a component: " + applicables[0]);
        }
        final var builder = Component.text();
        Color color = null;
        for (final var applicable : applicables) {
            if (applicable instanceof Color) { // we currently support just colors
                color = (Color) applicable;
                // TODO: other styles
            } else if (color != null && applicable instanceof ComponentLike) {
                ((ComponentLike) applicable).asComponent().withColor(color).apply(builder);
            } else {
                applicable.apply(builder);
            }
        }
        return builder.build();
    }

    @NotNull
    public static Component repeat(@NotNull Component component, int repetitions, @Nullable Component separator) {
        if (repetitions == 0) {
            return Component.empty();
        }
        if (repetitions == 1) {
            return component;
        }
        final var builder = Component.text();
        for (int i = 0; i < repetitions; i++) {
            builder.append(component);
            if (separator != null) {
                builder.append(separator);
            }
        }
        return builder.build();
    }
}
