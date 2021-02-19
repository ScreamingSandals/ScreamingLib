package org.screamingsandals.lib.sender;

import org.screamingsandals.lib.utils.Wrapper;

public interface Operator extends Wrapper {
    boolean isOp();

    void setOp(boolean op);
}
