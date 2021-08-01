package org.screamingsandals.lib.healthindicator;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.screamingsandals.lib.entity.EntityHuman;
import org.screamingsandals.lib.packet.AbstractPacket;
import org.screamingsandals.lib.packet.SClientboundSetDisplayObjectivePacket;
import org.screamingsandals.lib.packet.SClientboundSetObjectivePacket;
import org.screamingsandals.lib.packet.SClientboundSetScorePacket;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.data.DataContainer;
import org.screamingsandals.lib.visuals.impl.AbstractVisual;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class HealthIndicatorImpl extends AbstractVisual<HealthIndicator> implements HealthIndicator {
    private final String underNameTagKey;
    private final String tabListKey;
    private final ConcurrentSkipListMap<String, Integer> values = new ConcurrentSkipListMap<>();
    protected final List<PlayerWrapper> trackedPlayers = new LinkedList<>();
    @Getter
    @Setter
    protected DataContainer data;
    protected boolean ready;
    protected boolean healthInTabList;
    protected Component symbol = Component.empty();
    protected TaskerTask task;

    public HealthIndicatorImpl(UUID uuid) {
        super(uuid);
        this.underNameTagKey = generateObjectiveKey();
        this.tabListKey = generateObjectiveKey();
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
    public HealthIndicator showHealthInTabList(boolean flag) {
        this.healthInTabList = flag;
        return this;
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
        values.clear();
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

    protected void updateSymbol0() {
        if (visible) {
            getUpdateObjectivePacket().objectiveKey(underNameTagKey).sendPacket(viewers);
            if (healthInTabList) {
                getUpdateObjectivePacket().objectiveKey(tabListKey).sendPacket(viewers);
            }
        }
    }

    @Override
    public void onViewerAdded(PlayerWrapper player, boolean checkDistance) {
        if (visible) {
            var createObjectivePacket = getCreateObjectivePacket()
                    .objectiveKey(underNameTagKey);

            createObjectivePacket.sendPacket(player);

            var displayObjectivePacket = new SClientboundSetDisplayObjectivePacket()
                    .objectiveKey(underNameTagKey)
                    .slot(SClientboundSetDisplayObjectivePacket.DisplaySlot.BELOW_NAME);

            displayObjectivePacket.sendPacket(player);

            if (healthInTabList) {
                createObjectivePacket.objectiveKey(tabListKey);
                createObjectivePacket.sendPacket(player);

                displayObjectivePacket.objectiveKey(tabListKey).slot(SClientboundSetDisplayObjectivePacket.DisplaySlot.PLAYER_LIST);
                displayObjectivePacket.sendPacket(player);

                values.forEach((s, integer) -> createScorePacket(s, integer).objectiveKey(tabListKey).sendPacket(player));
            }

            values.forEach((s, integer) -> createScorePacket(s, integer).objectiveKey(underNameTagKey).sendPacket(player));
        }
    }

    @Override
    public void onViewerRemoved(PlayerWrapper player, boolean checkDistance) {
        getDestroyObjectivePacket().sendPacket(player);
    }

    @Override
    protected void update0() {
        if (visible) {
            var packets = new ArrayList<AbstractPacket>();

            var trackedPlayers = List.copyOf(this.trackedPlayers);

            List.copyOf(values.keySet()).stream().filter(s -> trackedPlayers.stream().noneMatch(p -> p.getName().equals(s))).forEach(s -> {
                values.remove(s);
                packets.add(getDestroyScorePacket(s).objectiveKey(underNameTagKey));
                if (healthInTabList) {
                    packets.add(getDestroyScorePacket(s).objectiveKey(tabListKey));
                }
            });

            trackedPlayers.forEach(playerWrapper -> {
                if (!playerWrapper.isOnline()) {
                    removeViewer(playerWrapper);
                    return;
                }

                var health = (int) Math.round(playerWrapper.as(EntityHuman.class).getHealth());
                var key = playerWrapper.getName();
                if (!values.containsKey(key) || values.get(key) != health) {
                    values.put(key, health);
                    packets.add(createScorePacket(key, health).objectiveKey(underNameTagKey));
                    if (healthInTabList) {
                        packets.add(createScorePacket(key, health).objectiveKey(tabListKey));
                    }
                }
            });

            packets.forEach(packet -> packet.sendPacket(viewers));
        }
    }

    private SClientboundSetObjectivePacket getNotFinalObjectivePacket() {
        return new SClientboundSetObjectivePacket()
                .title(symbol.asComponent())
                .criteriaType(SClientboundSetObjectivePacket.Type.INTEGER);
    }

    private SClientboundSetObjectivePacket getCreateObjectivePacket() {
        var packet = getNotFinalObjectivePacket();
        packet.mode(SClientboundSetObjectivePacket.Mode.CREATE);
        return packet;
    }

    private SClientboundSetObjectivePacket getUpdateObjectivePacket() {
        var packet = getNotFinalObjectivePacket();
        packet.mode(SClientboundSetObjectivePacket.Mode.UPDATE);
        return packet;
    }

    private SClientboundSetObjectivePacket getDestroyObjectivePacket() {
        return new SClientboundSetObjectivePacket()
                .mode(SClientboundSetObjectivePacket.Mode.DESTROY);
    }

    private SClientboundSetScorePacket createScorePacket(String key, int score) {
        return new SClientboundSetScorePacket()
                .entityName(key)
                .score(score)
                .action(SClientboundSetScorePacket.ScoreboardAction.CHANGE);
    }

    private SClientboundSetScorePacket getDestroyScorePacket(String key) {
        return new SClientboundSetScorePacket()
                .entityName(key)
                .action(SClientboundSetScorePacket.ScoreboardAction.REMOVE);
    }

    private static String generateObjectiveKey() {
        return new Random().ints(48, 123)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(16)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
