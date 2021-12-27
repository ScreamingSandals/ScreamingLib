package org.screamingsandals.lib.utils;

public interface ComparableWrapper extends Wrapper {
    /**
     * Compares the wrapper and the object.
     *
     * @param object object that represents wrapped type
     * @return true, if specified object is the same as this
     */
    boolean is(Object object);

    /**
     * Compares the wrapper and the objects.
     *
     * @param objects array of objects that represents wrapped type
     * @return true, if at least one of the specified objects is same as this
     */
    boolean is(Object... objects);
}
