package org.screamingsandals.lib.core.nms.player;

import lombok.Data;
import lombok.SneakyThrows;

import static org.screamingsandals.lib.core.nms.utils.ClassStorage.NMS.*;

@Data
public class Property {
    private final String name;
    private final String value;
    private final String signature;

    @SneakyThrows
    public Object getHandle() {
        return AuthlibGameProfileProperty.getConstructor(String.class, String.class, String.class).newInstance(name, value, signature);
    }
}
