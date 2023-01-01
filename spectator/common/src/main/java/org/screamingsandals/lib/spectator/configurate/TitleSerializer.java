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

package org.screamingsandals.lib.spectator.configurate;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.title.Title;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.time.Duration;

public class TitleSerializer implements TypeSerializer<Title> {
    public static final TitleSerializer INSTANCE = new TitleSerializer();

    private static final String TITLE_KEY = "title";
    private static final String SUBTITLE_KEY = "subtitle";
    private static final String TIMES_KEY = "times";
    private static final String FADE_IN_KEY = "fade-in";
    private static final String STAY_KEY = "stay";
    private static final String FADE_OUT_KEY = "fade-out";

    @Override
    public Title deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            var title = node.node(TITLE_KEY).get(Component.class, Component.empty());
            var subtitle = node.node(SUBTITLE_KEY).get(Component.class, Component.empty());
            @Nullable
            var fadeIn = node.node(TIMES_KEY, FADE_IN_KEY).get(Duration.class);
            @Nullable
            var stay = node.node(TIMES_KEY, STAY_KEY).get(Duration.class);
            @Nullable
            var fadeOut = node.node(TIMES_KEY, FADE_OUT_KEY).get(Duration.class);
            return Title.builder()
                    .title(title)
                    .subtitle(subtitle)
                    .fadeIn(fadeIn)
                    .stay(stay)
                    .fadeOut(fadeOut)
                    .build();
        } catch (Throwable throwable) {
            throw new SerializationException(throwable);
        }
    }

    @Override
    public void serialize(Type type, @Nullable Title obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        node.node(TITLE_KEY).set(Component.class, obj.title());
        node.node(SUBTITLE_KEY).set(Component.class, obj.subtitle());
        node.node(TIMES_KEY, FADE_IN_KEY).set(Duration.class, obj.fadeIn());
        node.node(TIMES_KEY, STAY_KEY).set(Duration.class, obj.stay());
        node.node(TIMES_KEY, FADE_OUT_KEY).set(Duration.class, obj.fadeOut());
    }
}
