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

package org.screamingsandals.lib.minitag.nodes;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Accessors(fluent = true)
public abstract class Node {
    private final List<Node> children = new ArrayList<>();

    public void putChildren(@NotNull Node node) {
        children.add(node);
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    @Override
    public String toString() {
        var add = toStringAdditional();
        return this.getClass().getSimpleName().toLowerCase().replace("node", "") + (add != null ? " (" + add + ")" : "") + " {\n" +
                children.stream().map(Node::toString).map(s -> s.replaceAll("(?m)^", "  ")).collect(Collectors.joining(",\n"))
                + "\n}";
    }

    @ApiStatus.OverrideOnly
    @Nullable
    protected String toStringAdditional() {
        return null;
    }
}
