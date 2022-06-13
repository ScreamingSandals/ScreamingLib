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

package org.screamingsandals.lib.utils;

import org.jetbrains.annotations.Nullable;

public enum TriState {
    TRUE,
    FALSE,
    INITIAL;

    public static TriState fromBoolean(boolean bool) {
        return bool ? TRUE : FALSE;
    }

    public static TriState fromBoolean(@Nullable Boolean bool) {
        return bool == null ? INITIAL : (bool ? TRUE : FALSE);
    }

    @Nullable
    public Boolean toBoxedBoolean() {
        return this == INITIAL ? null : (this == TRUE);
    }
}
