package org.screamingsandals.lib.utils.key;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Data
@RequiredArgsConstructor(staticName = "of")
public class NumericMappingKey implements MappingKey {
    private final int number;

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof NumericMappingKey) {
            return number == ((NumericMappingKey) object).number;
        }
        try {
            var number2 = Integer.parseInt(object.toString());
            return number2 == number;
        } catch (NumberFormatException ignored) {}
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    @NotNull
    public String toString() {
        return String.valueOf(number);
    }
}
