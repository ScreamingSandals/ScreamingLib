package org.screamingsandals.lib.bukkit;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.SPacketPlayOutScoreboardTeam;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Collection;

public class BukkitSPacketPlayOutScoreboardTeam extends BukkitSPacket implements SPacketPlayOutScoreboardTeam {
    public BukkitSPacketPlayOutScoreboardTeam() {
        super(ClassStorage.NMS.PacketPlayOutScoreboardTeam);
    }

    @Override
    public void setTeamName(Component teamName) {
        if (teamName == null) {
            throw new UnsupportedOperationException("Team name cannot be null!");
        }
         packet.setField("a", AdventureHelper.toLegacy(teamName));
    }

    @Override
    public void setMode(Mode mode) {
        if (mode == null) {
            throw new UnsupportedOperationException("Mode cannot be null!");
        }
        packet.setField("i", mode.ordinal());
    }

    @Override
    public void setFriendlyFire(int friendlyFireFlag) {
        packet.setField("j", friendlyFireFlag);
    }

    @Override
    public void setTagVisibility(TagVisibility visibility) {
        if (visibility == null) {
            throw new UnsupportedOperationException("Visibility mode cannot be null!");
        }
        packet.setField("e", visibility.getEnumName());
    }

    @Override
    public void setTeamColor(TeamColor color) {
        if (ClassStorage.safeGetClass("{nms}.EnumChatFormat") != null) {
            packet.setField("g", Reflect.getMethod(ClassStorage.NMS.EnumChatFormat, "a", int.class).invokeStatic(color.getColor()));
        } else {
            packet.setField("g", color.getColor());
        }
    }

    @Override
    public void setTeamPrefix(Component teamPrefix) {
         if (teamPrefix == null) {
            throw new UnsupportedOperationException("Team prefix cannot be null!");
         }
        if (packet.setField("c", ClassStorage.asMinecraftComponent(teamPrefix)) == null) {
            packet.setField("c", AdventureHelper.toLegacy(teamPrefix));
        }
    }

    @Override
    public void setTeamSuffix(Component teamSuffix) {
        if (teamSuffix == null) {
            throw new UnsupportedOperationException("Team suffix cannot be null!");
        }
        if (packet.setField("d", ClassStorage.asMinecraftComponent(teamSuffix)) == null) {
            packet.setField("d", AdventureHelper.toLegacy(teamSuffix));
        }
    }

    @Override
    public void setEntities(Collection<String> entities) {
        if (entities == null) {
            entities = Lists.newArrayList();
        }
        packet.setField("h", entities);
    }

    @Override
    public void setDisplayName(Component displayName) {
        if (displayName == null) {
            throw new UnsupportedOperationException("Display name cannot be null!");
        }
        if (packet.setField("b", ClassStorage.asMinecraftComponent(displayName)) == null) {
            packet.setField("b", AdventureHelper.toLegacy(displayName));
        }
    }

    @Override
    public void setCollisionRule(CollisionRule rule) {
        if (rule == null) {
            throw new UnsupportedOperationException("Collision rule cannot be null!");
        }
        packet.setField("f", rule.getEnumName());
    }
}
