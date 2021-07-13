package org.screamingsandals.lib.bukkit.sidebar;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.bukkit.sidebar.team.BukkitScoreboardTeam;
import org.screamingsandals.lib.packet.*;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.sender.SenderMessage;
import org.screamingsandals.lib.sender.StaticSenderMessage;
import org.screamingsandals.lib.sidebar.AbstractSidebar;
import org.screamingsandals.lib.sidebar.team.ScoreboardTeam;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.visual.SimpleCLTextEntry;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

public class BukkitSidebar extends AbstractSidebar {
    private final Component objectiveKey;
    private final ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, String>> lines = new ConcurrentSkipListMap<>();

    public BukkitSidebar(UUID uuid) {
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
            var bukkitPlayer = player.as(Player.class);
            getCreateObjectivePacket(player).sendPacket(player);
            updateForPlayer(player);
            getDisplayObjectivePacket().sendPacket(player);
            teams.forEach(scoreboardTeam ->
                    ((BukkitScoreboardTeam) scoreboardTeam).constructCreatePacket().sendPacket(player)
            );
        }
    }

    @Override
    public void onViewerRemoved(PlayerWrapper player, boolean checkDistance) {
        if (visible && player.isOnline()) {
            var bukkitPlayer = player.as(Player.class);
            teams.forEach(scoreboardTeam ->
                    ((BukkitScoreboardTeam) scoreboardTeam).constructDestructPacket().sendPacket(player)
            );
            getDestroyObjectivePacket().sendPacket(player);
        }
    }

    @Override
    protected void update0() {
        List.copyOf(viewers).forEach(this::updateForPlayer);
    }

    private void updateForPlayer(PlayerWrapper playerWrapper) {
        var lines = this.lines.get(playerWrapper.getUuid());
        if (lines == null) {
            lines = new ConcurrentSkipListMap<>();
            this.lines.put(playerWrapper.getUuid(), lines);
        }

        var list = getLines()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey() >= 0 && entry.getKey() <= 15)
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .map(textEntry -> {
                    if (textEntry instanceof SimpleCLTextEntry) {
                        var like = ((SimpleCLTextEntry) textEntry).getComponentLike();
                        if (like instanceof SenderMessage) {
                            return ((SenderMessage) like).asComponent(playerWrapper);
                        }
                    }
                    return textEntry.getText();
                })
                .map(AdventureHelper::toLegacy)
                .collect(Collectors.toList());

        Collections.reverse(list);

        for (var i = 0; i < list.size(); i++) {
            list.set(i, makeUnique(list.get(i), list));
        }

        var packets = new ArrayList<SPacket>();

        if (!(this.title instanceof StaticSenderMessage)) {
            packets.add(getUpdateObjectivePacket(playerWrapper));
        }

        var forRemoval = new ArrayList<Integer>();

        for (var i = 0; i < 15; i++) {
            if (i < list.size()) {
                if (lines.containsKey(i)) {
                    packets.add(destroyScore(lines.get(i)));
                }
                lines.put(i, list.get(i));
                packets.add(getCreateScorePacket(i, list.get(i)));
            } else if (lines.containsKey(i)) {
                packets.add(destroyScore(lines.get(i)));
                forRemoval.add(i);
            }
        }

        forRemoval.forEach(lines::remove);

        packets.forEach(packet -> packet.sendPacket(playerWrapper));
    }

    @Override
    public void updateTitle0() {
        if (visible && !viewers.isEmpty()) {
            viewers.forEach(p -> getUpdateObjectivePacket(p).sendPacket(p));
        }
    }

    // INTERNAL METHODS

    private SPacketPlayOutScoreboardObjective getCreateObjectivePacket(PlayerWrapper player) {
        var packet = getNotFinalObjectivePacket(player);
        packet.setMode(SPacketPlayOutScoreboardObjective.Mode.CREATE);
        return packet;
    }

    private SPacketPlayOutScoreboardObjective getUpdateObjectivePacket(PlayerWrapper player) {
        var packet = getNotFinalObjectivePacket(player);
        packet.setMode(SPacketPlayOutScoreboardObjective.Mode.UPDATE);
        return packet;
    }

    private SPacketPlayOutScoreboardObjective getNotFinalObjectivePacket(PlayerWrapper player) {
        return PacketMapper.createPacket(SPacketPlayOutScoreboardObjective.class)
                .setObjectiveKey(objectiveKey)
                .setTitle(title.asComponent(player))
                .setCriteria(SPacketPlayOutScoreboardObjective.Type.INTEGER);
    }

    private SPacketPlayOutScoreboardObjective getDestroyObjectivePacket() {
        return PacketMapper.createPacket(SPacketPlayOutScoreboardObjective.class)
                .setObjectiveKey(objectiveKey)
                .setMode(SPacketPlayOutScoreboardObjective.Mode.DESTROY);
    }

    private SPacketPlayOutScoreboardDisplayObjective getDisplayObjectivePacket() {
        return PacketMapper.createPacket(SPacketPlayOutScoreboardDisplayObjective.class)
                .setObjectiveKey(objectiveKey)
                .setDisplaySlot(SPacketPlayOutScoreboardDisplayObjective.DisplaySlot.SIDEBAR);
    }

    private SPacketPlayOutScoreboardScore getCreateScorePacket(int i, String value) {
        return PacketMapper.createPacket(SPacketPlayOutScoreboardScore.class)
                .setValue(Component.text(value))
                .setObjectiveKey(objectiveKey)
                .setScore(i)
                .setAction(SPacketPlayOutScoreboardScore.ScoreboardAction.CHANGE);
    }

    private SPacketPlayOutScoreboardScore destroyScore(String value) {
        return PacketMapper.createPacket(SPacketPlayOutScoreboardScore.class)
                .setValue(Component.text(value))
                .setObjectiveKey(objectiveKey)
                .setAction(SPacketPlayOutScoreboardScore.ScoreboardAction.REMOVE);
    }

    public String makeUnique(String toUnique, List<String> from) {
        if (toUnique == null) toUnique = " ";
        final var stringBuilder = new StringBuilder(toUnique);
        while (from.contains(stringBuilder.toString())) {
            stringBuilder.append(" ");
        }

        if (stringBuilder.length() > 40) {
            return stringBuilder.substring(0, 40);
        }
        return stringBuilder.toString();
    }

    @Override
    public ScoreboardTeam team(String identifier) {
        var team = new BukkitScoreboardTeam(this, identifier);
        teams.add(team);
        if (visible && !viewers.isEmpty()) {
            var packet = team.constructCreatePacket();
            viewers.forEach(packet::sendPacket);
        }
        return team;
    }
}
