package org.screamingsandals.lib.bukkit.packet;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.packet.SPacketPlayOutScoreboardTeam;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Collection;
import java.util.Optional;

public class BukkitSPacketPlayOutScoreboardTeam extends BukkitSPacket implements SPacketPlayOutScoreboardTeam {
    private final Object data;

    public BukkitSPacketPlayOutScoreboardTeam() {
        super(ClassStorage.NMS.PacketPlayOutScoreboardTeam);
        data = Reflect.forceConstruct(ClassStorage.NMS.PacketPlayOutScoreboardTeamData);
    }

    @Override
    public void sendPacket(PlayerWrapper player) {
        packet.setField("k", Optional.of(data));
        super.sendPacket(player);
    }

    @Override
    public void setTeamName(Component teamName) {
        if (teamName == null) {
            throw new UnsupportedOperationException("Team name cannot be null!");
        }
        final var legacyString = AdventureHelper.toLegacy(teamName);

        if (Version.isVersion(1, 17)) {
            packet.setField("i", legacyString);
        } else {
            packet.setField("a", legacyString);
        }
    }

    @Override
    public void setMode(Mode mode) {
        if (mode == null) {
            throw new UnsupportedOperationException("Mode cannot be null!");
        }
        if (Version.isVersion(1, 17)) {
            switch (mode) {
                case CREATE:
                    packet.setField("h", 0);
                    break;
                case REMOVE:
                    packet.setField("h", 1);
                    break;
                case UPDATE:
                    packet.setField("h", 2);
                    break;
                case ADD_ENTITY:
                    packet.setField("h", 3);
                    break;
                case REMOVE_ENTITY:
                    packet.setField("h", 4);
                    break;
            }
        } else {
            packet.setField("i", mode.ordinal());
        }
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
        if (Version.isVersion(1, 17)) {
            Reflect.setField(data, "g", i);
        } else {
            packet.setField("j", i);
        }
    }

    @Override
    public void setTagVisibility(TagVisibility visibility) {
        if (visibility == null) {
            throw new UnsupportedOperationException("Visibility mode cannot be null!");
        }
        if (Version.isVersion(1, 17)) {
            Reflect.setField(data, "d", visibility.getEnumName());
        } else {
            packet.setField("e", visibility.getEnumName());
        }
    }

    @Override
    public void setTeamColor(TextColor color) {
        final var str = NamedTextColor.nearestTo(color).toString();

       if (ClassStorage.NMS.EnumChatFormat != null) {
           final var enumC = Reflect.findEnumConstant(ClassStorage.NMS.EnumChatFormat,str.toUpperCase());
           if (Version.isVersion(1, 17)) {
               Reflect.setField(data, "f",  enumC);
           } else {
               packet.setField("g", enumC);
           }
        } else {
            packet.setField("g", str);
        }
    }

    @Override
    public void setTeamPrefix(Component teamPrefix) {
         if (teamPrefix == null) {
            throw new UnsupportedOperationException("Team prefix cannot be null!");
         }
         final var minecraftComponent = ClassStorage.asMinecraftComponent(teamPrefix);
        if (packet.setField("c", minecraftComponent) == null) {
            if (Version.isVersion(1, 17)) {
                Reflect.setField(data, "b", minecraftComponent);
            } else {
                packet.setField("c", AdventureHelper.toLegacy(teamPrefix));
            }
        }
    }

    @Override
    public void setTeamSuffix(Component teamSuffix) {
        if (teamSuffix == null) {
            throw new UnsupportedOperationException("Team suffix cannot be null!");
        }
        final var minecraftComponent = ClassStorage.asMinecraftComponent(teamSuffix);
        if (Version.isVersion(1, 17)) {
            Reflect.setField(data, "c", minecraftComponent);
        } else {
            packet.setField("d", AdventureHelper.toLegacy(teamSuffix));
        }
    }

    @Override
    public void setEntities(Collection<String> entities) {
        if (entities == null) {
            entities = Lists.newArrayList();
        }
        if (Version.isVersion(1, 17)) {
            packet.setField("j", entities);
        } else {
            packet.setField("h", entities);
        }
    }

    @Override
    public void setDisplayName(Component displayName) {
        if (displayName == null) {
            throw new UnsupportedOperationException("Display name cannot be null!");
        }
        final var minecraftComponent = ClassStorage.asMinecraftComponent(displayName);
        if (packet.setField("b", minecraftComponent) == null) {
            if (Version.isVersion(1, 17)) {
                Reflect.setField(data, "a", minecraftComponent);
            } else {
                packet.setField("b", AdventureHelper.toLegacy(displayName));
            }
        }
    }

    @Override
    public void setCollisionRule(CollisionRule rule) {
        if (rule == null) {
            throw new UnsupportedOperationException("Collision rule cannot be null!");
        }
        if (Version.isVersion(1, 17)) {
            Reflect.setField(data, "e", rule.getEnumName());
        } else {
            packet.setField("f", rule.getEnumName());
        }
    }
}
