package org.screamingsandals.lib.player.event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class SPlayerDeathEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    private Component deathMessage;
    private List<Item> drops;
    private boolean keepInventory;
    private boolean shouldDropExperience;
    private boolean keepLevel;
    private int newLevel;
    private int newTotalExp;
    private int newExp;
}
