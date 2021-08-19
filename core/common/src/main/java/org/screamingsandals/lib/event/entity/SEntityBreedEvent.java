package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SEntityBreedEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<EntityBasic> entity;
    private final ImmutableObjectLink<EntityBasic> mother;
    private final ImmutableObjectLink<EntityBasic> father;
    private final ImmutableObjectLink<@Nullable EntityBasic> breeder;
    private final ImmutableObjectLink<@Nullable Item> bredWith;
    private final ObjectLink<Integer> experience;

    public EntityBasic getEntity() {
        return entity.get();
    }

    public EntityBasic getMother() {
        return mother.get();
    }

    public EntityBasic getFather() {
        return father.get();
    }

    @Nullable
    public EntityBasic getBreeder() {
        return breeder.get();
    }

    @Nullable
    public Item getBredWith() {
        return bredWith.get();
    }

    public int getExperience() {
        return experience.get();
    }

    public void setExperience(int experience) {
        this.experience.set(experience);
    }
}
