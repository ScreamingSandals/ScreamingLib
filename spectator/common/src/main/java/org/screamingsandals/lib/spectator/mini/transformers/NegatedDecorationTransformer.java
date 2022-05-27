package org.screamingsandals.lib.spectator.mini.transformers;

import org.screamingsandals.lib.minitag.nodes.TagNode;
import org.screamingsandals.lib.minitag.tags.TransformedTag;

import java.util.ArrayList;
import java.util.List;

public class NegatedDecorationTransformer implements TransformedTag.Transformer {
    @Override
    public TagNode transform(TagNode node) {
        if (node.getArgs() != null) {
            var attributes = new ArrayList<>(node.getArgs());
            attributes.add(0, "false");
            return new TagNode(node.getTag().substring(1), List.copyOf(attributes));
        } else {
            return new TagNode(node.getTag().substring(1), List.of("false"));
        }
    }
}
