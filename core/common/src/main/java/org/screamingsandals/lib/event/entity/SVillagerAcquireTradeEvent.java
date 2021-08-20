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
public class SVillagerAcquireTradeEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<EntityBasic> entity;
    //TODO: recipes
    private final ObjectLink<Object> recipe;

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
}
