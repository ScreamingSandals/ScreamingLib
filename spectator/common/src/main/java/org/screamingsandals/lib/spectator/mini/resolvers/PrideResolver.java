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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class PrideResolver extends ColorChangingResolver {

    private static final Map<String, List<Color>> FLAGS = new HashMap<>();

    static {
        // colors ported from Adventure, licensed under MIT:
        // https://github.com/KyoriPowered/adventure/blob/d0e78f844c2f48070f1ccf64056331522d025874/text-minimessage/src/main/java/net/kyori/adventure/text/minimessage/tag/standard/PrideTag.java

        FLAGS.put("pride", colors(0xE50000, 0xFF8D00, 0xFFEE00, 0x28121, 0x004CFF, 0x770088));
        FLAGS.put("progress", colors(0xFFFFFF, 0xFFAFC7, 0x73D7EE, 0x613915, 0x000000, 0xE50000, 0xFF8D00, 0xFFEE00, 0x28121, 0x004CFF, 0x770088));
        FLAGS.put("trans", colors(0x5BCFFB, 0xF5ABB9, 0xFFFFFF, 0xF5ABB9, 0x5BCFFB));
        FLAGS.put("bi", colors(0xD60270, 0x9B4F96, 0x0038A8));
        FLAGS.put("pan", colors(0xFF1C8D, 0xFFD700, 0x1AB3FF));
        FLAGS.put("nb", colors(0xFCF431, 0xFCFCFC, 0x9D59D2, 0x282828));
        FLAGS.put("lesbian", colors(0xD62800, 0xFF9B56, 0xFFFFFF, 0xD4662A6, 0xA40062));
        FLAGS.put("ace", colors(0x000000, 0xA4A4A4, 0xFFFFFF, 0x810081));
        FLAGS.put("agender", colors(0x000000, 0xBABABA, 0xFFFFFF, 0xBAF484, 0xFFFFFF, 0xBABABA, 0x000000));
        FLAGS.put("demisexual", colors(0x000000, 0xFFFFFF, 0x6E0071, 0xD3D3D3));
        FLAGS.put("genderqueer", colors(0xB57FDD, 0xFFFFFF, 0x49821E));
        FLAGS.put("genderfluid", colors(0xFE76A2, 0xFFFFFF, 0xBF12D7, 0x000000, 0x303CBE));
        FLAGS.put("intersex", colors(0xFFD800, 0x7902AA, 0xFFD800));
        FLAGS.put("aro", colors(0x3BA740, 0xA8D47A, 0xFFFFFF, 0xABABAB, 0x000000));
        FLAGS.put("baker", colors(0xCD66FF, 0xFF6599, 0xFE0000, 0xFE9900, 0xFFFF01, 0x009900, 0x0099CB, 0x350099, 0x990099));
        FLAGS.put("philly", colors(0x000000, 0x784F17, 0xFE0000, 0xFD8C00, 0xFFE500, 0x119F0B, 0x0644B3, 0xC22EDC));
        FLAGS.put("queer", colors(0x000000, 0x9AD9EA, 0x00A3E8, 0xB5E51D, 0xFFFFFF, 0xFFC90D, 0xFC6667, 0xFEAEC9, 0x000000));
        FLAGS.put("gay", colors(0x078E70, 0x26CEAA, 0x98E8C1, 0xFFFFFF, 0x7BADE2, 0x5049CB, 0x3D1A78));
        FLAGS.put("bigender", colors(0xC479A0, 0xECA6CB, 0xD5C7E8, 0xFFFFFF, 0xD5C7E8, 0x9AC7E8, 0x6C83CF));
        FLAGS.put("demigender", colors(0x7F7F7F, 0xC3C3C3, 0xFBFF74, 0xFFFFFF, 0xFBFF74, 0xC3C3C3, 0x7F7F7F));
    }

    @Override
    protected @Nullable TagInstance obtainNewTagInstance(@NotNull TagNode tag) {
        @NotNull String flagName;
        var args = tag.getArgs().iterator();
        if (args.hasNext()) {
            flagName = args.next().toLowerCase(Locale.ROOT);
        } else {
            flagName = "pride";
        }
        List<Color> colors = FLAGS.get(flagName);

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
                this.phase = 0;
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

                return Color.interpolate(factor, colors.get(this.colorIndex), colors.get(this.colorIndex + 1));
            }
        };
    }

    private static @NotNull List<Color> colors(final int @NotNull... colors) {
        return Arrays.stream(colors).mapToObj(Color::rgb).collect(Collectors.toList());
    }
}
