package org.screamingsandals.lib.spectator.mini.transformers;

import lombok.Data;
import org.screamingsandals.lib.minitag.nodes.TagNode;
import org.screamingsandals.lib.minitag.tags.TransformedTag;

import java.util.ArrayList;
import java.util.List;

@Data
public class TagToAttributeTransformer implements TransformedTag.Transformer {
    private final String tag;

    @Override
    public TagNode transform(TagNode node) {
        if (node.getArgs() != null) {
            var attributes = new ArrayList<>(node.getArgs());
            attributes.add(0, node.getTag());
            return new TagNode(tag, List.copyOf(attributes));
        } else {
            return new TagNode(tag, List.of(node.getTag()));
        }
    }
}
