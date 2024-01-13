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

package org.screamingsandals.lib.impl.adventure.spectator;

import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.utils.BasicWrapper;

public class AdventureColor extends BasicWrapper<TextColor> implements Color {
    public AdventureColor(@NotNull TextColor wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public int red() {
        return wrappedObject.red();
    }

    @Override
    public int green() {
        return wrappedObject.green();
    }

    @Override
    public int blue() {
        return wrappedObject.blue();
    }

    @Override
    public @NotNull String toString() {
        return wrappedObject.toString();
    }

    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        try {
            return super.as(type);
        } catch (Throwable ignored) {
            return AdventureBackend.getAdditionalColorConverter().convert(this, type);
        }
    }
}
