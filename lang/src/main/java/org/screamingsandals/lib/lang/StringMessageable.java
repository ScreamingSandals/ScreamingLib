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

package org.screamingsandals.lib.lang;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor(staticName = "of")
public class StringMessageable implements Messageable {
    private final List<String> keys;
    private final Type type;

    public static StringMessageable of(String message) {
        return of(List.of(message), Type.LEGACY);
    }

    public static StringMessageable of(String... messages) {
        return of(List.of(messages), Type.LEGACY);
    }

    public static StringMessageable of(List<String> messages) {
        return of(messages, Type.LEGACY);
    }

    public static StringMessageable of(String message, Type type) {
        return of(List.of(message), type);
    }

    public static StringMessageable of(Type type, String... messages) {
        return of(List.of(messages), type);
    }

    @Override
    public boolean needsTranslation() {
        return false;
    }
}
