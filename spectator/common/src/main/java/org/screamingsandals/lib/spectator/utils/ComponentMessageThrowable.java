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

package org.screamingsandals.lib.spectator.utils;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;

public interface ComponentMessageThrowable extends ComponentLike {

    @Nullable
    static Component toComponent(@Nullable Throwable throwable) {
        if (throwable instanceof ComponentMessageThrowable) {
            return ((ComponentMessageThrowable) throwable).componentMessage();
        } else if (throwable != null) {
            final String message = throwable.getMessage();
            if (message != null) {
                return Component.text(message);
            }
        }
        return null;
    }

    @Nullable
    Component componentMessage();

    @Override
    default Component asComponent() {
        var msg = componentMessage();
        return msg == null ? Component.empty() : msg;
    }
}