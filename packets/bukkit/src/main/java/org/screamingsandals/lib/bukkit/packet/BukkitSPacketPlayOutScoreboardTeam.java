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
    private Object data = null;

    public BukkitSPacketPlayOutScoreboardTeam() {
        super(ClassStorage.NMS.PacketPlayOutScoreboardTeam);
        if (ClassStorage.NMS.PacketPlayOutScoreboardTeamData != null) {
            data = Reflect.forceConstruct(ClassStorage.NMS.PacketPlayOutScoreboardTeamData);
        }
    }

    @Override
    public void sendPacket(PlayerWrapper player) {
        packet.setField("k,f_179316_", Optional.ofNullable(data));
        super.sendPacket(player);
    }

    @Override
    public Object getRawPacket() {
        packet.setField("k,f_179316_", Optional.ofNullable(data));
        return super.getRawPacket();
    }

    @Override
    public SPacketPlayOutScoreboardTeam setTeamName(Component teamName) {
        if (teamName == null) {
            throw new UnsupportedOperationException("Team name cannot be null!");
        }

        final var legacyString = AdventureHelper.toLegacy(teamName);
        if (Version.isVersion(1, 17)) {
            packet.setField("i,f_133287_", legacyString);
        } else {
            packet.setField("a,field_149320_a", legacyString);
        }
        return this;
    }

    @Override
    public SPacketPlayOutScoreboardTeam setMode(Mode mode) {
        if (mode == null) {
            throw new UnsupportedOperationException("Mode cannot be null!");
        }

        if (Version.isVersion(1, 17)) {
            packet.setField("h,f_133295_", mode.ordinal());
        } else {
            packet.setField("i,field_149314_f", mode.ordinal());
        }
        return this;
    }

    @Override
    public SPacketPlayOutScoreboardTeam setFlags(boolean friendlyFire, boolean seeInvisible) {
        int i = 0;
        if (friendlyFire) {
            i |= 1;
        }
        if (seeInvisible) {
            i |= 2;
        }
        if (Version.isVersion(1, 17)) {
            Reflect.setField(data, "g,f_179358_", i);
        } else {
            packet.setField("j,field_149315_g", i);
        }
        return this;
    }

    @Override
    public SPacketPlayOutScoreboardTeam setTagVisibility(TagVisibility visibility) {
        if (visibility == null) {
            throw new UnsupportedOperationException("Visibility mode cannot be null!");
        }
        if (Version.isVersion(1, 17)) {
            Reflect.setField(data, "d,f_179355_", visibility.getEnumName());
        } else {
            packet.setField("e,field_179816_e", visibility.getEnumName());
        }
        return this;
    }

    @Override
    public SPacketPlayOutScoreboardTeam setTeamColor(TextColor color) {
        final var str = NamedTextColor.nearestTo(color).toString();

        if (ClassStorage.NMS.EnumChatFormat != null) {
            final var enumC = Reflect.findEnumConstant(ClassStorage.NMS.EnumChatFormat, str.toUpperCase());
            if (Version.isVersion(1, 17)) {
                Reflect.setField(data, "f,f_179357_", enumC);
            } else {
                packet.setField("g,field_179815_f", enumC);
            }
        } else {
            packet.setField("g,field_179815_f", str);
        }
        return this;
    }

    @Override
    public SPacketPlayOutScoreboardTeam setTeamPrefix(Component teamPrefix) {
        if (teamPrefix == null) {
            throw new UnsupportedOperationException("Team prefix cannot be null!");
        }

        final var minecraftComponent = ClassStorage.asMinecraftComponent(teamPrefix);
        if (Version.isVersion(1, 17)) {
            Reflect.setField(data, "b,f_179353_", minecraftComponent);
        } else {
            if (packet.setField("c,field_207509_c", minecraftComponent) == null) {
                packet.setField("c,field_207509_c", AdventureHelper.toLegacy(teamPrefix));
            }
        }
        return this;
    }

    @Override
    public SPacketPlayOutScoreboardTeam setTeamSuffix(Component teamSuffix) {
        if (teamSuffix == null) {
            throw new UnsupportedOperationException("Team suffix cannot be null!");
        }

        final var minecraftComponent = ClassStorage.asMinecraftComponent(teamSuffix);
        if (Version.isVersion(1, 17)) {
            Reflect.setField(data, "c,f_179353_", minecraftComponent);
        } else {
            if (packet.setField("d,field_207510_d", AdventureHelper.toLegacy(teamSuffix)) == null) {
                packet.setField("d,field_207510_d", teamSuffix);
            }
        }
        return this;
    }

    @Override
    public SPacketPlayOutScoreboardTeam setEntities(Collection<String> entities) {
        if (entities == null) {
            entities = Lists.newArrayList();
        }

        if (Version.isVersion(1, 17)) {
            packet.setField("j,f_133294_", entities);
        } else {
            packet.setField("h,field_149317_e", entities);
        }
        return this;
    }

    @Override
    public SPacketPlayOutScoreboardTeam setDisplayName(Component displayName) {
        if (displayName == null) {
            throw new UnsupportedOperationException("Display name cannot be null!");
        }
        final var minecraftComponent = ClassStorage.asMinecraftComponent(displayName);
        if (Version.isVersion(1, 17)) {
            Reflect.setField(data, "a,f_179352_", minecraftComponent);
        } else {
            if (packet.setField("b,field_149318_b", minecraftComponent) == null) {
                packet.setField("b,field_149318_b", AdventureHelper.toLegacy(displayName));
            }
        }
        return this;
    }

    @Override
    public SPacketPlayOutScoreboardTeam setCollisionRule(CollisionRule rule) {
        if (rule == null) {
            throw new UnsupportedOperationException("Collision rule cannot be null!");
        }

        if (Version.isVersion(1, 17)) {
            Reflect.setField(data, "e,f_179356_", rule.getEnumName());
        } else {
            packet.setField("f,field_186976_f", rule.getEnumName());
        }
        return this;
    }
}
