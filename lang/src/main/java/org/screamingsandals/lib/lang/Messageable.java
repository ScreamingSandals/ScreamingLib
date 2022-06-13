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

import org.screamingsandals.lib.lang.container.TranslationContainer;
import org.screamingsandals.lib.spectator.Component;

import java.util.List;

public interface Messageable {
    boolean needsTranslation();

    /**
     *
     * @return translation keys if needsTranslation() returns true; otherwise list of translated messages
     */
    List<String> getKeys();

    Type getType();

    default Component getFallback() {
        return Component.empty();
    }

    default List<String> translateIfNeeded(TranslationContainer container) {
        if (needsTranslation()) {
            return container.translate(getKeys());
        } else {
            return getKeys();
        }
    }

    enum Type {
        ADVENTURE,
        LEGACY
    }
}
