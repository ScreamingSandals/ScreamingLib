/*
 * Copyright 2022 ScreamingSandals
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
        if (!node.getArgs().isEmpty()) {
            var attributes = new ArrayList<>(node.getArgs());
            attributes.add(0, node.getTag());
            return new TagNode(tag, List.copyOf(attributes));
        } else {
            return new TagNode(tag, List.of(node.getTag()));
        }
    }
}
