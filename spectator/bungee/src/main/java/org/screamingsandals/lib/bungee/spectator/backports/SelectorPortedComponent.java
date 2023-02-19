/*
 * Copyright 2023 ScreamingSandals
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

package org.screamingsandals.lib.bungee.spectator.backports;

import com.google.gson.JsonObject;
import lombok.*;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiStatus.Internal
public class SelectorPortedComponent extends BasePortedComponent {
    private @NotNull String selector;
    private @Nullable BaseComponent separator;

    public SelectorPortedComponent(@NotNull SelectorPortedComponent component) {
        super(component);
        this.selector = component.selector;
        this.separator = component.separator != null ? component.separator.duplicate() : null;
    }

    @Override
    public @NotNull SelectorPortedComponent duplicate() {
        return new SelectorPortedComponent(this);
    }

    @Override
    public @NotNull String toPlainText() {
        return this.selector + super.toPlainText();
    }

    @Override
    public @NotNull String toLegacyText() {
        return this.selector + super.toLegacyText();
    }

    @Override
    public void write(@NotNull JsonObject out) {
        out.addProperty("selector", this.selector);
        if (this.separator != null) {
            out.addProperty("separator", ComponentSerializer.toString(this.separator));
        }
    }
}
