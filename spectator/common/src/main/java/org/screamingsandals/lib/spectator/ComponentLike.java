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

import org.screamingsandals.lib.spectator.event.hover.Content;
import org.screamingsandals.lib.spectator.event.hover.ContentLike;

public interface ComponentLike extends ContentLike, ComponentBuilderApplicable {
    Component asComponent();

    @Override
    default Content asContent() {
        return asComponent();
    }

    @Override
    default <C extends Component, B extends Component.Builder<B, C>> void apply(B builder) {
        builder.append(this.asComponent());
    }

    @Override
    default Component applyTo(Component component) {
        return component.withAppendix(this.asComponent());
    }
}