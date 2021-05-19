package org.screamingsandals.lib.bukkit;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.common.SPacketPlayOutScoreboardTeam;

import java.util.Collection;

//TODO: work on this later
public class BukkitSPacketPlayOutScoreboardTeam implements SPacketPlayOutScoreboardTeam {
    public BukkitSPacketPlayOutScoreboardTeam() {
    }

    @Override
    public void setTeamName(Component teamName) {
        if (teamName == null) {
            throw new UnsupportedOperationException("Team name cannot be null!");
        }
      //  packet.setField("a", AdventureHelper.toLegacy(teamName));
    }

    @Override
    public void setMode(Mode mode) {

    }

    @Override
    public void setFriendlyFire(boolean friendlyFireEnabled) {

    }

    @Override
    public void setTagVisibility(TagVisibility visibility) {

    }

    @Override
    public void setTeamColor(TeamColor color) {

    }

    @Override
    public void setTeamPrefix(Component teamPrefix) {

    }

    @Override
    public void setTeamSuffix(Component teamSuffix) {

    }

    @Override
    public void setEntityCount(int entityCount) {

    }

    @Override
    public void setEntities(Collection<String> entities) {

    }
}
