/*
 * Copyright 2024 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.sidebar;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.packet.AbstractPacket;
import org.screamingsandals.lib.packet.ClientboundSetDisplayObjectivePacket;
import org.screamingsandals.lib.packet.ClientboundSetObjectivePacket;
import org.screamingsandals.lib.packet.ClientboundSetScorePacket;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.sidebar.team.ScoreboardTeam;
import org.screamingsandals.lib.sidebar.team.ScoreboardTeamImpl;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.impl.spectator.StaticAudienceComponentLike;
import org.screamingsandals.lib.utils.data.DataContainer;
import org.screamingsandals.lib.visuals.UpdateStrategy;
import org.screamingsandals.lib.visuals.impl.AbstractVisual;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ScoreSidebarImpl extends AbstractVisual<ScoreSidebar> implements ScoreSidebar {
    @Getter
    protected final @NotNull List<@NotNull ScoreboardTeam> teams = new LinkedList<>();
    @Getter
    protected final @NotNull List<@NotNull ScoreEntry> entries = new CopyOnWriteArrayList<>();
    @Accessors(chain = true, fluent = true)
    @Getter
    @Setter
    protected @Nullable DataContainer data;
    protected boolean ready;
    protected @NotNull AudienceComponentLike title = AudienceComponentLike.empty();
    private final @NotNull String objectiveKey;
    private final @NotNull List<@NotNull ScoreEntry> lines = new CopyOnWriteArrayList<>();

    public ScoreSidebarImpl(@NotNull UUID uuid) {
        super(uuid);
        this.objectiveKey =
                new Random().ints(48, 123)
                        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                        .limit(16)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();
    }

    @Override
    public @Nullable ScoreboardTeam getTeam(@NotNull String identifier) {
        return teams.stream().filter(scoreboardTeam -> identifier.equals(scoreboardTeam.identifier())).findFirst().orElse(null);
    }

    @Contract("_ -> this")
    @Override
    public @NotNull ScoreSidebar removeTeam(@NotNull String identifier) {
        var team = getTeam(identifier);
        if (team != null) {
            this.removeTeam(team);
        }
        return this;
    }

    @Contract("_ -> this")
    @Override
    public @NotNull ScoreSidebar removeTeam(@NotNull ScoreboardTeam scoreboardTeam) {
        scoreboardTeam.destroy();
        teams.remove(scoreboardTeam);
        return this;
    }

    @Override
    public boolean hasData() {
        if (data == null) {
            return false;
        }

        return !data.isEmpty();
    }

    @Contract("_ -> this")
    @Override
    public @NotNull ScoreSidebar title(@NotNull Component title) {
        return title(AudienceComponentLike.of(title));
    }

    @Contract("_ -> this")
    @Override
    public @NotNull ScoreSidebar title(@NotNull ComponentLike title) {
        if (title instanceof AudienceComponentLike) {
            this.title = (AudienceComponentLike) title;
        } else {
            this.title = AudienceComponentLike.of(title);
        }
        updateTitle0();
        return this;
    }

    @Override
    public @NotNull ScoreSidebar entity(@NotNull String identifier, @NotNull Component displayName) {
        entries.stream()
                .filter(entryA -> entryA.getIdentifier().equals(identifier))
                .findFirst()
                .ifPresentOrElse(scoreEntry -> {
                    if (!displayName.equals(scoreEntry.getComponent())) {
                        scoreEntry.setComponent(displayName);
                        scoreEntry.setReloadCache(true);
                        update();
                    }
                }, () -> {
                    entries.add(new ScoreEntry(identifier, displayName));
                    update();
                });
        return this;
    }

    @Override
    public @NotNull ScoreSidebar score(@NotNull String identifier, int score) {
        entries.stream()
                .filter(entryA -> entryA.getIdentifier().equals(identifier))
                .findFirst()
                .ifPresent(scoreEntry -> {
                    scoreEntry.setScore(score);
                    update();
                });
        return this;
    }

    @Override
    public @NotNull ScoreSidebar removeEntity(@NotNull String identifier) {
        entries.stream()
                .filter(entryA -> entryA.getIdentifier().equals(identifier))
                .findFirst()
                .ifPresent(scoreEntry -> {
                    entries.remove(scoreEntry);
                    update();
                });
        return this;
    }

    @Contract("_ -> this")
    @Override
    public @NotNull ScoreSidebar update(@NotNull UpdateStrategy strategy) {
        if (ready) {
            var list = entries
                    .stream()
                    .sorted(Comparator.comparingInt(ScoreEntry::getScore).reversed())
                    .limit(15)
                    .collect(Collectors.toList());

            var packets = new ArrayList<AbstractPacket>();

            lines.stream()
                    .filter(scoreEntry -> !list.contains(scoreEntry))
                    .forEach(scoreEntry -> {
                        lines.remove(scoreEntry);
                        packets.add(destroyScore(scoreEntry.getCache()));
                    });

            list.forEach(scoreEntry -> {
                if (!lines.contains(scoreEntry)) {
                    lines.add(scoreEntry);
                }
                if (scoreEntry.getCache() == null || scoreEntry.isReloadCache()) {
                    if (scoreEntry.getCache() != null) {
                        packets.add(destroyScore(scoreEntry.getCache()));
                    }
                    scoreEntry.setCache(crop(scoreEntry.getComponent().toLegacy()));
                    scoreEntry.setReloadCache(false);
                }
                packets.add(createScorePacket(scoreEntry.getScore(), scoreEntry.getCache()));
            });

            if (visible) {
                packets.forEach(packet -> packet.sendPacket(viewers));
            }

            if (!(this.title instanceof StaticAudienceComponentLike)) {
                updateTitle0();
            }
        }
        return this;
    }

    @Contract("-> this")
    @Override
    public @NotNull ScoreSidebar show() {
        if (shown()) {
            return this;
        }

        ready = true;
        visible = true;
        viewers.forEach(a -> onViewerAdded(a, false));
        update();
        return this;
    }

    @Contract("-> this")
    @Override
    public @NotNull ScoreSidebar hide() {
        if (!shown()) {
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
        SidebarManager.removeSidebar(this);
    }

    @Override
    public void onViewerAdded(@NotNull Player player, boolean checkDistance) {
        if (visible && player.isOnline()) {
            getCreateObjectivePacket(player).sendPacket(player);
            allScores().forEach(packet -> packet.sendPacket(player));
            getDisplayObjectivePacket().sendPacket(player);
            teams.forEach(scoreboardTeam ->
                    ((ScoreboardTeamImpl) scoreboardTeam).constructCreatePacket().sendPacket(player)
            );
        }
    }

    @Override
    public void onViewerRemoved(@NotNull Player player, boolean checkDistance) {
        if (visible && player.isOnline()) {
            teams.forEach(scoreboardTeam ->
                    ((ScoreboardTeamImpl) scoreboardTeam).constructDestructPacket().sendPacket(player)
            );
            getDestroyObjectivePacket().sendPacket(player);
        }
    }

    // INTERNAL METHODS

    private @NotNull ClientboundSetObjectivePacket getCreateObjectivePacket(@NotNull Player player) {
        return notFinalObjectivePacket(player)
                .mode(ClientboundSetObjectivePacket.Mode.CREATE)
                .build();
    }

    private @NotNull ClientboundSetObjectivePacket getUpdateObjectivePacket(@NotNull Player player) {
        return notFinalObjectivePacket(player)
                .mode(ClientboundSetObjectivePacket.Mode.UPDATE)
                .build();
    }

    private @NotNull ClientboundSetObjectivePacket.ClientboundSetObjectivePacketBuilder notFinalObjectivePacket(@NotNull Player player) {
        return ClientboundSetObjectivePacket.builder()
                .objectiveKey(objectiveKey)
                .title(title.asComponent(player))
                .criteriaType(ClientboundSetObjectivePacket.Type.INTEGER);
    }

    private @NotNull ClientboundSetObjectivePacket getDestroyObjectivePacket() {
        return ClientboundSetObjectivePacket.builder()
                .objectiveKey(objectiveKey)
                .mode(ClientboundSetObjectivePacket.Mode.DESTROY)
                .build();
    }

    private @NotNull ClientboundSetDisplayObjectivePacket getDisplayObjectivePacket() {
        return ClientboundSetDisplayObjectivePacket.builder()
                .slot(ClientboundSetDisplayObjectivePacket.DisplaySlot.SIDEBAR)
                .objectiveKey(objectiveKey)
                .build();
    }

    private @NotNull ClientboundSetScorePacket createScorePacket(int i, @NotNull String value) {
        return ClientboundSetScorePacket.builder()
                .entityName(value)
                .objectiveKey(objectiveKey)
                .score(i)
                .action(ClientboundSetScorePacket.ScoreboardAction.CHANGE)
                .build();
    }

    private @NotNull ClientboundSetScorePacket destroyScore(@NotNull String value) {
        return ClientboundSetScorePacket.builder()
                .entityName(value)
                .objectiveKey(objectiveKey)
                .action(ClientboundSetScorePacket.ScoreboardAction.REMOVE)
                .build();
    }

    private @NotNull List<@NotNull ClientboundSetScorePacket> allScores() {
        return lines
                .stream()
                .map(entry -> createScorePacket(entry.getScore(), entry.getCache()))
                .collect(Collectors.toList());
    }

    public @NotNull String crop(@NotNull String baseLine) {
        if (baseLine.length() > 40) {
            return baseLine.substring(0, 40);
        }
        return baseLine;
    }

    @Override
    public @NotNull ScoreboardTeam team(@NotNull String identifier) {
        var team = new ScoreboardTeamImpl(this, identifier);
        teams.add(team);
        if (visible && !viewers.isEmpty()) {
            var packet = team.constructCreatePacket();
            packet.sendPacket(viewers);
        }
        return team;
    }

    protected void updateTitle0() {
        if (visible && !viewers.isEmpty()) {
            viewers.forEach(p -> getUpdateObjectivePacket(p).sendPacket(p));
        }
    }

    @Data
    public static class ScoreEntry {
        private final @NotNull String identifier;
        private @NotNull Component component;
        private int score;
        private @Nullable String cache;
        private boolean reloadCache;
    }
}
