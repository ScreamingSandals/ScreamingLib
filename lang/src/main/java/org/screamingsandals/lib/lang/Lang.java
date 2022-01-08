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

package org.screamingsandals.lib.lang;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.lang.container.TranslationContainer;
import org.screamingsandals.lib.sender.CommandSenderWrapper;

/**
 * Access point for default values.
 */
public class Lang {
    /* Package Private, we want this customizable */
    @Getter
    static MiniMessage MINIMESSAGE = MiniMessage.miniMessage();

    @Getter
    private static LangService defaultService;
    @Getter
    @Setter
    private static Component defaultPrefix = Component.empty();

    public static void initDefault(LangService defaultService) {
        if (Lang.defaultService != null) {
            throw new UnsupportedOperationException("Already initialized");
        }
        Lang.defaultService = defaultService;
    }

    public static TranslationContainer getFor(CommandSenderWrapper sender) {
        if (defaultService == null) {
            throw new UnsupportedOperationException("Not initialized yet");
        }
        return defaultService.getFor(sender);
    }

    public static void withParser(@NotNull MiniMessage miniMessage) {
        Lang.MINIMESSAGE = miniMessage;
    }
}
