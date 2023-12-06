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

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.*;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.TriState;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.function.Function;

@RequiredArgsConstructor
public class ComponentSerializer implements TypeSerializer<Component> {

    public static final @NotNull ComponentSerializer INSTANCE = new ComponentSerializer(s -> {
        if (s.matches(".*§[0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx].*")) {
            return Component.fromLegacy(s);
        }
        return Component.text(s);
    });

    // Text
    private static final @NotNull String TEXT_KEY = "text";

    // Translate
    private static final @NotNull String TRANSLATE_KEY = "translate";
    private static final @NotNull String FALLBACK_KEY = "fallback";
    private static final @NotNull String WITH_KEY = "with";

    // Score
    private static final @NotNull String SCORE_KEY = "score";
    private static final @NotNull String SCORE_NAME_KEY = "name";
    private static final @NotNull String SCORE_OBJECTIVE_KEY = "objective";
    private static final @NotNull String SCORE_VALUE_KEY = "value";

    // Selector
    private static final @NotNull String SELECTOR_KEY = "selector";

    // Keybind
    private static final @NotNull String KEYBIND_KEY = "keybind";

    // NBT
    private static final @NotNull String NBT_KEY = "nbt";
    private static final @NotNull String INTERPRET_KEY = "interpret";
    private static final @NotNull String BLOCK_KEY = "block";
    private static final @NotNull String ENTITY_KEY = "entity";
    private static final @NotNull String STORAGE_KEY = "storage";
    private static final @NotNull String SOURCE_KEY = "source";
    private static final @NotNull String SOURCE_VALUE_BLOCK = "block";
    private static final @NotNull String SOURCE_VALUE_ENTITY = "entity";
    private static final @NotNull String SOURCE_VALUE_STORAGE = "storage";

    // NBT # Selector
    private static final @NotNull String SEPARATOR_KEY = "separator";

    // Shared content & Formatting
    private static final @NotNull String EXTRA_KEY = "extra";
    private static final @NotNull String COLOR_KEY = "color";
    private static final @NotNull String FONT_KEY = "font";
    private static final @NotNull String BOLD_KEY = "bold";
    private static final @NotNull String ITALIC_KEY = "italic";
    private static final @NotNull String UNDERLINED_KEY = "underlined";
    private static final @NotNull String STRIKETHROUGH_KEY = "strikethrough";
    private static final @NotNull String OBFUSCATED_KEY = "obfuscated";
    private static final @NotNull String INSERTION_KEY = "insertion";
    private static final @NotNull String CLICK_EVENT_KEY = "clickEvent";
    private static final @NotNull String HOVER_EVENT_KEY = "hoverEvent";

    // Types
    private static final @NotNull String TYPE_KEY = "type";
    private static final @NotNull String TYPE_VALUE_TEXT = "text";
    private static final @NotNull String TYPE_VALUE_TRANSLATABLE = "translatable";
    private static final @NotNull String TYPE_VALUE_SCORE = "score";
    private static final @NotNull String TYPE_VALUE_SELECTOR = "selector";
    private static final @NotNull String TYPE_VALUE_KEYBIND = "keybind";
    private static final @NotNull String TYPE_VALUE_NBT = "nbt";

    private final @Nullable Function<String, Component> stringDeserializer;

    @Override
    public @NotNull Component deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            if (node.isList()) {
                var list = node.childrenList();
                if (list.isEmpty()) {
                    return Component.empty();
                } else if (list.size() == 1) {
                    return Objects.requireNonNull(list.get(0).get(Component.class));
                } else {
                    Component first = null; // TODO: Optimize by using builder
                    for (var child : list) {
                        if (first == null) {
                            first = child.get(Component.class);
                        } else {
                            first = first.withAppendix(Objects.requireNonNull(child.get(Component.class)));
                        }
                    }
                    return Objects.requireNonNull(first);
                }
            } else if (!node.isMap()) {
                var str = node.getString();
                if (str == null) {
                    return Component.empty();
                }
                if (stringDeserializer != null) {
                    return stringDeserializer.apply(str);
                }
                return Component.text(str);
            }

            var declaredType = node.node(TYPE_KEY).getString();

            Component.Builder<?,?> builder = null;
            if (TYPE_VALUE_TEXT.equals(declaredType) || (declaredType == null && node.hasChild(TEXT_KEY))) {
                Preconditions.checkArgument(!node.node(TEXT_KEY).virtual(), "A text component requires a text");
                builder = Component.text().content(node.node(TEXT_KEY).getString());
            } else if (TYPE_VALUE_TRANSLATABLE.equals(declaredType) || (declaredType == null && node.hasChild(TRANSLATE_KEY))) {
                Preconditions.checkArgument(!node.node(TRANSLATE_KEY).virtual(), "A translatable component requires a translate key");
                builder = Component.translatable().translate(node.node(TRANSLATE_KEY).getString()).fallback(node.node(FALLBACK_KEY).getString());

                if (node.hasChild(WITH_KEY)) {
                    var withNode = node.node(WITH_KEY);
                    if (withNode.isList()) {
                        ((TranslatableComponent.Builder) builder).args(withNode.getList(Component.class));
                    } else {
                        /* This is not valid in the official format, but it's probably just one component */
                        ((TranslatableComponent.Builder) builder).args(withNode.get(Component.class));
                    }
                }
            } else if (TYPE_VALUE_SCORE.equals(declaredType) || (declaredType == null && node.hasChild(SCORE_KEY))) {
                var scoreNode = node.node(SCORE_KEY);
                var nameNode = scoreNode.node(SCORE_NAME_KEY);
                var objectiveNode = scoreNode.node(SCORE_OBJECTIVE_KEY);
                Preconditions.checkArgument(!scoreNode.virtual() && !nameNode.virtual() && !objectiveNode.virtual(), "A score component requires a score, a name and an objective");
                var b = Component.score()
                        .name(nameNode.getString())
                        .objective(objectiveNode.getString());
                if (scoreNode.hasChild(SCORE_VALUE_KEY)) {
                    //noinspection deprecation
                    b.value(scoreNode.node(SCORE_VALUE_KEY).getString());
                }
                builder = b;
            } else if (TYPE_VALUE_SELECTOR.equals(declaredType) || (declaredType == null && node.hasChild(SELECTOR_KEY))) {
                Preconditions.checkArgument(!node.node(SELECTOR_KEY).virtual(), "A selector component requires a selector");
                builder = Component.selector().pattern(node.node(SELECTOR_KEY).getString());
            } else if (TYPE_VALUE_KEYBIND.equals(declaredType) || (declaredType == null && node.hasChild(KEYBIND_KEY))) {
                Preconditions.checkArgument(!node.node(KEYBIND_KEY).virtual(), "A keybind component requires a keybind");
                builder = Component.keybind().keybind(node.node(KEYBIND_KEY).getString());
            } else if (TYPE_VALUE_NBT.equals(declaredType) || (declaredType == null && node.hasChild(NBT_KEY))) {
                Preconditions.checkArgument(!node.node(NBT_KEY).virtual(), "A nbt component requires a nbt key");
                var nbt = node.node(NBT_KEY).getString();
                var interpret = node.node(INTERPRET_KEY).getBoolean();
                var declaredSource = node.node(SOURCE_KEY).getString();
                if ((declaredSource == null && node.hasChild(BLOCK_KEY)) || SOURCE_VALUE_BLOCK.equals(declaredSource)) {
                    Preconditions.checkArgument(!node.node(BLOCK_KEY).virtual(), "A nbt component with a block source requires a block");
                    builder = Component.blockNBT()
                            .nbtPath(nbt)
                            .interpret(interpret)
                            .blockPosition(node.node(BLOCK_KEY).getString());
                } else if ((declaredSource == null && node.hasChild(ENTITY_KEY)) || SOURCE_VALUE_ENTITY.equals(declaredSource)) {
                    Preconditions.checkArgument(!node.node(ENTITY_KEY).virtual(), "A nbt component with an entity source requires an entity");
                    builder = Component.entityNBT()
                            .nbtPath(nbt)
                            .interpret(interpret)
                            .selector(node.node(ENTITY_KEY).getString());
                } else if ((declaredSource == null && node.hasChild(STORAGE_KEY)) || SOURCE_VALUE_STORAGE.equals(declaredSource)) {
                    Preconditions.checkArgument(!node.node(STORAGE_KEY).virtual(), "A nbt component with a storage source requires a storage");
                    builder = Component.storageNBT()
                            .nbtPath(nbt)
                            .interpret(interpret)
                            .storageKey(ResourceLocation.of(node.node(STORAGE_KEY).getString()));
                }
            }

            if (builder == null) {
                builder = Component.text();
            }

            if (node.hasChild(EXTRA_KEY)) {
                var extraNode = node.node(EXTRA_KEY);
                if (extraNode.isList()) {
                    var extra = extraNode.childrenList();
                    for (var ex : extra) {
                        builder.append(Objects.requireNonNull(ex.get(Component.class)));
                    }
                } else {
                    /* This is not valid in the official format, but it's probably just one component */
                    builder.append(Objects.requireNonNull(extraNode.get(Component.class)));
                }
            }

            if (builder instanceof SeparableComponent.Builder && node.hasChild(SEPARATOR_KEY)) {
                ((SeparableComponent.Builder<?,?>) builder).separator(node.node(SEPARATOR_KEY).get(Component.class));
            }

            if (node.hasChild(FONT_KEY)) {
                builder.font(ResourceLocation.of(node.node(FONT_KEY).getString()));
            }

            if (node.hasChild(COLOR_KEY)) {
                builder.color(node.node(COLOR_KEY).get(Color.class));
            }

            if (node.hasChild(BOLD_KEY)) {
                builder.bold(node.node(BOLD_KEY).getBoolean());
            }

            if (node.hasChild(ITALIC_KEY)) {
                builder.italic(node.node(ITALIC_KEY).getBoolean());
            }

            if (node.hasChild(UNDERLINED_KEY)) {
                builder.underlined(node.node(UNDERLINED_KEY).getBoolean());
            }

            if (node.hasChild(STRIKETHROUGH_KEY)) {
                builder.strikethrough(node.node(STRIKETHROUGH_KEY).getBoolean());
            }

            if (node.hasChild(OBFUSCATED_KEY)) {
                builder.obfuscated(node.node(OBFUSCATED_KEY).getBoolean());
            }

            if (node.hasChild(INSERTION_KEY)) {
                builder.insertion(node.node(INSERTION_KEY).getString());
            }

            if (node.hasChild(CLICK_EVENT_KEY)) {
                builder.clickEvent(node.node(CLICK_EVENT_KEY).get(ClickEvent.class));
            }

            if (node.hasChild(HOVER_EVENT_KEY)) {
                builder.hoverEvent(node.node(HOVER_EVENT_KEY).get(HoverEvent.class));
            }

            return builder.build();
        } catch (Throwable throwable) {
            throw new SerializationException(throwable);
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable Component obj, @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        if (obj instanceof TextComponent) {
            node.node(TYPE_KEY).set(TYPE_VALUE_TEXT);
            node.node(TEXT_KEY).set(((TextComponent) obj).content());
        } else if (obj instanceof TranslatableComponent) {
            node.node(TYPE_KEY).set(TYPE_VALUE_TRANSLATABLE);
            node.node(TRANSLATE_KEY).set(((TranslatableComponent) obj).translate());
            node.node(FALLBACK_KEY).set(((TranslatableComponent) obj).fallback());
            var args = ((TranslatableComponent) obj).args();
            if (!args.isEmpty()) {
                var withNode = node.node(WITH_KEY);
                for (var arg : args) {
                    withNode.appendListNode().set(Component.class, arg);
                }
            }
        } else if (obj instanceof ScoreComponent) {
            node.node(TYPE_KEY).set(TYPE_VALUE_SCORE);
            var sc = (ScoreComponent) obj;
            var scoreNode = node.node(SCORE_KEY);
            scoreNode.node(SCORE_NAME_KEY).set(sc.name());
            scoreNode.node(SCORE_OBJECTIVE_KEY).set(sc.objective());

            @SuppressWarnings("deprecation")
            var value = sc.value();
            if (value != null) {
                scoreNode.node(SCORE_VALUE_KEY).set(value);
            }
        } else if (obj instanceof SelectorComponent) {
            node.node(TYPE_KEY).set(TYPE_VALUE_SELECTOR);
            node.node(SELECTOR_KEY).set(((SelectorComponent) obj).pattern());
        } else if (obj instanceof KeybindComponent) {
            node.node(TYPE_KEY).set(TYPE_VALUE_KEYBIND);
            node.node(KEYBIND_KEY).set(((KeybindComponent) obj).keybind());
        } else if (obj instanceof NBTComponent) {
            node.node(TYPE_KEY).set(TYPE_VALUE_NBT);
            var nbt = (NBTComponent) obj;
            node.node(NBT_KEY).set(nbt.nbtPath());
            node.node(INTERPRET_KEY).set(nbt.interpret());
            if (nbt instanceof BlockNBTComponent) {
                node.node(SOURCE_KEY).set(SOURCE_VALUE_BLOCK);
                node.node(BLOCK_KEY).set(((BlockNBTComponent) nbt).blockPosition());
            } else if (nbt instanceof EntityNBTComponent) {
                node.node(SOURCE_KEY).set(SOURCE_VALUE_ENTITY);
                node.node(ENTITY_KEY).set(((EntityNBTComponent) nbt).selector());
            } else if (nbt instanceof StorageNBTComponent) {
                node.node(SOURCE_KEY).set(SOURCE_VALUE_STORAGE);
                node.node(STORAGE_KEY).set(((StorageNBTComponent) nbt).storageKey().asString());
            }
        } else { // who knows what this component is this
            node.node(TEXT_KEY).set("");
        }

        if (obj instanceof SeparableComponent) {
            var separator = ((SeparableComponent) obj).separator();
            if (separator != null) {
                node.node(SEPARATOR_KEY).set(Component.class, separator);
            }
        }

        var extras = obj.children();
        if (!extras.isEmpty()) {
            var extrasNode = node.node(EXTRA_KEY);
            for (var extra : extras) {
                extrasNode.appendListNode().set(Component.class, extra);
            }
        }

        var font = obj.font();
        if (font != null) {
            node.node(FONT_KEY).set(font.asString());
        }

        var color = obj.color();
        if (color != null) {
            node.node(COLOR_KEY).set(Color.class, color);
        }

        var bold = obj.bold();
        if (bold != TriState.INITIAL) {
            node.node(BOLD_KEY).set(bold == TriState.TRUE);
        }

        var italic = obj.italic();
        if (italic != TriState.INITIAL) {
            node.node(ITALIC_KEY).set(italic == TriState.TRUE);
        }

        var underlined = obj.underlined();
        if (underlined != TriState.INITIAL) {
            node.node(UNDERLINED_KEY).set(underlined == TriState.TRUE);
        }

        var strikethrough = obj.strikethrough();
        if (strikethrough != TriState.INITIAL) {
            node.node(STRIKETHROUGH_KEY).set(strikethrough == TriState.TRUE);
        }

        var obfuscated = obj.obfuscated();
        if (obfuscated != TriState.INITIAL) {
            node.node(OBFUSCATED_KEY).set(obfuscated == TriState.TRUE);
        }

        var insertion = obj.insertion();
        if (insertion != null) {
            node.node(INSERTION_KEY).set(insertion);
        }

        var hoverEvent = obj.hoverEvent();
        if (hoverEvent != null) {
            node.node(HOVER_EVENT_KEY).set(HoverEvent.class, hoverEvent);
        }

        var clickEvent = obj.clickEvent();
        if (clickEvent != null) {
            node.node(CLICK_EVENT_KEY).set(ClickEvent.class, clickEvent);
        }
    }

    @Override
    public @Nullable Component emptyValue(@NotNull Type specificType, @NotNull ConfigurationOptions options) {
        return Component.empty();
    }
}
