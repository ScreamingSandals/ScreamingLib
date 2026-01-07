/*
 * Copyright 2026 ScreamingSandals
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

package org.screamingsandals.lib.impl.bungee.spectator.dialog;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.dialog.Dialog;

@Data
public class BungeeDialogReference implements Dialog {
    private final @NotNull String reference;

    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        throw new UnsupportedOperationException("Not supported for reference dialogs yet.");
    }
}
