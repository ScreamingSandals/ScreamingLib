package org.screamingsandals.lib.utils;

import java.io.Serializable;

/**
 * Something that can have a name.
 */
public interface Nameable extends Serializable {

    /**
     * Creates new {@link Nameable}.
     *
     * @param name the name to use
     * @return new Nameable instance.
     */
    static Nameable of(String name) {
        return () -> name;
    }

    /**
     * @return the name
     */
    String name();
}