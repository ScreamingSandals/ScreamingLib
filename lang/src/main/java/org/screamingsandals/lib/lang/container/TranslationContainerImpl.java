package org.screamingsandals.lib.lang.container;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
class TranslationContainerImpl implements TranslationContainer {
    static final TranslationContainer EMPTY = new TranslationContainerImpl(BasicConfigurationNode.root(), null);

    private ConfigurationNode node;
    @Nullable
    private TranslationContainer fallbackContainer;

    public List<String> translate(Collection<String> key) {
        return translate(key.toArray(String[]::new));
    }

    public List<String> translate(String... key) {
        var node = this.node.node((Object[]) key);
        if (node.isList()) {
            return node.childrenList().stream().map(ConfigurationNode::getString).collect(Collectors.toList());
        }
        if (!node.empty()) {
            return List.of(node.getString(""));
        }
        return fallbackContainer != null ? fallbackContainer.translate(key) : List.of();
    }
}
