package org.screamingsandals.lib.utils;

import java.util.Optional;

public interface Nameable {
    Optional<String> getName();

    void setName(String name);
}
