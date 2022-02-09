package org.screamingsandals.lib.spectator.configurate;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.*;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.utils.TriState;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.function.Function;

@RequiredArgsConstructor
public class ComponentSerializer implements TypeSerializer<Component> {

    public static final ComponentSerializer INSTANCE = new ComponentSerializer(null);

    // Text
    private static final String TEXT_KEY = "text";

    // Translate
    private static final String TRANSLATE_KEY = "translate";
    private static final String WITH_KEY = "with";

    // Score
    private static final String SCORE_KEY = "score";
    private static final String SCORE_NAME_KEY = "name";
    private static final String SCORE_OBJECTIVE_KEY = "objective";
    private static final String SCORE_VALUE_KEY = "value";

    // Selector
    private static final String SELECTOR_KEY = "selector";

    // Keybind
    private static final String KEYBIND_KEY = "keybind";

    // NBT
    private static final String NBT_KEY = "nbt";
    private static final String INTERPRET_KEY = "interpret";
    private static final String BLOCK_KEY = "block";
    private static final String ENTITY_KEY = "entity";
    private static final String STORAGE_KEY = "storage";

    // NBT # Selector
    private static final String SEPARATOR_KEY = "separator";

    // Shared content & Formatting
    private static final String EXTRA_KEY = "extra";
    private static final String COLOR_KEY = "color";
    private static final String FONT_KEY = "font";
    private static final String BOLD_KEY = "bold";
    private static final String ITALIC_KEY = "italic";
    private static final String UNDERLINED_KEY = "underlined";
    private static final String STRIKETHROUGH_KEY = "strikethrough";
    private static final String OBFUSCATED_KEY = "obfuscated";
    private static final String INSERTION_KEY = "insertion";
    private static final String CLICK_EVENT_KEY = "clickEvent";
    private static final String HOVER_EVENT_KEY = "hoverEvent";

    @Nullable
    private final Function<String, Component> stringDeserializer;

    @Override
    public Component deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            if (node.isList()) {
                var list = node.childrenList();
                if (list.size() == 0) {
                    return Component.empty();
                } else if (list.size() == 1) {
                    return list.get(0).get(Component.class);
                } else {
                    Component first = null; // TODO: Optimize by using builder
                    for (var child : list) {
                        if (first == null) {
                            first = child.get(Component.class);
                        } else {
                            first = first.withAppendix(child.get(Component.class));
                        }
                    }
                    return first;
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

            Component.Builder<?,?> builder;
            if (node.hasChild(TEXT_KEY)) {
                builder = Component.text().content(node.node(TEXT_KEY).getString());
            } else if (node.hasChild(TRANSLATE_KEY)) {

            }
            // TODO

            return null;
        } catch (Throwable throwable) {
            throw new SerializationException(throwable);
        }
    }

    @Override
    public void serialize(Type type, @Nullable Component obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        if (obj instanceof TextComponent) {
            node.set(TEXT_KEY).set(((TextComponent) obj).content());
        } else if (obj instanceof TranslatableComponent) {
            node.set(TRANSLATE_KEY).set(((TranslatableComponent) obj).translate());
            var args = ((TranslatableComponent) obj).args();
            if (!args.isEmpty()) {
                var withNode = node.node(WITH_KEY);
                for (var arg : args) {
                    withNode.appendListNode().set(Component.class, arg);
                }
            }
        } else if (obj instanceof ScoreComponent) {
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
            node.node(SELECTOR_KEY).set(((SelectorComponent) obj).pattern());
        } else if (obj instanceof KeybindComponent) {
            node.node(KEYBIND_KEY).set(((KeybindComponent) obj).keybind());
        } else if (obj instanceof NBTComponent) {
            var nbt = (NBTComponent) obj;
            node.node(NBT_KEY).set(nbt.nbtPath());
            node.node(INTERPRET_KEY).set(nbt.interpret());
            if (nbt instanceof BlockNBTComponent) {
                node.node(BLOCK_KEY).set(((BlockNBTComponent) nbt).blockPosition());
            } else if (nbt instanceof EntityNBTComponent) {
                node.node(ENTITY_KEY).set(((EntityNBTComponent) nbt).selector());
            } else if (nbt instanceof StorageNBTComponent) {
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
    @Nullable
    public Component emptyValue(Type specificType, ConfigurationOptions options) {
        return Component.empty();
    }
}
