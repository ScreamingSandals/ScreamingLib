package org.screamingsandals.lib.world.dimension;

import org.jetbrains.annotations.ApiStatus;
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
public abstract class DimensionMapping extends AbstractTypeMapper<DimensionHolder> {
    private static DimensionMapping dimensionMapping;

    protected final BidirectionalConverter<DimensionHolder> dimensionConverter = BidirectionalConverter.<DimensionHolder>build()
            .registerP2W(DimensionHolder.class, d -> d);

    @ApiStatus.Internal
    public DimensionMapping() {
        if (dimensionMapping != null) {
            throw new UnsupportedOperationException("DimensionMapping is already initialized!");
        }
        dimensionMapping = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DIMENSION)
    @OfMethodAlternative(value = DimensionHolder.class, methodName = "ofOptional")
    public static Optional<DimensionHolder> resolve(Object dimension) {
        if (dimensionMapping == null) {
            throw new UnsupportedOperationException("DimensionMapping is not initialized yet.");
        }

        if (dimension == null) {
            return Optional.empty();
        }

        return dimensionMapping.dimensionConverter.convertOptional(dimension).or(() -> dimensionMapping.resolveFromMapping(dimension));
    }

    @OfMethodAlternative(value = DimensionHolder.class, methodName = "all")
    public static List<DimensionHolder> getValues() {
        if (dimensionMapping == null) {
            throw new UnsupportedOperationException("DimensionMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(dimensionMapping.values);
    }

    public static <T> T convertDimensionHolder(DimensionHolder holder, Class<T> newType) {
        if (dimensionMapping == null) {
            throw new UnsupportedOperationException("DimensionMapping is not initialized yet.");
        }
        return dimensionMapping.dimensionConverter.convert(holder, newType);
    }
}
