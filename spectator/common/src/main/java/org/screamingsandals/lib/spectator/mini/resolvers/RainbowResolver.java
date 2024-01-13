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

package org.screamingsandals.lib.spectator.mini.resolvers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.minitag.nodes.TagNode;
import org.screamingsandals.lib.spectator.Color;

public class RainbowResolver extends ColorChangingResolver {
    @Override
    protected @Nullable TagInstance obtainNewTagInstance(@NotNull TagNode tag) {
        boolean reversed = false;
        int phase = 1;
        if (tag.getArgs().size() >= 1) {
            var value = tag.getArgs().get(0);
            if (value.startsWith("!")) {
                reversed = true;
                value = value.substring(1);
            }
            if (!value.isEmpty()) {
                try {
                    phase = Integer.parseInt(value);
                } catch (NumberFormatException ignored) {
                }
            }
        }

        final boolean finalReversed = reversed;
        final int finalPhase = phase;
        return new TagInstance() {
            private int size;
            private double frequency = 1;
            private int colorIndex;

            @Override
            public void init(int size) {
                this.size = size;
                this.frequency = Math.PI * 2 / size;
                if (finalReversed) {
                    this.colorIndex = size - 1;
                }
            }

            @Override
            public void advanceColor() {
                if (finalReversed) {
                    if (this.colorIndex == 0) {
                        this.colorIndex = this.size - 1;
                    } else {
                        this.colorIndex--;
                    }
                } else {
                    this.colorIndex++;
                }
            }

            @Override
            public @NotNull Color color() {
                final int index = this.colorIndex;
                final int red = (int) (Math.sin(this.frequency * index + 2 + finalPhase) * 127 + 128);
                final int green = (int) (Math.sin(this.frequency * index + 0 + finalPhase) * 127 + 128);
                final int blue = (int) (Math.sin(this.frequency * index + 4 + finalPhase) * 127 + 128);
                return Color.rgb(red, green, blue);
            }
        };
    }
}
