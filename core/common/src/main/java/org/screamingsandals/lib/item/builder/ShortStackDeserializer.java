package org.screamingsandals.lib.item.builder;

import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ShortStackDeserializer {
    public static final Pattern SHORT_STACK_PATTERN = Pattern.compile("^(?<material>(?:(?!(?<!\\\\)(?:\\\\\\\\)*;).)+)(\\\\*)?(;(?<amount>(?:(?!(?<!\\\\)(?:\\\\\\\\)*;).)+)?(\\\\*)?(;(?<name>(\"((?!(?<!\\\\)(?:\\\\\\\\)*\").)+|(?:(?!(?<!\\\\)(?:\\\\\\\\)*;).)+))?(\\\\*)?(;(?<lore>.*))?)?)?$");
    public static final Pattern LORE_SPLIT = Pattern.compile("((\"((?!(?<!\\\\)(?:\\\\\\\\)*\").)+\")|((?!(?<!\\\\)(?:\\\\\\\\)*;).)+)(?=($|;))");

    public static void deserializeShortStack(ItemBuilder builder, Object shortStackObject) {
        if (shortStackObject instanceof ConfigurationNode) {
            shortStackObject = ((ConfigurationNode) shortStackObject).getString();
        }
        if (!(shortStackObject instanceof String)) {
            var opt = ItemTypeHolder.ofOptional(shortStackObject);
            if (opt.isPresent()) {
                builder.type(opt.get());
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
        var lore_string = matcher.group("lore");
        var lore = new ArrayList<String>();
        if (lore_string != null) {
            Matcher loreMatcher = LORE_SPLIT.matcher(lore_string);
            while (loreMatcher.find()) {
                lore.add(loreMatcher.group());
            }
        }

        var materialHolder = ItemTypeHolder.ofOptional(material);
        if (materialHolder.isEmpty()) {
            return;
        }
        builder.type(materialHolder.get());
        try {
            if (amount != null && !amount.trim().isEmpty()) {
                builder.amount(Integer.parseInt(amount.trim()));
            }
        } catch (NumberFormatException ignored) {
        }
        if (name != null && !name.trim().isEmpty()) {
            builder.displayName(AdventureHelper.toComponent(name.trim()));
        }
        builder.itemLore(lore.stream().map(AdventureHelper::toComponent).collect(Collectors.toList()));
    }
}
