/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.impl.utils.registry;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApiStatus.Internal
public final class SimpleRegistryItemStream<O, T extends RegistryItem> implements RegistryItemStream<T> {
    private final @NotNull Supplier<@NotNull Stream<@NotNull O>> streamSupplier;
    private final @NotNull Function<@NotNull O, @NotNull T> converter;
    private final @NotNull Function<@NotNull O, @NotNull ResourceLocation> locationSupplier;
    private final @NotNull BiPredicate<@NotNull O, @NotNull String> literalInLocationPathChecker;
    private final @NotNull BiPredicate<@NotNull O, @NotNull String> namespaceEqualityChecker;
    private final @NotNull List<@NotNull Predicate<O>> operations;

    public SimpleRegistryItemStream(
            @NotNull Supplier<@NotNull Stream<@NotNull O>> streamSupplier,
            @NotNull Function<@NotNull O, @NotNull T> converter,
            @NotNull Function<@NotNull O, @NotNull ResourceLocation> locationSupplier,
            @NotNull BiPredicate<@NotNull O, @NotNull String> literalInLocationPathChecker,
            @NotNull BiPredicate<@NotNull O, @NotNull String> namespaceChecker,
            @NotNull List<@NotNull Predicate<O>> operations
    ) {
        this.streamSupplier = streamSupplier;
        this.converter = converter;
        this.locationSupplier = locationSupplier;
        this.literalInLocationPathChecker = literalInLocationPathChecker;
        this.namespaceEqualityChecker = namespaceChecker;
        this.operations = List.copyOf(operations);
    }

    public static <T extends RegistryItem> @NotNull SimpleRegistryItemStream<?, T> createDummy() {
        return new SimpleRegistryItemStream<>(
                Stream::empty,
                o -> null,
                a -> null,
                (o, literal) -> false,
                (o, namespace) -> false,
                List.of()
        );
    }

    @Override
    public @NotNull RegistryItemStream<T> filterByLocation(@NotNull Predicate<@NotNull ResourceLocation> predicate) {
        return expand(o -> predicate.test(locationSupplier.apply(o)));
    }

    @Override
    public @NotNull RegistryItemStream<T> filterByLiteralInPath(@NotNull String literal) {
        var lowerCaseLiteral = literal.toLowerCase(Locale.ROOT);
        return expand(o -> literalInLocationPathChecker.test(o, lowerCaseLiteral));
    }

    @Override
    public @NotNull RegistryItemStream<T> filterByNamespace(@NotNull String namespace) {
        var lowerCaseNamespace = namespace.toLowerCase(Locale.ROOT);
        return expand(o -> namespaceEqualityChecker.test(o, lowerCaseNamespace));
    }

    @Override
    public @NotNull RegistryItemStream<T> filter(@NotNull Predicate<@NotNull T> predicate) {
        return expand(o -> predicate.test(converter.apply(o)));
    }

    @Override
    public @NotNull RegistryItemStream<T> is(@NotNull Object object) {
        return expand(o -> converter.apply(o).is(object));
    }

    @Override
    public @NotNull RegistryItemStream<T> is(@NotNull Object @NotNull ... objects) {
        return expand(o -> converter.apply(o).is(objects));
    }

    @Override
    public @NotNull List<@NotNull T> collect() {
        return javaStream().collect(Collectors.toList());
    }

    @Override
    public @NotNull List<@NotNull ResourceLocation> collectLocations() {
        return javaStreamOfLocations().collect(Collectors.toList());
    }

    private @NotNull Stream<@NotNull O> filter() {
        var stream = streamSupplier.get();
        for (var filter : operations) {
            stream = stream.filter(filter);
        }
        return stream;
    }

    @Override
    public @NotNull Stream<@NotNull T> javaStream() {
        return filter().map(converter);
    }

    @Override
    public @NotNull Stream<@NotNull ResourceLocation> javaStreamOfLocations() {
        return filter().map(locationSupplier);
    }

    private @NotNull RegistryItemStream<T> expand(@NotNull Predicate<O> operation) {
        var newOperations = new ArrayList<>(operations);
        newOperations.add(operation);
        return new SimpleRegistryItemStream<>(
                streamSupplier,
                converter,
                locationSupplier,
                literalInLocationPathChecker,
                namespaceEqualityChecker,
                newOperations
        );
    }
}
