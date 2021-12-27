package org.screamingsandals.lib.item.meta;

import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AbstractService(pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$")
public abstract class PotionMapping extends AbstractTypeMapper<PotionHolder> {
    private static PotionMapping potionMapping;
    protected BidirectionalConverter<PotionHolder> potionConverter = BidirectionalConverter.<PotionHolder>build()
            .registerP2W(PotionHolder.class, e -> e);

    protected PotionMapping() {
        if (potionMapping != null) {
            throw new UnsupportedOperationException("Potion mapping is already initialized.");
        }

        potionMapping = this;
    }

    @OnPostConstruct
    public void bukkit2minecraftMapping() {
        mapAlias("EMPTY", "UNCRAFTABLE");
        mapAlias("LEAPING", "JUMP");
        mapAlias("SWIFTNESS", "SPEED");
        mapAlias("HEALING", "INSTANT_HEAL");
        mapAlias("HARMING", "INSTANT_DAMAGE");
        mapAlias("REGENERATION", "REGEN");
    }

    protected void mapAlias(String potion, String potionBukkit) {
        if (potion == null || potionBukkit == null) {
            throw new IllegalArgumentException("Both potions mustn't be null!");
        }

        super.mapAlias(potion, potionBukkit);
        super.mapAlias("long_" + potion, "long_" + potionBukkit);
        super.mapAlias("strong_" + potion, "strong_" + potionBukkit);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.POTION)
    @OfMethodAlternative(value = PotionHolder.class, methodName = "ofOptional")
    public static Optional<PotionHolder> resolve(Object potionObject) {
        if (potionMapping == null) {
            throw new UnsupportedOperationException("Potion mapping is not initialized yet.");
        }
        if (potionObject == null) {
            return Optional.empty();
        }

        return potionMapping.potionConverter.convertOptional(potionObject).or(() -> potionMapping.resolveFromMapping(potionObject));
    }

    @OfMethodAlternative(value = PotionHolder.class, methodName = "all")
    public static List<PotionHolder> getValues() {
        if (potionMapping == null) {
            throw new UnsupportedOperationException("PotionMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(potionMapping.values);
    }
}
