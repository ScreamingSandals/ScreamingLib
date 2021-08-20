package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SVillagerReplenishTradeEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<EntityBasic> entity;
    //TODO:
    private final ObjectLink<Object> recipe;
    private final ObjectLink<Integer> bonus;

    public EntityBasic getEntity() {
        return entity.get();
    }

    public Object getRecipe() {
        return recipe.get();
    }

    @Deprecated
    public void setRecipe(Object recipe) {
        this.recipe.set(recipe);
    }

    public int getBonus() {
        return bonus.get();
    }

    public void setBonus(int bonus) {
        this.bonus.set(bonus);
    }
}
