package org.screamingsandals.lib.utils.key;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Data
@RequiredArgsConstructor(staticName = "of")
public class StringMappingKey implements MappingKey {
    private final String str;

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof StringMappingKey) {
            return Objects.equals(str, ((StringMappingKey) object).str);
        }
        return str.equals(String.valueOf(object));
    }

    @Override
    public int hashCode() {
        return Objects.hash(str);
    }

    @Override
    @NotNull
    public String toString() {
        return str;
    }
}
