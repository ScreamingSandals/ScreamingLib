package org.screamingsandals.lib.event.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.block.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class SBlockExperienceEvent extends AbstractEvent {
    private final ImmutableObjectLink<BlockHolder> block;
    private final ObjectLink<Integer> experience;

    public BlockHolder getBlock() {
        return block.get();
    }

    public int getExperience() {
        return experience.get();
    }

    public void setExperience(int experience) {
        this.experience.set(experience);
    }
}
