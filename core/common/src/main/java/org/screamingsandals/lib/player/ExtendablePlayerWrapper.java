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

package org.screamingsandals.lib.player;

import lombok.experimental.Delegate;
import org.screamingsandals.lib.utils.Wrapper;

public class ExtendablePlayerWrapper implements PlayerWrapper {
    @Delegate(types = PlayerWrapper.class, excludes = Wrapper.class)
    private final PlayerWrapper wrappedObject;

    protected ExtendablePlayerWrapper(PlayerWrapper wrappedObject) {
        if (wrappedObject instanceof ExtendablePlayerWrapper) {
            throw new UnsupportedOperationException("ExtendablePlayerWrapper can't wrap another ExtendablePlayerWrapper!");
        }
        this.wrappedObject = wrappedObject;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (type.isInstance(wrappedObject)) {
            return (T) wrappedObject;
        }
        if (type.isInstance(this)) {
            return (T) this;
        }
        return wrappedObject.as(type);
    }
}
