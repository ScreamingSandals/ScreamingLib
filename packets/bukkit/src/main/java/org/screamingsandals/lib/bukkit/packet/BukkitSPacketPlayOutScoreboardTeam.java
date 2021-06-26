package org.screamingsandals.lib.bukkit.packet;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutScoreboardTeam;
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
    public void setFlags(boolean friendlyFire, boolean seeInvisible) {
        int i = 0;
        if (friendlyFire) {
            i |= 1;
        }
        if (seeInvisible) {
            i |= 2;
        }
        packet.setField("j", i);
    }

    @Override
    public void setTagVisibility(TagVisibility visibility) {
        if (visibility == null) {
            throw new UnsupportedOperationException("Visibility mode cannot be null!");
        }
        packet.setField("e", visibility.getEnumName());
    }

    @Override
    public void setTeamColor(TextColor color) {
        if (ClassStorage.NMS.EnumChatFormat != null) {
            packet.setField("g", Reflect.findEnumConstant(ClassStorage.NMS.EnumChatFormat, NamedTextColor.nearestTo(color).toString().toUpperCase()));
        } else {
            packet.setField("g", NamedTextColor.nearestTo(color).toString());
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