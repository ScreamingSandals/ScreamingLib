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

package org.screamingsandals.lib.proxy.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.event.SCancellableAsyncEvent;
import org.screamingsandals.lib.proxy.PendingConnection;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerLoginEvent implements SCancellableAsyncEvent {
    private final PendingConnection player;
    private Result result = Result.ALLOW;
    private Component cancelMessage = Component.text("Nope.");
    private boolean cancelled;

    @Override
    public boolean cancelled() {
        return cancelled;
    }

    @Override
    public void cancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
