/*
 * Copyright 2025 ScreamingSandals
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

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.api.Wrapper;
import org.screamingsandals.lib.impl.spectator.Spectator;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

@LimitedVersionSupport(">= 1.21.4")
public interface ShadowColor extends Wrapper, ComponentBuilderApplicable, RawValueHolder {
    int red();

    int green();

    int blue();

    int alpha();

    default int compoundArgb() {
        return alpha() << 24 | red() << 16 | green() << 8 | blue();
    }

    @NotNull String toString();

    static @NotNull ShadowColor rgb(int red, int green, int blue) {
        return Spectator.getBackend().shadowArgb(1, red, green, blue);
    }

    static @NotNull ShadowColor rgba(int red, int green, int blue, int alpha) {
        return Spectator.getBackend().shadowArgb(red, green, blue, alpha);
    }

    static @NotNull ShadowColor argb(int compound) {
        return Spectator.getBackend().shadowArgb((compound >> 24) & 0xFF, (compound >> 16) & 0xFF, (compound >> 8) & 0xFF, compound & 0xFF);
    }

    static @NotNull ShadowColor hex(@NotNull String hex) {
        return Spectator.getBackend().shadowHex(hex);
    }

    static @NotNull ShadowColor interpolate(float t, final @NotNull ShadowColor a, @NotNull ShadowColor b) {
        float clampedT = Math.min(1.0f, Math.max(0.0f, t));
        int ar = a.red();
        int br = b.red();
        int ag = a.green();
        int bg = b.green();
        int ab = a.blue();
        int bb = b.blue();
        int aa = a.alpha();
        int ba = b.alpha();
        return rgba(
                Math.round(ar + clampedT * (br - ar)),
                Math.round(ag + clampedT * (bg - ag)),
                Math.round(ab + clampedT * (bb - ab)),
                Math.round(aa + clampedT * (ba - aa))
        );
    }

    @Override
    default <C extends Component, B extends Component.Builder<B, C>> void apply(@NotNull B builder) {
        builder.shadowColor(this);
    }

    @Override
    default @NotNull Component applyTo(@NotNull Component component) {
        return component.withShadowColor(this);
    }
}
