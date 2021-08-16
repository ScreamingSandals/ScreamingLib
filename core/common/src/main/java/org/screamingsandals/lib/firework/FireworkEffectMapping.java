package org.screamingsandals.lib.firework;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("AlternativeMethodAvailable")
@AbstractService
public abstract class FireworkEffectMapping extends AbstractTypeMapper<FireworkEffectHolder> {
    private static FireworkEffectMapping fireworkEffectMapping = null;
    private static final Function<ConfigurationNode, FireworkEffectHolder> CONFIGURATE_METHOD = node -> {
        if (!node.isMap()) {
            return resolve(node.getString()).orElse(null);
        }

        var effectNode = node.node("type");
        var flickerNode = node.node("flicker");
        var trailNode = node.node("trail");
        var colorsNode = node.node("colors");
        var fadeColorsNode = node.node("fade-colors");

        var holderOptional = resolve(effectNode.getString());
        if (holderOptional.isPresent()) {
            var holder = holderOptional.get()
                    .flicker(flickerNode.getBoolean(true))
                    .trail(trailNode.getBoolean(true));


            if (!colorsNode.empty()) {
                if (colorsNode.isList()) {
                    holder = holder.colors(colorsNode.childrenList()
                            .stream()
                            .map(node2 -> {
                                if (node2.isMap()) {
                                    return TextColor.color(node2.node("red").getInt(), node2.node("green").getInt(), node2.node("blue").getInt());
                                } else {
                                    var color = node2.getString("");
                                    var c = TextColor.fromCSSHexString(color);
                                    if (c != null) {
                                        return c;
                                    } else {
                                        var c2 = NamedTextColor.NAMES.value(color.toLowerCase().trim());
                                        if (c2 != null) {
                                            return c2;
                                        }
                                    }
                                }
                                return null;
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList()));
                } else if (colorsNode.isMap()) {
                    holder = holder.colors(List.of(TextColor.color(colorsNode.node("red").getInt(), colorsNode.node("green").getInt(), colorsNode.node("blue").getInt())));
                } else {
                    var color = colorsNode.getString("");
                    var c = TextColor.fromCSSHexString(color);
                    if (c != null) {
                        holder = holder.colors(List.of(c));
                    } else {
                        var c2 = NamedTextColor.NAMES.value(color.toLowerCase().trim());
                        if (c2 != null) {
                            holder = holder.colors(List.of(c2));
                        }
                    }
                }
            }


            if (!fadeColorsNode.empty()) {
                if (fadeColorsNode.isList()) {
                    holder = holder.fadeColors(fadeColorsNode.childrenList()
                            .stream()
                            .map(node2 -> {
                                if (node2.isMap()) {
                                    return TextColor.color(node2.node("red").getInt(), node2.node("green").getInt(), node2.node("blue").getInt());
                                } else {
                                    var color = node2.getString("");
                                    var c = TextColor.fromCSSHexString(color);
                                    if (c != null) {
                                        return c;
                                    } else {
                                        var c2 = NamedTextColor.NAMES.value(color.toLowerCase().trim());
                                        if (c2 != null) {
                                            return c2;
                                        }
                                    }
                                }
                                return null;
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList()));
                } else if (fadeColorsNode.isMap()) {
                    holder = holder.fadeColors(List.of(TextColor.color(fadeColorsNode.node("red").getInt(), fadeColorsNode.node("green").getInt(), fadeColorsNode.node("blue").getInt())));
                } else {
                    var color = fadeColorsNode.getString("");
                    var c = TextColor.fromCSSHexString(color);
                    if (c != null) {
                        holder = holder.fadeColors(List.of(c));
                    } else {
                        var c2 = NamedTextColor.NAMES.value(color.toLowerCase().trim());
                        if (c2 != null) {
                            holder = holder.fadeColors(List.of(c2));
                        }
                    }
                }
            }

            return holder;
        }
        return null;
    };
    protected BidirectionalConverter<FireworkEffectHolder> fireworkEffectConverter = BidirectionalConverter.<FireworkEffectHolder>build()
            .registerW2P(String.class, FireworkEffectHolder::getPlatformName)
            .registerP2W(FireworkEffectHolder.class, e -> e)
            .registerP2W(ConfigurationNode.class, CONFIGURATE_METHOD)
            .registerP2W(Map.class, map -> {
                try {
                    return CONFIGURATE_METHOD.apply(BasicConfigurationNode.root().set(map));
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
                return null;
            });

    public FireworkEffectMapping() {
        if (fireworkEffectMapping != null) {
            throw new UnsupportedOperationException("FireworkEffectMapping is already initialized.");
        }

        fireworkEffectMapping = this;
    }

    @OnPostConstruct
    public void postConstruct() {
        mapAlias("SMALL", "BALL");
        mapAlias("LARGE", "BALL_LARGE");
    }

    @CustomAutocompletion(CustomAutocompletion.Type.FIREWORK_EFFECT)
    @OfMethodAlternative(value = FireworkEffectHolder.class, methodName = "ofOptional")
    public static Optional<FireworkEffectHolder> resolve(Object fireworkEffectObject) {
        if (fireworkEffectMapping == null) {
            throw new UnsupportedOperationException("FireworkEffectMapping is not initialized yet.");
        }
        if (fireworkEffectObject == null) {
            return Optional.empty();
        }

        return fireworkEffectMapping.fireworkEffectConverter.convertOptional(fireworkEffectObject).or(() -> fireworkEffectMapping.resolveFromMapping(fireworkEffectObject));
    }

    public static <T> T convertFireworkEffectHolder(FireworkEffectHolder holder, Class<T> newType) {
        if (fireworkEffectMapping == null) {
            throw new UnsupportedOperationException("FireworkEffectMapping is not initialized yet.");
        }
        return fireworkEffectMapping.fireworkEffectConverter.convert(holder, newType);
    }

    public static boolean isInitialized() {
        return fireworkEffectMapping != null;
    }
}
