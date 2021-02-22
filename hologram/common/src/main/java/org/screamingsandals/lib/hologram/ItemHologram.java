package org.screamingsandals.lib.hologram;

import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.Pair;

public interface ItemHologram extends Hologram {

    ItemHologram rotating(boolean isRotating);

    boolean isRotating();

    Pair<Integer, TaskerTime> getRotatingTime();

    void setRotatingTime(Pair<Integer, TaskerTime> rotatingTime);

    ItemHologram item(Item item);
}
