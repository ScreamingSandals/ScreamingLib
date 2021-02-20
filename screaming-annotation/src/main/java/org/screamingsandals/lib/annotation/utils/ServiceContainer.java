package org.screamingsandals.lib.annotation.utils;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.lang.model.element.TypeElement;
import java.util.LinkedList;
import java.util.List;

@Data
public class ServiceContainer {
    @NotNull
    private final TypeElement service;
    @Nullable
    private final TypeElement abstractService;

    private final List<TypeElement> dependencies = new LinkedList<>();
    private final List<TypeElement> loadAfter = new LinkedList<>();
    private final boolean earlyInitialization;

    public boolean is(TypeElement typeElement) {
        if (typeElement == null) {
            return false;
        }
        return typeElement.equals(service) || typeElement.equals(abstractService);
    }
}
