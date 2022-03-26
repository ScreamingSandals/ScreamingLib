package org.screamingsandals.lib.minitag.tags;

import lombok.Data;
import lombok.experimental.Accessors;
import org.screamingsandals.lib.minitag.nodes.TagNode;

@Data
@Accessors(fluent = true, chain = true)
public class TransformedTag implements RegisteredTag {
    private final TagType tagType;
    private final Transformer transformer;

    @FunctionalInterface
    public interface Transformer {
        TagNode transform(TagNode node);
    }
}
