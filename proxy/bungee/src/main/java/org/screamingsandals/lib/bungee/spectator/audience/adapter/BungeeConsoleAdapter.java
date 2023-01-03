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

package org.screamingsandals.lib.bungee.spectator.audience.adapter;

import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.audience.ConsoleAudience;
import org.screamingsandals.lib.spectator.audience.adapter.ConsoleAdapter;

public class BungeeConsoleAdapter extends BungeeAdapter implements ConsoleAdapter {
    public BungeeConsoleAdapter(@NotNull CommandSender sender, @NotNull ConsoleAudience owner) {
        super(sender, owner);
    }

    @Override
    public @NotNull ConsoleAudience owner() {
        return (ConsoleAudience) super.owner();
    }
}
