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

package org.screamingsandals.lib.event;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.Optional;

public interface PlatformEventWrapper extends Wrapper, RawValueHolder {
    @Override
    @ApiStatus.Experimental
    default Object raw() {
        return event();
    }

    @ApiStatus.Experimental
    Object event();

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    @ApiStatus.Experimental
    default <T> T as(Class<T> type) {
        if (type.isInstance(event())) {
            return (T) event();
        }
        throw new UnsupportedOperationException("Can't unwrap wrapper to " + type.getName());
    }

    @Override
    @ApiStatus.Experimental
    default <T> Optional<T> asOptional(Class<T> type) {
        return Wrapper.super.asOptional(type);
    }
}
