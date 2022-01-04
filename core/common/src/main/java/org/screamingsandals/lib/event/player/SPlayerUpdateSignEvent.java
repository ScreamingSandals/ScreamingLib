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

package org.screamingsandals.lib.event.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.block.BlockHolder;

public interface SPlayerUpdateSignEvent extends SCancellableEvent, SPlayerEvent, PlatformEventWrapper {

    BlockHolder getBlock();

    Component[] lines();

    Component line(@Range(from = 0, to = 3) int index);

    void line(@Range(from = 0, to = 3) int index, Component component);

    default void line(@Range(from = 0, to = 3) int index, ComponentLike component) {
        line(index, component.asComponent());
    }
}
