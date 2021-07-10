package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.world.BlockHolder;

import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class SPlayerClickedBlockEvent extends CancellableAbstractEvent {
    private PlayerWrapper player;
    private PlayerWrapper.Hand hand;
    private Action action;
    @Nullable
    private Item item;
    @Nullable
    private BlockHolder block;
    private BlockFace blockFace;

    public enum Action {
        LEFT_CLICK_BLOCK,
        RIGHT_CLICK_BLOCK,
        LEFT_CLICK_AIR,
        RIGHT_CLICK_AIR,
        PHYSICAL;

        public static List<Action> VALUES = Arrays.asList(values());

        public static Action convert(String name) {
            return VALUES.stream()
                    .filter(next -> next.name().equalsIgnoreCase(name))
                    .findFirst()
                    .orElseThrow();
        }
    }

}
