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

package org.screamingsandals.lib.utils.registry;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@ApiStatus.Experimental // subject to change
public interface RegistryItemStream<T extends RegistryItem> {

    /**
     * Returns a new registry stream consisting of all items that match the given predicate.
     *
     * @param predicate the filtering method
     * @return new registry stream
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull RegistryItemStream<T> filterByLocation(@NotNull Predicate<@NotNull ResourceLocation> predicate);

    /**
     * Returns a new registry stream consisting of all items that match the given pattern.
     *
     * @param locationPattern the filtering pattern
     * @return new registry stream
     */
    @Contract(value = "_ -> new", pure = true)
    default @NotNull RegistryItemStream<T> filterByLocation(@NotNull Pattern locationPattern) {
        return filterByLocation(loc -> locationPattern.matcher(loc.asString()).matches());
    }

    /**
     * Returns a new registry stream consisting of all items that match the given regex.
     *
     * @param regex the filtering regex
     * @return new registry stream
     */
    @Contract(value = "_ -> new", pure = true)
    default @NotNull RegistryItemStream<T> filterByLocation(@NotNull String regex) {
        var locationPattern = Pattern.compile(regex);
        return filterByLocation(loc -> locationPattern.matcher(loc.asString()).matches());
    }

    /**
     * Returns a new registry stream consisting of all items that contains the given literal in path of their resource location.
     * This operation is case in-sensitive.
     *
     * @param literal the required literal
     * @return new registry stream
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull RegistryItemStream<T> filterByLiteralInPath(@NotNull String literal);

    /**
     * Returns a new registry stream consisting of all items that match the given namespace.
     * This operation is case in-sensitive.
     *
     * @param namespace the filtering namespace
     * @return new registry stream
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull RegistryItemStream<T> filterByNamespace(@NotNull String namespace);

    /**
     * Returns a new registry stream consisting of all items that match the given predicate.
     *
     * @param predicate the filtering method
     * @return new registry stream
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull RegistryItemStream<T> filter(@NotNull Predicate<@NotNull T> predicate);

    /**
     * Returns a new registry stream consisting of all items for which {@link ComparableWrapper#is(Object)} returns true.
     *
     * @param object object that represents wrapped type
     * @return new registry stream
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull RegistryItemStream<T> is(@NotNull Object object);
    
    /**
     * Returns a new registry stream consisting of all items for which {@link ComparableWrapper#is(Object...)} returns true.
     *
     * @param objects array of objects that represents wrapped type
     * @return new registry stream
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull RegistryItemStream<T> is(@NotNull Object @NotNull... objects);

    /**
     * Returns filtered items in a list.
     *
     * @return list containing results
     */
    @Contract(value = "-> new", pure = true)
    @NotNull List<@NotNull T> collect();

    /**
     * Returns resource locations of filtered items in a list.
     *
     * @return list containing results
     */
    @Contract(value = "-> new", pure = true)
    @NotNull List<@NotNull ResourceLocation> collectLocations();

    /**
     * Returns filtered items in {@link Stream}.
     *
     * @return stream containing results
     */
    @Contract(value = "-> new", pure = true)
    @NotNull Stream<@NotNull T> javaStream();

    /**
     * Returns resource locations of filtered items in {@link Stream}.
     *
     * @return stream containing results
     */
    @Contract(value = "-> new", pure = true)
    @NotNull Stream<@NotNull ResourceLocation> javaStreamOfLocations();
}
