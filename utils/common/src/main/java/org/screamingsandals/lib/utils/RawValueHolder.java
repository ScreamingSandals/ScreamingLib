package org.screamingsandals.lib.utils;

import org.screamingsandals.lib.utils.reflect.InvocationResult;

public interface RawValueHolder {
    Object raw();

    default InvocationResult reflect() {
        return new InvocationResult(raw());
    }
}
