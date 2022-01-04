/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.configurate;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public abstract class AbstractScreamingSerializer {

    @Nullable
    protected TextColor deserializeColor(ConfigurationNode colorNode) {
        if (colorNode.isMap()) {
            return TextColor.color(colorNode.node("red").getInt(colorNode.node("RED").getInt()), colorNode.node("green").getInt(colorNode.node("GREEN").getInt()), colorNode.node("blue").getInt(colorNode.node("BLUE").getInt()));
        } else {
            var color = colorNode.getString("");
            var c = TextColor.fromCSSHexString(color);
            if (c != null) {
                return c;
            } else {
                var named = NamedTextColor.NAMES.value(color.toLowerCase().trim());
                if (named != null) {
                    return named;
                } else {
                    try {
                        var number = colorNode.get(Integer.class); // Please fail if the raw object is not Number
                        if (number != null) {
                            return TextColor.color(number);
                        }
                    } catch (Throwable ignored) {
                    }
                    return null;
                }
            }
        }
    }

    protected void serializeColor(TextColor color, ConfigurationNode node) throws SerializationException {
        if (color instanceof NamedTextColor) {
            node.set(NamedTextColor.NAMES.key(((NamedTextColor) color)));
        } else {
            var exact = NamedTextColor.ofExact(color.value());
            if (exact != null) {
                node.set(NamedTextColor.NAMES.key(exact));
            } else {
                node.set(color.asHexString());
            }
        }
    }

    protected void serializeColor(RGBLike color, ConfigurationNode node) throws SerializationException {
        if (color instanceof TextColor) {
            serializeColor((TextColor) color, node);
        } else {
            serializeColor(TextColor.color(color.red(), color.green(), color.blue()), node);
        }
    }
}
