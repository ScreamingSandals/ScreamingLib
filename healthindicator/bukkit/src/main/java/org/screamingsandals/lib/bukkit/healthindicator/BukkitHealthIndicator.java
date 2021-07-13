package org.screamingsandals.lib.bukkit.healthindicator;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.entity.EntityHuman;
import org.screamingsandals.lib.healthindicator.AbstractHealthIndicator;
import org.screamingsandals.lib.packet.*;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

public class BukkitHealthIndicator extends AbstractHealthIndicator {
    private final Component objectiveKey;
    private final ConcurrentSkipListMap<String, Integer> values = new ConcurrentSkipListMap<>();

    public BukkitHealthIndicator(UUID uuid) {
        super(uuid);
        this.objectiveKey = Component.text(
                new Random().ints(48, 123)
                        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                        .limit(16)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString()
        );
    }

    @Override
    public void onViewerAdded(PlayerWrapper player, boolean checkDistance) {
        if (visible) {
            getCreateObjectivePacket().sendPacket(player);
            getDisplayObjectivePacket().sendPacket(player);
            values.forEach((s, integer) -> createScorePacket(s, integer).sendPacket(player));
        }
    }

    @Override
    public void onViewerRemoved(PlayerWrapper player, boolean checkDistance) {
        getDestroyObjectivePacket().sendPacket(player);
    }

    @Override
    protected void update0() {
        if (visible) {
            var packets = new ArrayList<SPacket>();

            var trackedPlayers = List.copyOf(this.trackedPlayers);

            List.copyOf(values.keySet()).stream().filter(s -> trackedPlayers.stream().noneMatch(p -> p.getName().equals(s))).forEach(s -> {
                values.remove(s);
                packets.add(getDestroyScorePacket(s));
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
                    packets.add(createScorePacket(key, health));
                }
            });

            packets.forEach(packet -> packet.sendPacket(viewers));
        }
    }

    @Override
    protected void updateSymbol0() {
        if (visible) {
            var packet = getUpdateObjectivePacket();
            viewers.forEach(player -> ClassStorage.sendPacket(player.as(Player.class), packet));
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        values.clear();
    }

    private SPacketPlayOutScoreboardObjective getNotFinalObjectivePacket() {
        return PacketMapper.createPacket(SPacketPlayOutScoreboardObjective.class)
                .setObjectiveKey(objectiveKey)
                .setTitle(symbol.asComponent())
                .setCriteria(SPacketPlayOutScoreboardObjective.Type.INTEGER);
    }

    private SPacketPlayOutScoreboardObjective getCreateObjectivePacket() {
        var packet = getNotFinalObjectivePacket();
        packet.setMode(SPacketPlayOutScoreboardObjective.Mode.CREATE);
        return packet;
    }

    private SPacketPlayOutScoreboardObjective getUpdateObjectivePacket() {
        var packet = getNotFinalObjectivePacket();
        packet.setMode(SPacketPlayOutScoreboardObjective.Mode.UPDATE);
        return packet;
    }

    private SPacketPlayOutScoreboardObjective getDestroyObjectivePacket() {
        return PacketMapper.createPacket(SPacketPlayOutScoreboardObjective.class)
                .setObjectiveKey(objectiveKey)
                .setMode(SPacketPlayOutScoreboardObjective.Mode.DESTROY);
    }

    private SPacketPlayOutScoreboardDisplayObjective getDisplayObjectivePacket() {
        return PacketMapper.createPacket(SPacketPlayOutScoreboardDisplayObjective.class)
                .setDisplaySlot(SPacketPlayOutScoreboardDisplayObjective.DisplaySlot.BELOW_NAME)
                .setObjectiveKey(objectiveKey);
    }

    private SPacketPlayOutScoreboardScore createScorePacket(String key, int score) {
        return PacketMapper.createPacket(SPacketPlayOutScoreboardScore.class)
                .setValue(Component.text(key))
                .setObjectiveKey(objectiveKey)
                .setScore(score)
                .setAction(SPacketPlayOutScoreboardScore.ScoreboardAction.CHANGE);
    }

    private SPacketPlayOutScoreboardScore getDestroyScorePacket(String key) {
        return PacketMapper.createPacket(SPacketPlayOutScoreboardScore.class)
                .setValue(Component.text(key))
                .setObjectiveKey(objectiveKey)
                .setAction(SPacketPlayOutScoreboardScore.ScoreboardAction.REMOVE);
    }
}
