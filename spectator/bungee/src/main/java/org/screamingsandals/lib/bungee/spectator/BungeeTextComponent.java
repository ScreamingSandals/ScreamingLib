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

package org.screamingsandals.lib.bungee.spectator;

import org.screamingsandals.lib.spectator.TextComponent;

public class BungeeTextComponent extends BungeeComponent implements TextComponent {
    protected BungeeTextComponent(net.md_5.bungee.api.chat.TextComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String content() {
        return ((net.md_5.bungee.api.chat.TextComponent) wrappedObject).getText();
    }

    public static class BungeeTextBuilder extends BungeeComponent.BungeeBuilder<
            TextComponent,
            TextComponent.Builder,
            net.md_5.bungee.api.chat.TextComponent
            > implements TextComponent.Builder {

        public BungeeTextBuilder(net.md_5.bungee.api.chat.TextComponent component) {
            super(component);
        }

        @Override
        public TextComponent.Builder content(String content) {
            component.setText(content);
            return self();
        }
    }
}
