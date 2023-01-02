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

package org.screamingsandals.lib.spectator.utils;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.TextComponent;
import org.screamingsandals.lib.spectator.TranslatableComponent;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@Accessors(fluent = true)
@Data
@Builder
// TODO: create one that can manipulate with styling
public class SimpleTextReplacement {
    private final @NotNull Pattern matchPattern;
    private final @NotNull Function<@NotNull MatchResult, @Nullable String> replacement;

    public @NotNull Component replace(@NotNull Component component) {
        if (component instanceof TextComponent) {
            var content = ((TextComponent) component).content();
            if (!content.isEmpty()) {
                var matcher = matchPattern.matcher(content);
                var lastIndex = 0;
                var output = new StringBuilder();
                while (matcher.find()) {
                    output.append(content, lastIndex, matcher.start());

                    var replacement = this.replacement.apply(matcher);
                    if (replacement != null) {
                        output.append(replacement);
                    } else {
                        output.append(matcher.group());
                    }

                    lastIndex = matcher.end();
                }
                if (lastIndex < content.length()) {
                    output.append(content, lastIndex, content.length());
                }
                component = ((TextComponent) component).withContent(output.toString());
            }
        } else if (component instanceof TranslatableComponent) {
            var arguments = ((TranslatableComponent) component).args();
            if (!arguments.isEmpty()) {
                var narguments = new ArrayList<Component>();
                for (var arg : arguments) {
                    narguments.add(replace(arg));
                }
                component = ((TranslatableComponent) component).withArgs(narguments);
            }
        }

        var children = component.children();
        if (!children.isEmpty()) {
            var newChildren = new ArrayList<Component>();
            for (var child : children) {
                newChildren.add(replace(child));
            }
            component = component.withChildren(newChildren);
        }

        return component;
    }
}
