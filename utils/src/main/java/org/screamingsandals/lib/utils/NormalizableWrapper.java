package org.screamingsandals.lib.utils;

public interface NormalizableWrapper<N extends NormalizableWrapper<N>> extends Wrapper {
    /**
     * Normalizes the object by removing unsupported fields and converting deprecated.
     *
     * @return normalized object
     */
    N normalize();
}
