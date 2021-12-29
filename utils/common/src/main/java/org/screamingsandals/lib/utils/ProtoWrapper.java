package org.screamingsandals.lib.utils;

import com.google.protobuf.Message;

/**
 * A wrapper for .proto conversions.
 * @param <T> .proto type of this class
 */
public interface ProtoWrapper<T extends Message> {

    /**
     * Wraps current wrapper into a .proto class
     * @return .proto class
     */
    T asProto();
}
