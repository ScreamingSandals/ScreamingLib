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

    public boolean is(TypeElement typeElement) {
        if (typeElement == null) {
            return false;
        }
        return types.isAssignable(service.asType(), typeElement.asType()) || (forwardedType != null && types.isSameType(typeElement.asType(), forwardedType.asType()));
    }
}
