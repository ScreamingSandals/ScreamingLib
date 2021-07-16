package org.screamingsandals.lib.bukkit.packet;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.nms.accessors.ChatFormattingAccessor;
import org.screamingsandals.lib.nms.accessors.ClientboundSetPlayerTeamPacketAccessor;
import org.screamingsandals.lib.nms.accessors.ClientboundSetPlayerTeamPacket_i_ParametersAccessor;
import org.screamingsandals.lib.packet.SClientboundSetPlayerTeamPacket;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.lang.ref.Reference;
import java.sql.Ref;
import java.util.Collection;
import java.util.Optional;

public class BukkitSClientboundSetPlayerTeamPacket extends BukkitSPacket implements SClientboundSetPlayerTeamPacket {
    private Object data = null;

    public BukkitSClientboundSetPlayerTeamPacket() {
        super(ClientboundSetPlayerTeamPacketAccessor.getType());
        if (ClientboundSetPlayerTeamPacket_i_ParametersAccessor.getType() != null) {
            data = Reflect.forceConstruct(ClientboundSetPlayerTeamPacket_i_ParametersAccessor.getType());
        }
    }

    @Override
    public void sendPacket(PlayerWrapper player) {
        packet.setField(ClientboundSetPlayerTeamPacketAccessor.getFieldParameters(), Optional.ofNullable(data));
        super.sendPacket(player);
    }

    @Override
    public Object getRawPacket() {
        packet.setField(ClientboundSetPlayerTeamPacketAccessor.getFieldParameters(), Optional.ofNullable(data));
        return super.getRawPacket();
    }

    @Override
    public SClientboundSetPlayerTeamPacket setTeamName(Component teamName) {
        if (teamName == null) {
            throw new UnsupportedOperationException("Team name cannot be null!");
        }

        final var legacyString = AdventureHelper.toLegacy(teamName);
        packet.setField(ClientboundSetPlayerTeamPacketAccessor.getFieldName(), AdventureHelper.toLegacy(teamName));
        return this;
    }

    @Override
    public SClientboundSetPlayerTeamPacket setMode(Mode mode) {
        if (mode == null) {
            throw new UnsupportedOperationException("Mode cannot be null!");
        }

        packet.setField(ClientboundSetPlayerTeamPacketAccessor.getFieldMethod(), mode.ordinal());
        return this;
    }

    @Override
    public SClientboundSetPlayerTeamPacket setFlags(boolean friendlyFire, boolean seeInvisible) {
        int i = 0;
        if (friendlyFire) {
            i |= 1;
        }
        if (seeInvisible) {
            i |= 2;
        }
        if (Version.isVersion(1, 17)) {
            Reflect.setField(data, ClientboundSetPlayerTeamPacket_i_ParametersAccessor.getFieldOptions(), i);
        } else {
            packet.setField(ClientboundSetPlayerTeamPacketAccessor.getFieldField_149315_g(), i);
        }
        return this;
    }

    @Override
    public SClientboundSetPlayerTeamPacket setTagVisibility(TagVisibility visibility) {
        if (visibility == null) {
            throw new UnsupportedOperationException("Visibility mode cannot be null!");
        }

        if (Version.isVersion(1, 17)) {
            Reflect.setField(data, ClientboundSetPlayerTeamPacket_i_ParametersAccessor.getFieldNametagVisibility(), visibility.getEnumName());
        } else {
            packet.setField(ClientboundSetPlayerTeamPacketAccessor.getFieldField_179816_e(), visibility.getEnumName());
        }
        return this;
    }

    @Override
    public SClientboundSetPlayerTeamPacket setTeamColor(TextColor color) {
        final var str = NamedTextColor.nearestTo(color).toString();
        final var enumC = Reflect.findEnumConstant(ChatFormattingAccessor.getType(), str.toUpperCase());
        if (Version.isVersion(1, 17)) {
            Reflect.setField(data, ClientboundSetPlayerTeamPacket_i_ParametersAccessor.getFieldColor(), enumC);
        } else {
            if (packet.setField(ClientboundSetPlayerTeamPacketAccessor.getFieldField_179815_f(), enumC) == null) {
                packet.setField(ClientboundSetPlayerTeamPacketAccessor.getFieldField_179815_f(), Reflect.getField(ChatFormattingAccessor.getType(), ChatFormattingAccessor.getFieldWHITE()));
            }
        }
        return this;
    }

    @Override
    public SClientboundSetPlayerTeamPacket setTeamPrefix(Component teamPrefix) {
        if (teamPrefix == null) {
            throw new UnsupportedOperationException("Team prefix cannot be null!");
        }

        final var minecraftComponent = ClassStorage.asMinecraftComponent(teamPrefix);
        if (Version.isVersion(1, 17)) {
            Reflect.setField(data, "b,f_179353_", minecraftComponent);
            Reflect.setField(data, ClientboundSetPlayerTeamPacket_i_ParametersAccessor.getFieldPlayerPrefix(), minecraftComponent);
        } else {
            if (packet.setField(ClientboundSetPlayerTeamPacketAccessor.getFieldField_207509_c(), minecraftComponent) == null) {
                packet.setField(ClientboundSetPlayerTeamPacketAccessor.getFieldField_207509_c(), AdventureHelper.toLegacy(teamPrefix));
            }
        }
        return this;
    }

    @Override
    public SClientboundSetPlayerTeamPacket setTeamSuffix(Component teamSuffix) {
        if (teamSuffix == null) {
            throw new UnsupportedOperationException("Team suffix cannot be null!");
        }

        final var minecraftComponent = ClassStorage.asMinecraftComponent(teamSuffix);
        if (Version.isVersion(1, 17)) {
            Reflect.setField(data, ClientboundSetPlayerTeamPacket_i_ParametersAccessor.getFieldPlayerSuffix(), minecraftComponent);
        } else {
            if (packet.setField(ClientboundSetPlayerTeamPacketAccessor.getFieldField_207510_d(), AdventureHelper.toLegacy(teamSuffix)) == null) {
                packet.setField(ClientboundSetPlayerTeamPacketAccessor.getFieldField_207510_d(), teamSuffix);
            }
        }
        return this;
    }

    @Override
    public SClientboundSetPlayerTeamPacket setEntities(Collection<String> entities) {
        if (entities == null) {
            entities = Lists.newArrayList();
        }

        packet.setField(ClientboundSetPlayerTeamPacketAccessor.getFieldPlayers(), entities);
        return this;
    }

    @Override
    public SClientboundSetPlayerTeamPacket setDisplayName(Component displayName) {
        if (displayName == null) {
            throw new UnsupportedOperationException("Display name cannot be null!");
        }

        final var minecraftComponent = ClassStorage.asMinecraftComponent(displayName);

        if (Version.isVersion(1, 17)) {
            Reflect.setField(data, ClientboundSetPlayerTeamPacket_i_ParametersAccessor.getFieldDisplayName(), minecraftComponent);
        } else {
            if (packet.setField(ClientboundSetPlayerTeamPacketAccessor.getFieldField_149318_b(), minecraftComponent) == null) {
                packet.setField(ClientboundSetPlayerTeamPacketAccessor.getFieldField_149318_b(), AdventureHelper.toLegacy(displayName));
            }
        }
        return this;
    }

    @Override
    public SClientboundSetPlayerTeamPacket setCollisionRule(CollisionRule rule) {
        if (rule == null) {
            throw new UnsupportedOperationException("Collision rule cannot be null!");
        }

        if (Version.isVersion(1, 17)) {
            Reflect.setField(data, ClientboundSetPlayerTeamPacket_i_ParametersAccessor.getFieldCollisionRule(), rule.getEnumName());
        } else {
            packet.setField(ClientboundSetPlayerTeamPacketAccessor.getFieldField_186976_f(), rule.getEnumName());
        }
        return this;
    }
}
