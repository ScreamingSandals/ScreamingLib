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

package org.screamingsandals.lib.spectator.event;

import org.screamingsandals.lib.spectator.Spectator;
import org.screamingsandals.lib.spectator.event.hover.Content;
import org.screamingsandals.lib.spectator.event.hover.ContentLike;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.Wrapper;

public interface HoverEvent extends Wrapper, RawValueHolder {

    static HoverEvent.Builder builder() {
        return Spectator.getBackend().hoverEvent();
    }

    Action action();

    Content content();

    enum Action {
        SHOW_TEXT,
        SHOW_ITEM,
        SHOW_ENTITY
        /* SHOW_ACHIEVEMENT no longer exist */
    }

    interface Builder {
        Builder action(Action action);

        Builder content(Content content);

        default Builder content(ContentLike contentLike) {
            return content(contentLike.asContent());
        }

        HoverEvent build();
    }
}
