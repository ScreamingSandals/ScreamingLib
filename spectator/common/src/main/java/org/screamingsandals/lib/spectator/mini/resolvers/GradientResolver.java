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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GradientResolver extends ColorChangingResolver {

    @Override
    protected @Nullable TagInstance obtainNewTagInstance(@NotNull TagNode tag) {
        float phase = 0;
        List<Color> colors;
        var args = tag.getArgs().iterator();
        if (args.hasNext()) {
            colors = new ArrayList<>();
            while (args.hasNext()) {
                var value = args.next();

                if (!args.hasNext()) {
                    try {
                        phase = (float) Double.parseDouble(value);
                        if (phase < -1f || phase > 1f) {
                            return null; // invalid gradient phase
                        }
                        break;
                    } catch (NumberFormatException ignored) {
                    }
                }

                colors.add(Color.hexOrName(value));
            }

            if (colors.size() < 2) {
                return null; // not enough colors
            }
        } else {
            colors = List.of(Color.WHITE, Color.BLACK);
        }

        boolean negativePhase = phase < 0;
        if (negativePhase) {
            phase++;
            Collections.reverse(colors);
        }

        final float finalPhase = phase;
        return new TagInstance() {
            private int index;
            private int colorIndex;

            private float factorStep;
            private float phase;

            @Override
            public void init(int size) {
                int sectorLength = size / (colors.size() - 1);
                if (sectorLength < 1) {
                    sectorLength = 1;
                }
                this.factorStep = 1.0f / (sectorLength + this.index);
                this.phase = finalPhase * sectorLength;
            }

            @Override
            public void advanceColor() {
                this.index++;
                if (this.factorStep * this.index > 1) {
                    this.colorIndex++;
                    this.index = 0;
                }
            }

            @Override
            public @NotNull Color color() {
                float factor = this.factorStep * (this.index + this.phase);
                if (factor > 1) {
                    factor = 1 - (factor - 1);
                }

                if (negativePhase && colors.size() % 2 != 0) {
                    return Color.interpolate(factor, colors.get(this.colorIndex + 1), colors.get(this.colorIndex));
                } else {
                    return Color.interpolate(factor, colors.get(this.colorIndex), colors.get(this.colorIndex + 1));
                }
            }
        };
    }
}
