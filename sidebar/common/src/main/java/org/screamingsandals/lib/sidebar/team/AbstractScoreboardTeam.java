package org.screamingsandals.lib.sidebar.team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.common.packet.SPacketPlayOutScoreboardTeam;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public abstract class AbstractScoreboardTeam implements ScoreboardTeam {
    protected final String identifier;
    protected TextColor color = NamedTextColor.WHITE;
    protected Component displayName = Component.empty();
    protected Component teamPrefix = Component.empty();
    protected Component teamSuffix = Component.empty();
    protected boolean friendlyFire = true;
    protected boolean seeInvisible = true;
    protected SPacketPlayOutScoreboardTeam.TagVisibility nameTagVisibility = SPacketPlayOutScoreboardTeam.TagVisibility.ALWAYS;
    protected SPacketPlayOutScoreboardTeam.CollisionRule collisionRule = SPacketPlayOutScoreboardTeam.CollisionRule.ALWAYS;
    protected final List<PlayerWrapper> players = new LinkedList<>();

    @Override
    public ScoreboardTeam color(TextColor color) {
        this.color = color;
        updateInfo();
        return this;
    }

    @Override
    public ScoreboardTeam displayName(Component displayName) {
        this.displayName = displayName;
        updateInfo();
        return this;
    }

    @Override
    public ScoreboardTeam teamPrefix(Component teamPrefix) {
        this.teamPrefix = teamPrefix;
        updateInfo();
        return this;
    }

    @Override
    public ScoreboardTeam teamSuffix(Component teamSuffix) {
        this.teamSuffix = teamSuffix;
        updateInfo();
        return this;
    }

    @Override
    public ScoreboardTeam friendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
        updateInfo();
        return this;
    }

    @Override
    public ScoreboardTeam seeInvisible(boolean seeInvisible) {
        this.seeInvisible = seeInvisible;
        updateInfo();
        return this;
    }

    @Override
    public ScoreboardTeam nameTagVisibility(SPacketPlayOutScoreboardTeam.TagVisibility nameTagVisibility) {
        this.nameTagVisibility = nameTagVisibility;
        updateInfo();
        return this;
    }

    @Override
    public ScoreboardTeam collisionRule(SPacketPlayOutScoreboardTeam.CollisionRule collisionRule) {
        this.collisionRule = collisionRule;
        updateInfo();
        return this;
    }

    @Override
    public ScoreboardTeam player(PlayerWrapper player) {
        if (!players.contains(player)) {
            players.add(player);
            sendAddPlayer(player);
        }
        return this;
    }

    @Override
    public ScoreboardTeam removePlayer(PlayerWrapper player) {
        if (players.contains(player)) {
            players.remove(player);
            sendRemovePlayer(player);
        }
        return this;
    }

    @Override
    public List<PlayerWrapper> players() {
        return List.copyOf(players);
    }

    @Override
    public @NonNull Iterable<? extends Audience> audiences() {
        return players();
    }

    protected abstract void updateInfo();

    protected abstract void sendAddPlayer(PlayerWrapper player);

    protected abstract void sendRemovePlayer(PlayerWrapper player);
}
