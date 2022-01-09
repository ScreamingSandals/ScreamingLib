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

package org.screamingsandals.lib.spectator;

import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.List;

public interface Book extends Wrapper, RawValueHolder {
    static Book.Builder builder() {
        return Spectator.getBackend().book();
    }

    Component title();

    Component author();

    List<Component> pages();

    interface Builder {
        Builder title(Component title);

        Builder author(Component author);

        Builder pages(List<Component> pages);

        Builder pages(Component... pages);

        Book build();
    }
}
