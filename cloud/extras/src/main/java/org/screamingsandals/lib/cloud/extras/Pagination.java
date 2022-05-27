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

//
// MIT License
//
// Copyright (c) 2021 Alexander Söderberg & Contributors
// Copyright (c) 2022 ScreamingSandals
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
package org.screamingsandals.lib.cloud.extras;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.Component;

final class Pagination<T> {

    private final BiFunction<Integer, Integer, List<Component>> headerRenderer;
    private final BiFunction<T, Boolean, Component> rowRenderer;
    private final BiFunction<Integer, Integer, Component> footerRenderer;
    private final BiFunction<Integer, Integer, Component> outOfRangeRenderer;

    Pagination(
            final @NotNull BiFunction<Integer, Integer, List<Component>> headerRenderer,
            final @NotNull BiFunction<T, Boolean, Component> rowRenderer,
            final @NotNull BiFunction<Integer, Integer, Component> footerRenderer,
            final @NotNull BiFunction<Integer, Integer, Component> outOfRangeRenderer
    ) {
        this.headerRenderer = headerRenderer;
        this.rowRenderer = rowRenderer;
        this.footerRenderer = footerRenderer;
        this.outOfRangeRenderer = outOfRangeRenderer;
    }

    @NotNull
    List<Component> render(
            final @NotNull List<T> content,
            final int page,
            final int itemsPerPage
    ) {
        final int pages = (int) Math.ceil(content.size() / (itemsPerPage * 1.00));
        if (page < 1 || page > pages) {
            return Collections.singletonList(this.outOfRangeRenderer.apply(page, pages));
        }

        final List<Component> renderedContent = new ArrayList<>(this.headerRenderer.apply(page, pages));

        final int start = itemsPerPage * (page - 1);
        final int maxIndex = (start + itemsPerPage);
        for (int index = start; index < maxIndex; index++) {
            if (index > content.size() - 1) {
                break;
            }
            renderedContent.add(this.rowRenderer.apply(content.get(index), index == maxIndex - 1));
        }

        renderedContent.add(this.footerRenderer.apply(page, pages));

        return Collections.unmodifiableList(renderedContent);
    }

}
