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

package org.screamingsandals.lib.annotation.utils;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;
import java.util.LinkedList;
import java.util.List;

@Data
public class ServiceContainer {
    private final Types types;
    @NotNull
    private final TypeElement service;
    @Nullable
    private final TypeElement forwardedType;

    private final List<TypeElement> dependencies = new LinkedList<>();
    private final List<TypeElement> loadAfter = new LinkedList<>();
    private final List<TypeElement> init = new LinkedList<>();
    private final boolean earlyInitialization;
    private final boolean staticOnly;
    private final boolean coreService;

    public boolean is(TypeElement typeElement) {
        if (typeElement == null) {
            return false;
        }
        return types.isAssignable(service.asType(), typeElement.asType()) || (forwardedType != null && types.isSameType(typeElement.asType(), forwardedType.asType()));
    }
}
