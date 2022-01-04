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

package org.screamingsandals.lib.utils.adventure.wrapper;

import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.adventure.ComponentUtils;
import org.screamingsandals.lib.utils.reflect.Reflect;

@Data
public final class ComponentWrapper implements ComponentLike, Wrapper {
    private final Component component;

    @Override
    @NotNull
    public Component asComponent() {
        return component;
    }

    public static ComponentWrapper of(ComponentLike component) {
        return new ComponentWrapper(component.asComponent());
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (String.class.isAssignableFrom(type)) {
            return (T) AdventureHelper.toLegacy(component);
        } else if (type.isInstance(component)) {
            return (T) component;
        }

        var gsonSerializer = Reflect.getClassSafe(type.getPackageName() + ".serializer.gson.GsonComponentSerializer");
        if (gsonSerializer == null) {
            throw new UnsupportedOperationException("Not supported! The target adventure needs to have gson serializer!");
        }

        return (T) ComponentUtils.componentToPlatform(component, Reflect.fastInvoke(gsonSerializer, "gson"));
    }
}
