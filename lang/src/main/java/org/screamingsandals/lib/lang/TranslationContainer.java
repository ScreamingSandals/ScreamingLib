package org.screamingsandals.lib.lang;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class TranslationContainer {
    public static final TranslationContainer EMPTY = new TranslationContainer(BasicConfigurationNode.root(), null);

    private ConfigurationNode configurationNode;
    private TranslationContainer fallbackContainer;

    public List<String> translate(Collection<String> key) {
        return translate(key.toArray(String[]::new));
    }

    public List<String> translate(String... key) {
        var node = configurationNode.node((Object[]) key);
        if (node.isList()) {
            return node.childrenList().stream().map(ConfigurationNode::getString).collect(Collectors.toList());
        } else if (node.empty()) {
            return List.of(node.getString(""));
        } else {
            return fallbackContainer != null ? fallbackContainer.translate(key) : List.of();
        }
    }
}
