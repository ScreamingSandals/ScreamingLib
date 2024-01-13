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

package org.screamingsandals.lib.utils;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.utils.annotations.ImplicitReceiver;

import java.util.function.Consumer;

@ImplicitReceiver // Kotlin
@FunctionalInterface
public interface ReceiverConsumer<T> extends Consumer<T> {
    @ApiStatus.Internal
    void accept0(T receiver);

    default void accept(T receiver) {
        ConsumerExecutor.setDelegate(this, receiver); // Groovy
        accept0(receiver);
    }
}
