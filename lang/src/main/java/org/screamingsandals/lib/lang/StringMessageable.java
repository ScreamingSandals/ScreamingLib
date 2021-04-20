package org.screamingsandals.lib.lang;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor(staticName = "of")
public class StringMessageable implements Messageable {
    private final List<String> keys;
    private final Type type;

    public static StringMessageable of(String message) {
        return of(List.of(message), Type.LEGACY);
    }

    public static StringMessageable of(String... messages) {
        return of(List.of(messages), Type.LEGACY);
    }

    public static StringMessageable of(List<String> messages) {
        return of(messages, Type.LEGACY);
    }

    @Override
    public boolean needsTranslation() {
        return false;
    }
}
