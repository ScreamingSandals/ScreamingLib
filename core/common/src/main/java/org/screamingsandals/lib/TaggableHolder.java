package org.screamingsandals.lib;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.Wrapper;

public interface TaggableHolder extends Wrapper {
    boolean hasTag(@NotNull Object tag);
}
