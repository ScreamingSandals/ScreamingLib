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
import org.screamingsandals.lib.spectator.*;
import org.screamingsandals.lib.spectator.mini.MiniMessageParser;
import org.screamingsandals.lib.spectator.mini.placeholders.Placeholder;

import java.util.ArrayList;
import java.util.List;

public abstract class ColorChangingResolver implements DoubleTagResolver {
    @SuppressWarnings("unchecked")
    @Override
    public <B extends Component.Builder<B, C>, C extends Component> @Nullable B resolve(@NotNull MiniMessageParser parser, @NotNull B builder, @NotNull TagNode tag, @NotNull Placeholder @NotNull ... placeholders) {
        var instance = obtainNewTagInstance(tag);

        if (instance == null) {
            return builder;
        }

        var component = builder.build();

        int size = calculateSize(component);
        instance.init(size);

        var value = apply(component, instance);

        if (value instanceof TextComponent) {
            return (B) ((TextComponent) value).toBuilder();
        } else if (value instanceof TranslatableComponent) {
            return (B) ((TranslatableComponent) value).toBuilder();
        } else if (value instanceof BlockNBTComponent) {
            return (B) ((BlockNBTComponent) value).toBuilder();
        } else if (value instanceof EntityNBTComponent) {
            return (B) ((EntityNBTComponent) value).toBuilder();
        } else if (value instanceof KeybindComponent) {
            return (B) ((KeybindComponent) value).toBuilder();
        } else if (value instanceof ScoreComponent) {
            return (B) ((ScoreComponent) value).toBuilder();
        } else if (value instanceof SelectorComponent) {
            return (B) ((SelectorComponent) value).toBuilder();
        } else if (value instanceof StorageNBTComponent) {
            return (B) ((StorageNBTComponent) value).toBuilder();
        }

        // How did we get here?
        return (B) Component.text().append(value);
    }

    private int calculateSize(@NotNull Component component) {
        int size = 0;
        if (component instanceof TextComponent) {
            var content = ((TextComponent) component).content();
            size += content.codePointCount(0, content.length());
        } else {
            size = 1; // unknown components get 1
        }
        for (var child : component.children()) {
            size += calculateSize(child);
        }
        return size;
    }

    private @NotNull Component apply(@NotNull Component component, @NotNull TagInstance instance) {
        if (component.color() != null) {
            var len = calculateSize(component);
            for (var i = 0; i < len; i++) {
                instance.advanceColor();
            }

            return component;
        }

        var originalChildren = component.children();
        component = component.withChildren(List.of());

        if (component instanceof TextComponent) {
            var content = ((TextComponent) component).content();
            var builder = ((TextComponent) component).toBuilder().content("");
            var holder = new int[1];
            for (var it = content.codePoints().iterator(); it.hasNext(); ) {
                holder[0] = it.nextInt();
                builder.append(Component.text(new String(holder, 0, 1), instance.color()));
                instance.advanceColor();
            }

            component = builder.build();
        } else {
            component = component.withColorIfAbsent(instance.color());
            instance.advanceColor();
        }

        var newChildren = new ArrayList<Component>();
        for (var child : originalChildren) {
            newChildren.add(apply(child, instance));
        }

        return component.withAppendix(newChildren);
    }

    protected abstract @Nullable TagInstance obtainNewTagInstance(@NotNull TagNode tag);

    protected interface TagInstance {
        void init(int size);

        void advanceColor();

        @NotNull Color color();
    }
}
