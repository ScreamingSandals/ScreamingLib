package org.screamingsandals.lib.bukkit.scoreboard.team;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.bukkit.scoreboard.BukkitScoreboard;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.player.SenderWrapper;
import org.screamingsandals.lib.scoreboard.team.AbstractScoreboardTeam;
import org.screamingsandals.lib.utils.reflect.InvocationResult;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BukkitScoreboardTeam extends AbstractScoreboardTeam {
    private final BukkitScoreboard scoreboard;
    private final String teamKey;

    public BukkitScoreboardTeam(BukkitScoreboard scoreboard, String identifier) {
        super(identifier);
        this.scoreboard = scoreboard;
        this.teamKey = new Random().ints(48, 123)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(16)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    @Override
    protected void updateInfo() {
        if (scoreboard.isShown() && scoreboard.hasViewers()) {
            var packet = packetStart(2);
            packInfo(packet);
            scoreboard.getViewers().forEach(player -> ClassStorage.sendPacket(player.as(Player.class), packet.raw()));
        }
    }

    @Override
    protected void sendAddPlayer(PlayerWrapper player) {
        if (scoreboard.isShown() && scoreboard.hasViewers()) {
            var packet = packetStart(3);
            packPlayers(packet, List.of(player));
            scoreboard.getViewers().forEach(player1 -> ClassStorage.sendPacket(player1.as(Player.class), packet.raw()));
        }
    }

    @Override
    protected void sendRemovePlayer(PlayerWrapper player) {
        if (scoreboard.isShown() && scoreboard.hasViewers()) {
            var packet = packetStart(4);
            packPlayers(packet, List.of(player));
            scoreboard.getViewers().forEach(player1 -> ClassStorage.sendPacket(player1.as(Player.class), packet.raw()));
        }
    }

    @Override
    public void destroy() {
        if (scoreboard.isShown() && scoreboard.hasViewers()) {
            var packet = constructDestructPacket();
            scoreboard.getViewers().forEach(player -> ClassStorage.sendPacket(player.as(Player.class), packet));
        }
    }

    public Object constructDestructPacket() {
        return packetStart(1).raw();
    }

    private InvocationResult packetStart(int mode) {
        var packet = Reflect.constructResulted(ClassStorage.NMS.PacketPlayOutScoreboardTeam);
        packet.setField("a", teamKey);
        packet.setField("i", mode);
        return packet;
    }

    public Object constructCreatePacket() {
        var packet = packetStart(0);
        packInfo(packet);
        packPlayers(packet, players);
        return packet.raw();
    }

    private void packInfo(InvocationResult packet) {
        packet.setField("b", BukkitScoreboard.asMinecraftComponent(displayName));
        int i = 0;
        if (friendlyFire) {
            i |= 1;
        }
        if (seeInvisible) {
            i |= 2;
        }
        packet.setField("j", i);
        packet.setField("e", nameTagVisibility.getIdentifier());
        packet.setField("f", collisionRule.getIdentifier());
        packet.setField("g", Reflect.findEnumConstant(ClassStorage.NMS.EnumChatFormat, NamedTextColor.nearestTo(color).toString().toUpperCase()));
        packet.setField("c", BukkitScoreboard.asMinecraftComponent(teamPrefix));
        packet.setField("d", BukkitScoreboard.asMinecraftComponent(teamSuffix));
    }

    private void packPlayers(InvocationResult packet, List<PlayerWrapper> players) {
        packet.setField("h", players.stream().map(SenderWrapper::getName).collect(Collectors.toList()));
    }
}
