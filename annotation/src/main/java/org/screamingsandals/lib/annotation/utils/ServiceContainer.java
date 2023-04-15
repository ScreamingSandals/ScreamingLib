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

package org.screamingsandals.lib.annotation.utils;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Data
public class ServiceContainer {
    private final @NotNull Types types;
    private final @NotNull TypeElement service;

    private final @NotNull List<@NotNull TypeElement> dependencies = new LinkedList<>();
    private final @NotNull List<@NotNull TypeElement> loadAfter = new LinkedList<>();
    private final @NotNull List<@NotNull TypeElement> init = new LinkedList<>();
    private final @NotNull Set<@NotNull String> accessedPlugins = new HashSet<>();
    private final boolean earlyInitialization;
    private final boolean staticOnly;
    private final boolean coreService;
    private final boolean provided;
    private boolean delayControllables;

    public static @NotNull ServiceContainer createPluginService(@NotNull Types types, @NotNull TypeElement plugin) {
        var service = new ServiceContainer(types, plugin, false, false, false, false);
        service.delayControllables = true;
        return service;
    }

    public boolean is(@Nullable TypeElement typeElement) {
        if (typeElement == null) {
            return false;
        }
        return types.isAssignable(service.asType(), typeElement.asType());
    }

    public boolean isExactly(@Nullable TypeElement typeElement) {
        if (typeElement == null) {
            return false;
        }
        return types.isSameType(service.asType(), typeElement.asType());
    }
}
