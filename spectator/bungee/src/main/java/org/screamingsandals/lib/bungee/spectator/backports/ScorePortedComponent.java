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
public class ScorePortedComponent extends BasePortedComponent {
    private @NotNull String name;
    private @NotNull String objective;
    private @Nullable String value;

    public ScorePortedComponent(@NotNull ScorePortedComponent component) {
        super(component);
        this.name = component.name;
        this.objective = component.objective;
        this.value = component.value;
    }

    @Override
    public @NotNull ScorePortedComponent duplicate() {
        return new ScorePortedComponent(this);
    }

    @Override
    public @NotNull String toPlainText() {
        return (this.value != null ? this.value : "") + super.toPlainText();
    }

    @Override
    public @NotNull String toLegacyText() {
        return (this.value != null ? this.value : "") + super.toLegacyText();
    }

    @Override
    public void write(@NotNull JsonObject out) {
        var json = new JsonObject();
        json.addProperty("name", this.name);
        json.addProperty("objective", this.objective);
        if (value != null && !value.isEmpty()) {
            json.addProperty("value", this.value);
        }
        out.add("score", json);
    }
}
