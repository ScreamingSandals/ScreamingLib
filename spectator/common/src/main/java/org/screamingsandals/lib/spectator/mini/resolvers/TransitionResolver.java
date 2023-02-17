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

package org.screamingsandals.lib.spectator.mini.resolvers;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.minitag.nodes.TagNode;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.mini.MiniMessageParser;
import org.screamingsandals.lib.spectator.mini.placeholders.Placeholder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransitionResolver implements StylingResolver {
    @Override
    public <B extends Component.Builder<B, C>, C extends Component> void applyStyle(@NotNull MiniMessageParser parser, @NotNull B builder, @NotNull TagNode tag, @NotNull Placeholder @NotNull ... placeholders) {
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
                            return; // invalid gradient phase
                        }
                        break;
                    } catch (NumberFormatException ignored) {
                    }
                }

                colors.add(Color.hexOrName(value));
            }

            if (colors.size() < 2) {
                return; // not enough colors
            }
        } else {
            colors = List.of(Color.WHITE, Color.BLACK);
        }

        boolean negativePhase = phase < 0;
        if (negativePhase) {
            phase++;
            Collections.reverse(colors);
        }

        var color = colors.get(0);
        {
            float steps = 1f / (colors.size() - 1);
            for (int colorIndex = 1; colorIndex < colors.size(); colorIndex++) {
                float val = colorIndex * steps;
                if (val >= phase) {
                    float factor = 1 + (phase - val) * (colors.size() - 1);

                    if (negativePhase) {
                        color = Color.interpolate(1 - factor, colors.get(colorIndex), colors.get(colorIndex - 1));
                    } else {
                        color = Color.interpolate(factor, colors.get(colorIndex - 1), colors.get(colorIndex));
                    }
                }
            }
        }

        builder.color(color);
    }
}
