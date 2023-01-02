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

package org.screamingsandals.lib.item.builder;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.spectator.Component;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@UtilityClass
public class ShortStackDeserializer {
    public static final @NotNull Pattern SHORT_STACK_PATTERN = Pattern.compile("^(?<material>(?:(?!(?<!\\\\)(?:\\\\\\\\)*;).)+)(\\\\*)?(;(?<amount>(?:(?!(?<!\\\\)(?:\\\\\\\\)*;).)+)?(\\\\*)?(;(?<name>(\"((?!(?<!\\\\)(?:\\\\\\\\)*\").)+|(?:(?!(?<!\\\\)(?:\\\\\\\\)*;).)+))?(\\\\*)?(;(?<lore>.*))?)?)?$");
    public static final @NotNull Pattern LORE_SPLIT = Pattern.compile("((\"((?!(?<!\\\\)(?:\\\\\\\\)*\").)+\")|((?!(?<!\\\\)(?:\\\\\\\\)*;).)+)(?=($|;))");

    public static void deserializeShortStack(@NotNull ItemBuilder builder, @Nullable Object shortStackObject) {
        if (shortStackObject instanceof ConfigurationNode) {
            shortStackObject = ((ConfigurationNode) shortStackObject).getString();
        }
        if (!(shortStackObject instanceof String)) {
            var opt = ItemTypeHolder.ofNullable(shortStackObject);
            if (opt != null) {
                builder.type(opt);
                return;
            }
        }
        if (shortStackObject == null) {
            return;
        }

        var shortStack = shortStackObject.toString().trim();
        if (shortStack.startsWith("(cast to ItemStack)")) {
            shortStack = shortStack.substring(19).trim();
        }

        var matcher = SHORT_STACK_PATTERN.matcher(shortStack);

        if (!matcher.matches() || matcher.group("material") == null) {
            return;
        }

        var material = matcher.group("material");
        var amount = matcher.group("amount");
        var name = matcher.group("name");
        if (name != null && name.startsWith("\"") && name.endsWith("\"")) {
            name = name.substring(1, name.length() - 1);
        }
        var loreString = matcher.group("lore");
        var lore = new ArrayList<String>();
        if (loreString != null) {
            Matcher loreMatcher = LORE_SPLIT.matcher(loreString);
            while (loreMatcher.find()) {
                lore.add(loreMatcher.group());
            }
        }

        var materialHolder = ItemTypeHolder.ofNullable(material);
        if (materialHolder == null) {
            return;
        }
        builder.type(materialHolder);
        try {
            if (amount != null && !amount.trim().isEmpty()) {
                builder.amount(Integer.parseInt(amount.trim()));
            }
        } catch (NumberFormatException ignored) {
        }
        if (name != null && !name.trim().isEmpty()) {
            builder.displayName(Component.fromLegacy(name.trim()));
        }
        builder.itemLore(lore.stream().map(Component::fromLegacy).collect(Collectors.toList()));
    }
}
