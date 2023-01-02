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
    private @Nullable TranslationContainer fallbackContainer;

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

    @Override
    public boolean isEmpty() {
        return node.empty();
    }
}
