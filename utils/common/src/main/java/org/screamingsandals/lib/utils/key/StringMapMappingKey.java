package org.screamingsandals.lib.utils.key;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor(staticName = "of")
public class StringMapMappingKey implements MappingKey {
    private final Map<String, String> str;

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof StringMapMappingKey) {
            return Objects.equals(str, ((StringMapMappingKey) object).str);
        }
        if (object instanceof Map) {
            return Objects.equals(str, object);
        }
        return toString().equalsIgnoreCase(String.valueOf(object));
    }

    @Override
    public int hashCode() {
        return Objects.hash(str);
    }

    @Override
    @NotNull
    public String toString() {
        return '[' + str.entrySet().stream().map(e -> e.getKey().toLowerCase() + "=" + e.getValue().toLowerCase()).collect(Collectors.joining(",")) + ']';
    }
}
