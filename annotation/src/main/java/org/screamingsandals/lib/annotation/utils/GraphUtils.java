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

package org.screamingsandals.lib.annotation.utils;

import com.google.common.graph.Graph;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
@UtilityClass
public class GraphUtils {
    public static <N> @NotNull List<N> sortGraph(@NotNull Graph<N> graph) {
        var sorted = new ArrayList<N>();
        var roots = new ArrayDeque<N>();
        var nonRoots = new HashMap<N, Integer>();

        for (N node : graph.nodes()) {
            int degree = graph.inDegree(node);
            if (degree == 0) {
                roots.add(node);
            } else {
                nonRoots.put(node, degree);
            }
        }

        N next;
        while ((next = roots.poll()) != null) {
            for (N successor : graph.successors(next)) {
                int newInDegree = nonRoots.remove(successor) - 1;

                if (newInDegree == 0) {
                    roots.add(successor);
                } else {
                    nonRoots.put(successor, newInDegree);
                }

            }
            sorted.add(next);
        }

        return sorted;
    }
}
