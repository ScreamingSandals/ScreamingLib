package org.screamingsandals.lib.utils.key;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor(staticName = "of")
public class ComplexMappingKey implements MappingKey {
    private final List<MappingKey> mappingKeys;

    public static ComplexMappingKey of(MappingKey...keys) {
        return of(Arrays.asList(keys));
    }

    @Override
    @NotNull
    public String toString() {
        return mappingKeys.stream().map(Object::toString).collect(Collectors.joining(":"));
    }
}
