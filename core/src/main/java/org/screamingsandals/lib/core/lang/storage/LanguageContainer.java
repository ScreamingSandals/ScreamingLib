package org.screamingsandals.lib.core.lang.storage;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import lombok.Data;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.screamingsandals.lib.core.config.SConfig;

import java.util.List;

@Data
public class LanguageContainer {
    private final SConfig config;
    private LanguageContainer fallback;
    private String code;
    private String prefix;

    public LanguageContainer(SConfig config, LanguageContainer fallback, String code) {
        this.config = Preconditions.checkNotNull(config, "config");
        this.fallback = fallback;
        this.code = Preconditions.checkNotNull(code, "code");
        this.prefix = Preconditions.checkNotNull(config.get("prefix").getString(), "prefix");
    }

    public List<MessageContainer> getMessages(String key) {
        final var node = config.get(key);
        final var notFoundContainer = new MessageContainer("&cNo translation found for: " + key);
        final var fallbackNode = fallback != null ? fallback.getConfig().get(key) : CommentedConfigurationNode.root();

        if (node.isEmpty() || fallbackNode.isEmpty()) {
            return List.of(notFoundContainer);
        }

        try {
            final var messages = node.getList(TypeToken.of(MessageContainer.class));
            if (messages.size() < 1) {
                return List.of(notFoundContainer);
            }

            messages.removeIf(container -> container.getText().startsWith("[_SKIP]"));

            return messages;
        } catch (ObjectMappingException e) {
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
}
