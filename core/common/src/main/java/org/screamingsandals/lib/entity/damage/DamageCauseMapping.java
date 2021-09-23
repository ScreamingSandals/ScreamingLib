package org.screamingsandals.lib.entity.damage;

import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class DamageCauseMapping extends AbstractTypeMapper<DamageCauseHolder> {
    private static DamageCauseMapping damageCauseMapping;

    protected final BidirectionalConverter<DamageCauseHolder> damageCauseConverter = BidirectionalConverter.<DamageCauseHolder>build()
            .registerP2W(DamageCauseHolder.class, d -> d);

    protected DamageCauseMapping() {
        if (damageCauseMapping != null) {
            throw new UnsupportedOperationException("DamageCauseMapping is already initialized!");
        }
        damageCauseMapping = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DAMAGE_CAUSE)
    @OfMethodAlternative(value = DamageCauseHolder.class, methodName = "ofOptional")
    public static Optional<DamageCauseHolder> resolve(Object damageCause) {
        if (damageCauseMapping == null) {
            throw new UnsupportedOperationException("DamageCauseMapping is not initialized yet.");
        }

        if (damageCause == null) {
            return Optional.empty();
        }

        return damageCauseMapping.damageCauseConverter.convertOptional(damageCause).or(() -> damageCauseMapping.resolveFromMapping(damageCause));
    }

    @OfMethodAlternative(value = DamageCauseHolder.class, methodName = "all")
    public static List<DamageCauseHolder> getValues() {
        if (damageCauseMapping == null) {
            throw new UnsupportedOperationException("DamageCauseMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(damageCauseMapping.values);
    }

    public static <T> T convertDamageCauseHolder(DamageCauseHolder holder, Class<T> newType) {
        if (damageCauseMapping == null) {
            throw new UnsupportedOperationException("DamageCauseMapping is not initialized yet.");
        }
        return damageCauseMapping.damageCauseConverter.convert(holder, newType);
    }
}
