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
import java.util.List;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@Accessors(fluent = true)
@Data
@Builder
public class TextToComponentReplacement {
    private final @NotNull Pattern matchPattern;
    private final @NotNull Function<@NotNull MatchResult, @Nullable Component> replacement;

    public @NotNull Component replace(@NotNull Component component) {
        var originalChildren = component.children();
        component = component.withChildren(List.of());
        if (component instanceof TextComponent) {
            var content = ((TextComponent) component).content();
            if (!content.isEmpty()) {
                var matcher = matchPattern.matcher(content);
                var lastIndex = 0;
                var results = new ArrayList<>();
                while (matcher.find()) {
                    var s = content.substring(lastIndex, matcher.start());
                    Object s2;
                    if (!s.isEmpty()) {
                        if (!results.isEmpty() && (s2 = results.get(results.size() - 1)) instanceof String) {
                            results.set(results.size() - 1, s2 + s);
                        } else {
                            results.add(s);
                        }
                    }

                    var replacement = this.replacement.apply(matcher);
                    if (replacement != null) {
                        results.add(replacement);
                    } else {
                        if (!results.isEmpty() && (s2 = results.get(results.size() - 1)) instanceof String) {
                            results.set(results.size() - 1, s2 + matcher.group());
                        } else {
                            results.add(matcher.group());
                        }
                    }

                    lastIndex = matcher.end();
                }
                if (!results.isEmpty() && !(results.size() == 1 && results.get(0) instanceof String)) {
                    if (lastIndex < content.length()) {
                        var s = content.substring(lastIndex);
                        Object s2;
                        if ((s2 = results.get(results.size() - 1)) instanceof String) {
                            results.set(results.size() - 1, s2 + s);
                        } else {
                            results.add(s);
                        }
                    }
                    component = ((TextComponent) component).withContent("");
                    for (var res : results) {
                        if (component.children().isEmpty() && ((TextComponent) component).content().isEmpty() && res instanceof String) {
                            component = ((TextComponent) component).withContent((String) res);
                        } else if (res instanceof Component) {
                            component = component.withAppendix((Component) res);
                        } else {
                            component = component.withAppendix(res.toString());
                        }
                    }
                }
            }
        } else if (component instanceof TranslatableComponent) {
            var arguments = ((TranslatableComponent) component).args();
            if (!arguments.isEmpty()) {
                var narguments = new ArrayList<Component>(arguments.size());
                for (var arg : arguments) {
                    narguments.add(replace(arg));
                }
                component = ((TranslatableComponent) component).withArgs(narguments);
            }
        }

        if (!originalChildren.isEmpty()) {
            var newChildren = new ArrayList<Component>(originalChildren.size());
            for (var child : originalChildren) {
                newChildren.add(replace(child));
            }
            component = component.withAppendix(newChildren);
        }

        return component;
    }
}
