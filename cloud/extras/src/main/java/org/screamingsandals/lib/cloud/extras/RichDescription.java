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

import cloud.commandframework.ArgumentDescription;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;

import static java.util.Objects.requireNonNull;

/**
 * An argument description implementation that uses Spectator components.
 */
public final class RichDescription implements ArgumentDescription {
    private static final @NotNull RichDescription EMPTY = new RichDescription(Component.empty());

    private final @NotNull Component contents;

    RichDescription(final @NotNull Component contents) {
        this.contents = contents;
    }

    /**
     * Get an empty description.
     *
     * @return the empty description
     */
    public static @NotNull RichDescription empty() {
        return EMPTY;
    }

    /**
     * Create a new rich description from the provided component.
     *
     * @param contents the rich contents
     * @return a new rich description
     */
    public static @NotNull RichDescription of(final @NotNull ComponentLike contents) {
        final Component componentContents = requireNonNull(contents, "contents").asComponent();
        if (Component.empty().equals(componentContents)) {
            return EMPTY;
        }

        return new RichDescription(componentContents);
    }

    /* Translatable helper methods */

    /**
     * Create a rich description pointing to a translation key.
     *
     * @param key the translation key
     * @return a new rich description
     */
    public static @NotNull RichDescription translatable(final @NotNull String key) {
        requireNonNull(key, "key");

        return new RichDescription(Component.translatable().translate(key).build());
    }

    /**
     * Create a rich description pointing to a translation key.
     *
     * @param key the translation key
     * @param args the arguments to use with the translation key
     * @return a new rich description
     */
    public static @NotNull RichDescription translatable(final @NotNull String key, final @NotNull ComponentLike @NotNull... args) {
        requireNonNull(key, "key");
        requireNonNull(args, "args");

        return new RichDescription(Component.translatable().translate(key).args(Arrays.stream(args).map(ComponentLike::asComponent).collect(Collectors.toList())).build());
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated to discourage use. A plain serialization is a somewhat expensive and lossy operation, use
     *      {@link #getContents()} instead.
     */
    @Override
    @Deprecated
    public @NotNull String getDescription() {
        return getContents().toPlainText(); // TODO: implement Translator
    }

    /**
     * Get the contents of this description.
     *
     * @return the component contents of this description
     */
    public @NotNull Component getContents() {
        return this.contents;
    }

    @Override
    public boolean isEmpty() {
        return Component.empty().equals(this.contents);
    }

}
