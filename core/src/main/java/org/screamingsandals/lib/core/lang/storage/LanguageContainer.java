package org.screamingsandals.lib.core.lang.storage;

import com.google.common.base.Preconditions;
import io.leangen.geantyref.TypeToken;
import lombok.Data;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.OfflinePlayer;
import org.screamingsandals.lib.core.config.SConfig;
import org.screamingsandals.lib.core.papi.PlaceholderConfig;
import org.screamingsandals.lib.core.wrapper.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.*;

@Data
public class LanguageContainer {
    private final Logger log = LoggerFactory.getLogger(LanguageContainer.class);
    private final SConfig config;
    private LanguageContainer fallback;
    private String code;
    private String prefix;
    private PlaceholderConfig papiConfig;
    private PluginWrapper pluginWrapper;

    public LanguageContainer(SConfig config, LanguageContainer fallback, String code,
                             String prefix, PlaceholderConfig papiConfig, PluginWrapper pluginWrapper) {
        this.config = Preconditions.checkNotNull(config, "config");
        this.fallback = fallback;
        this.code = Preconditions.checkNotNull(code, "code");
        this.prefix = Objects.requireNonNullElseGet(prefix, () -> Preconditions.checkNotNull(config.node("prefix").getString()));
        this.papiConfig = papiConfig;
        this.pluginWrapper = pluginWrapper;
    }

    public List<TextComponent> getMessages(String key, boolean prefix, Map<String, Object> placeholders, UUID uuid) {
        List<MessageContainer> messages;
        if (prefix) {
            messages = getMessagesWithPrefix(key);
        } else {
            messages = getMessages(key);
        }

        final var toReturn = new LinkedList<TextComponent>();
        messages.forEach(container -> {
            if (uuid != null && pluginWrapper.getType() == PluginWrapper.ServerType.PAPER) {
                final var player = pluginWrapper.getWrapperFor(uuid);
                if (player.isEmpty()) {
                    log.debug("Player is null, returning normal component.");
                    toReturn.add(container.toComponent(placeholders));
                    return;
                }

                if (papiConfig.isEnabled() && papiConfig.isUsePapi()) {
                    log.debug("Using PAPI for placeholders!");
                    me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(
                            (OfflinePlayer) player.get().getInstance(), container.getText());
                }
            }
            toReturn.add(container.toComponent(placeholders));
        });
        return toReturn;
    }

    public List<MessageContainer> getMessages(String key) {
        final var node = config.node(key);
        final var notFoundContainer = new MessageContainer("&cNo translation found for: " + key);
        final var fallbackNode = fallback != null ? fallback.getConfig().node(key) : CommentedConfigurationNode.root();

        if (node.empty()) {
            log.debug("Not found any translation, trying fallback!");

            if (fallbackNode.empty()) {
                log.debug("Fallback node is empty, returning not found container!");
                return List.of(notFoundContainer);
            }
        }

        try {
            if (node.isList()) {
                log.debug("Node is list!");
                final var messages = node.getList(new TypeToken<MessageContainer>() {
                });

                if (messages == null || messages.isEmpty()) {
                    log.debug("Messages are null or empty.");
                    return List.of(notFoundContainer);
                }

                messages.removeIf(container -> container.getText().startsWith("[_SKIP]"));
                return messages;
            }

            log.debug("Node is not a list!");
            final var message = node.get(TypeToken.get(MessageContainer.class));
            if (message == null || message.getText().startsWith("[_SKIP]")) {
                log.debug("Not found any translation!");
                return List.of(notFoundContainer);
            }

            return List.of(message);
        } catch (SerializationException e) {
            return List.of(notFoundContainer);
        }
    }

    public List<MessageContainer> getMessagesWithPrefix(String key) {
        final var messages = getMessages(key);

        messages.forEach(container -> {
            final var text = container.getText();
            final var prefix = getPrefix();

            if (prefix != null && prefix.length() > 0) {
                container.setText(prefix + " " + text);
            }
        });

        return messages;
    }

    public boolean exists(String key) {
        final var node = config.node(key);
        return !node.empty();
    }
}
