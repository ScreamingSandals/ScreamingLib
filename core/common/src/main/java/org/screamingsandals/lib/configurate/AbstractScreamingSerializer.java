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

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Color;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public abstract class AbstractScreamingSerializer {

    @Nullable
    protected Color deserializeColor(ConfigurationNode colorNode) {
        if (colorNode.isMap()) {
            return Color.rgb(colorNode.node("red").getInt(colorNode.node("RED").getInt()), colorNode.node("green").getInt(colorNode.node("GREEN").getInt()), colorNode.node("blue").getInt(colorNode.node("BLUE").getInt()));
        } else {
            var color = colorNode.getString("");
            return Color.hexOrName(color);
        }
    }

    protected void serializeColor(Color color, ConfigurationNode node) throws SerializationException {
        node.set(color.toString());
    }
}
