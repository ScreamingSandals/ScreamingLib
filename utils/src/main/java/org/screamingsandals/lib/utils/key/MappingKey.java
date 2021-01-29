package org.screamingsandals.lib.utils.key;

public interface MappingKey extends CharSequence {
    default int length() {
        return toString().length();
    }

    default char charAt(int index) {
        return toString().charAt(index);
    }

    default CharSequence subSequence(int start, int end) {
        return toString().subSequence(start, end);
    }
}
