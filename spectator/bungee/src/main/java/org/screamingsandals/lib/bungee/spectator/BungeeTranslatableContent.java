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

import net.md_5.bungee.api.chat.BaseComponent;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.TranslatableComponent;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BungeeTranslatableContent extends BungeeComponent implements TranslatableComponent {
    protected BungeeTranslatableContent(net.md_5.bungee.api.chat.TranslatableComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String translate() {
        return ((net.md_5.bungee.api.chat.TranslatableComponent) wrappedObject).getTranslate();
    }

    @Override
    public List<Component> args() {
        var with = ((net.md_5.bungee.api.chat.TranslatableComponent) wrappedObject).getWith();
        if (with == null || with.isEmpty()) {
            return List.of();
        }
        return with.stream()
                .map(AbstractBungeeBackend::wrapComponent)
                .collect(Collectors.toList());
    }

    public static class BungeeTranslatableBuilder extends BungeeBuilder<
            TranslatableComponent,
            TranslatableComponent.Builder,
            net.md_5.bungee.api.chat.TranslatableComponent
            > implements TranslatableComponent.Builder {

        public BungeeTranslatableBuilder(net.md_5.bungee.api.chat.TranslatableComponent component) {
            super(component);
        }

        @Override
        public TranslatableComponent.Builder translate(String translate) {
            component.setTranslate(translate);
            return self();
        }

        @Override
        public TranslatableComponent.Builder args(Component... components) {
            component.setWith(Arrays.stream(components).map(component1 -> component1.as(BaseComponent.class)).collect(Collectors.toUnmodifiableList()));
            return self();
        }

        @Override
        public TranslatableComponent.Builder args(Collection<Component> components) {
            component.setWith(components.stream().map(component1 -> component1.as(BaseComponent.class)).collect(Collectors.toUnmodifiableList()));
            return self();
        }
    }
}
