package org.screamingsandals.lib.healthindicator;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.data.DataContainer;
import org.screamingsandals.lib.visuals.impl.AbstractVisual;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractHealthIndicator extends AbstractVisual<HealthIndicator> implements HealthIndicator {
    protected final List<PlayerWrapper> trackedPlayers = new LinkedList<>();
    @Getter
    @Setter
    protected DataContainer data;
    protected boolean ready;
    protected Component symbol = Component.empty();
    protected TaskerTask task;

    public AbstractHealthIndicator(UUID uuid) {
        super(uuid);
    }

    @Override
    public HealthIndicator addTrackedPlayer(PlayerWrapper player) {
        if (!trackedPlayers.contains(player)) {
            trackedPlayers.add(player);
            if (visible && ready && task == null) {
                update();
            }
        }
        return this;
    }

    @Override
    public HealthIndicator removeTrackedPlayer(PlayerWrapper player) {
        if (trackedPlayers.contains(player)) {
            trackedPlayers.remove(player);
            if (visible && ready && task == null) {
                update();
            }
        }
        return this;
    }

    @Override
    public HealthIndicator symbol(ComponentLike symbol) {
        return symbol(symbol.asComponent());
    }

    @Override
    public HealthIndicator symbol(Component symbol) {
        this.symbol = symbol;
        if (visible && ready) {
            updateSymbol0();
        }
        return this;
    }

    @Override
    public boolean hasData() {
        if (data == null) {
            return false;
        }

        return !data.isEmpty();
    }

    @Override
    public HealthIndicator update() {
        if (ready) {
            update0();
        }
        return this;
    }

    @Override
    public HealthIndicator show() {
        if (isShown()) {
            return this;
        }

        ready = true;
        visible = true;
        viewers.forEach(a -> onViewerAdded(a, false));
        update();
        return this;
    }

    @Override
    public HealthIndicator hide() {
        if (!isShown()) {
            return this;
        }

        visible = false;
        ready = false;
        update();
        return this;
    }

    @Override
    public void destroy() {
        data = null;
        hide();
        viewers.clear();
        trackedPlayers.clear();
        if (task != null) {
            task.cancel();
            task = null;
        }

        HealthIndicatorManager.removeHealthIndicator(this);
    }

    @Override
    public HealthIndicator startUpdateTask(long time, TaskerTime unit) {
        if (task != null) {
            task.cancel();
        }

        task = Tasker.build(this::update)
                .async()
                .repeat(time, unit)
                .start();

        return this;
    }

    protected abstract void updateSymbol0();
}
