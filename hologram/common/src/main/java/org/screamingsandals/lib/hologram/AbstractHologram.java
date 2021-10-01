package org.screamingsandals.lib.hologram;

import lombok.Getter;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.utils.data.DataContainer;
import org.screamingsandals.lib.visuals.impl.AbstractLinedVisual;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.UUID;

public abstract class AbstractHologram extends AbstractLinedVisual<Hologram> implements Hologram {
    protected LocationHolder location;
    protected int viewDistance;
    protected boolean touchable;
    protected boolean ready;
    protected DataContainer data;
    protected float rotationIncrement;
    protected Pair<Integer, TaskerTime> rotationTime;
    protected RotationMode rotationMode;
    protected Item item;
    @Getter
    protected ItemPosition itemPosition;
    protected long clickCoolDown;

    protected AbstractHologram(UUID uuid, LocationHolder location, boolean touchable) {
        super(uuid);
        this.location = location;
        this.touchable = touchable;

        //default values
        this.ready = false;
        this.clickCoolDown = DEFAULT_CLICK_COOL_DOWN;
        this.viewDistance = DEFAULT_VIEW_DISTANCE;
        this.rotationIncrement = DEFAULT_ROTATION_INCREMENT;
        this.data = DataContainer.get();
        this.rotationTime = Pair.of(2, TaskerTime.TICKS);
        this.rotationMode = RotationMode.NONE;
        this.itemPosition = ItemPosition.ABOVE;
    }

    @Override
    public int getViewDistance() {
        return viewDistance;
    }

    @Override
    public Hologram setViewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
        return this;
    }

    @Override
    public LocationHolder getLocation() {
        return location;
    }

    @Override
    public Hologram setLocation(LocationHolder location) {
        this.location = location;
        update();
        return this;
    }

    @Override
    public boolean isTouchable() {
        return touchable;
    }

    @Override
    public Hologram setTouchable(boolean touchable) {
        this.touchable = touchable;
        return this;
    }

    @Override
    public Hologram update() {
        if (ready) {
            update0();
        }
        return this;
    }

    @Override
    public Hologram show() {
        if (isShown()) {
            return this;
        }

        ready = true;
        visible = true;
        update();
        return this;
    }

    @Override
    public Hologram hide() {
        if (!isShown()) {
            return this;
        }

        visible = false;
        ready = false;
        update();
        return this;
    }

    @Override
    public DataContainer getData() {
        return data;
    }

    @Override
    public void setData(DataContainer data) {
        this.data = data;
    }

    @Override
    public boolean hasData() {
        if (data == null) {
            return false;
        }

        return !data.isEmpty();
    }

    @Override
    public Pair<Integer, TaskerTime> getRotationTime() {
        return rotationTime;
    }

    @Override
    public Hologram rotationTime(Pair<Integer, TaskerTime> rotatingTime) {
        this.rotationTime = rotatingTime;
        update();
        return this;
    }

    @Override
    public Hologram rotationMode(RotationMode mode) {
        this.rotationMode = mode;
        update();
        return this;
    }

    @Override
    public RotationMode getRotationMode() {
        return rotationMode;
    }

    @Override
    public Hologram item(Item item) {
        this.item = item;
        update();
        return this;
    }

    @Override
    public Hologram rotationIncrement(float toIncrement) {
        this.rotationIncrement = toIncrement;
        return this;
    }

    @Override
    public Hologram itemPosition(ItemPosition location) {
        this.itemPosition = location;
        update();
        return this;
    }

    protected abstract void update0();

    @Override
    public void destroy() {
        data = null;
        hide();
        viewers.clear();
        HologramManager.removeHologram(this);
    }

    @Override
    public long getClickCoolDown() {
        return clickCoolDown;
    }

    @Override
    public Hologram setClickCoolDown(long clickCoolDown) {
        this.clickCoolDown = clickCoolDown;
        return this;
    }
}
