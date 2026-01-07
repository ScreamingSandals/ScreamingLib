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

package org.screamingsandals.lib.impl.bungee.spectator;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.ShadowColor;
import org.screamingsandals.lib.utils.BasicWrapper;

public class BungeeShadowColor extends BasicWrapper<java.awt.Color> implements ShadowColor {
    protected BungeeShadowColor(@NotNull java.awt.Color wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public int red() {
        return wrappedObject.getRed();
    }

    @Override
    public int green() {
        return wrappedObject.getGreen();
    }

    @Override
    public int blue() {
        return wrappedObject.getBlue();
    }

    @Override
    public int alpha() {
        return wrappedObject.getAlpha();
    }

    @Override
    public @NotNull String toString() {
        return String.format("#%02X%02X%02X%02X", red(), green(), blue(), alpha());
    }

    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        try {
            return super.as(type);
        } catch (Throwable ignored) {
            return AbstractBungeeBackend.getAdditionalShadowColorConverter().convert(this, type);
        }
    }
}
