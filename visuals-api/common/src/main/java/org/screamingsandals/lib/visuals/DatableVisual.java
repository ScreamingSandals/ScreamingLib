package org.screamingsandals.lib.visuals;

import org.screamingsandals.lib.utils.data.DataContainer;

public interface DatableVisual<T> extends Visual<T> {

    DataContainer getData();

    T setData(DataContainer data);

    boolean hasData();
}
