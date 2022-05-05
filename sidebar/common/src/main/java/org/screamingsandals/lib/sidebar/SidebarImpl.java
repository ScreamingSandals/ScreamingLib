/*
 * Copyright 2022 ScreamingSandals
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

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.screamingsandals.lib.packet.*;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.sender.SenderMessage;
import org.screamingsandals.lib.sender.StaticSenderMessage;
import org.screamingsandals.lib.sidebar.team.ScoreboardTeam;
import org.screamingsandals.lib.sidebar.team.ScoreboardTeamImpl;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.data.DataContainer;
import org.screamingsandals.lib.utils.visual.SimpleCLTextEntry;
import org.screamingsandals.lib.visuals.UpdateStrategy;
import org.screamingsandals.lib.visuals.impl.AbstractLinedVisual;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Accessors(chain = true, fluent = true)
public class SidebarImpl extends AbstractLinedVisual<Sidebar> implements Sidebar {
    @Accessors
    @Getter
    protected final List<ScoreboardTeam> teams = new LinkedList<>();
    @Getter
    @Setter
    protected DataContainer data;
    protected boolean ready;
    @Getter
    protected boolean destroyed;
    protected SenderMessage title = SenderMessage.empty();
    private final String objectiveKey;
    private final ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, String>> lines = new ConcurrentSkipListMap<>();

    public SidebarImpl(UUID uuid) {
        super(uuid);
        this.objectiveKey =
                new Random().ints(48, 123)
                        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                        .limit(16)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();
    }

    @Override
    public Optional<ScoreboardTeam> getTeam(String identifier) {
        return teams.stream().filter(scoreboardTeam -> identifier.equals(scoreboardTeam.identifier())).findFirst();
    }

    @Override
    public Sidebar removeTeam(String identifier) {
        getTeam(identifier).ifPresent(this::removeTeam);
        return this;
    }

    @Override
    public Sidebar removeTeam(ScoreboardTeam scoreboardTeam) {
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

    @Override
    public Sidebar title(Component title) {
        return title(SenderMessage.of(title));
    }

    @Override
    public Sidebar title(ComponentLike title) {
        if (title instanceof SenderMessage) {
            this.title = (SenderMessage) title;
        } else {
            this.title = SenderMessage.of(title);
        }
        updateTitle0();
        return this;
    }

    @Override
    public Sidebar update(UpdateStrategy strategy) {
        if (ready) {
            List.copyOf(viewers).forEach(this::updateForPlayer);
        }
        return this;
    }

    @Override
    public Sidebar show() {
        if (shown()) {
            return this;
        }

        ready = true;
        visible = true;
        viewers.forEach(a -> onViewerAdded(a, false));
        update();
        return this;
    }

    @Override
    public Sidebar hide() {
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
    public void onViewerAdded(PlayerWrapper player, boolean checkDistance) {
        if (visible) {
            getCreateObjectivePacket(player).sendPacket(player);
            updateForPlayer(player);
            getDisplayObjectivePacket().sendPacket(player);
            teams.forEach(scoreboardTeam ->
                    ((ScoreboardTeamImpl) scoreboardTeam).constructCreatePacket().sendPacket(player)
            );
        }
    }

    @Override
    public void onViewerRemoved(PlayerWrapper player, boolean checkDistance) {
        if (visible && player.isOnline()) {
            teams.forEach(scoreboardTeam ->
                    ((ScoreboardTeamImpl) scoreboardTeam).constructDestructPacket().sendPacket(player)
            );
            getDestroyObjectivePacket().sendPacket(player);
        }
    }

    @SuppressWarnings("unchecked")
    private void updateForPlayer(PlayerWrapper playerWrapper) {
        var lines = this.lines.get(playerWrapper.getUuid());
        if (lines == null) {
            lines = new ConcurrentSkipListMap<>();
            this.lines.put(playerWrapper.getUuid(), lines);
        }

        var list = lines()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey() >= 0 && entry.getKey() <= 15)
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .map(textEntry -> {
                    if (textEntry instanceof SimpleCLTextEntry) {
                        var like = ((SimpleCLTextEntry) textEntry).getComponentLike();
                        if (like instanceof SenderMessage) {
                            return ((SenderMessage) like).asComponentList(playerWrapper);
                        }
                    }
                    return textEntry.getText();
                })
                .flatMap(o -> {
                    if (o instanceof List) {
                        return ((List<Component>) o).stream();
                    } else {
                        return Stream.of((Component) o);
                    }
                })
                .map(AdventureHelper::toLegacy)
                .collect(Collectors.toList());

        Collections.reverse(list);

        list.replaceAll(toUnique -> makeUnique(toUnique, list));

        var packets = new ArrayList<AbstractPacket>();

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


    public void updateTitle0() {
        if (visible && !viewers.isEmpty()) {
            viewers.forEach(p -> getUpdateObjectivePacket(p).sendPacket(p));
        }
    }
    // INTERNAL METHODS

    private SClientboundSetObjectivePacket getCreateObjectivePacket(PlayerWrapper player) {
        var packet = getNotFinalObjectivePacket(player);
        packet.mode(SClientboundSetObjectivePacket.Mode.CREATE);
        return packet;
    }

    private SClientboundSetObjectivePacket getUpdateObjectivePacket(PlayerWrapper player) {
        var packet = getNotFinalObjectivePacket(player);
        packet.mode(SClientboundSetObjectivePacket.Mode.UPDATE);
        return packet;
    }

    private SClientboundSetObjectivePacket getNotFinalObjectivePacket(PlayerWrapper player) {
        return new SClientboundSetObjectivePacket()
                .objectiveKey(objectiveKey)
                .title(title.asComponent(player))
                .criteriaType(SClientboundSetObjectivePacket.Type.INTEGER);
    }

    private SClientboundSetObjectivePacket getDestroyObjectivePacket() {
        return new SClientboundSetObjectivePacket()
                .objectiveKey(objectiveKey)
                .mode(SClientboundSetObjectivePacket.Mode.DESTROY);
    }

    private SClientboundSetDisplayObjectivePacket getDisplayObjectivePacket() {
        return new SClientboundSetDisplayObjectivePacket()
                .objectiveKey(objectiveKey)
                .slot(SClientboundSetDisplayObjectivePacket.DisplaySlot.SIDEBAR);
    }

    private SClientboundSetScorePacket getCreateScorePacket(int i, String value) {
        return new SClientboundSetScorePacket()
                .entityName(value)
                .objectiveKey(objectiveKey)
                .score(i)
                .action(SClientboundSetScorePacket.ScoreboardAction.CHANGE);
    }

    private SClientboundSetScorePacket destroyScore(String value) {
        return new SClientboundSetScorePacket()
                .entityName(value)
                .objectiveKey(objectiveKey)
                .action(SClientboundSetScorePacket.ScoreboardAction.REMOVE);
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
        var team = new ScoreboardTeamImpl(this, identifier);
        teams.add(team);
        if (visible && !viewers.isEmpty()) {
            var packet = team.constructCreatePacket();
            viewers.forEach(packet::sendPacket);
        }
        return team;
    }
}
