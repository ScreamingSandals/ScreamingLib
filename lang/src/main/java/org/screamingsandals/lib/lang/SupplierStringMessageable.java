package org.screamingsandals.lib.lang;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Supplier;

@Data
@RequiredArgsConstructor(staticName = "of")
public class SupplierStringMessageable implements Messageable {
    private final Supplier<List<String>> supplier;
    private final Type type;

    public static SupplierStringMessageable of(Supplier<List<String>> message) {
        return of(message, Type.LEGACY);
    }

    @Override
    public List<String> getKeys() {
        return supplier.get();
    }

    @Override
    public boolean needsTranslation() {
        return false;
    }
}
