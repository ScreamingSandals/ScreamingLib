package org.screamingsandals.lib.bungee.spectator.audience.adapter;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.Book;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.spectator.audience.MessageType;
import org.screamingsandals.lib.spectator.audience.PlayerAudience;
import org.screamingsandals.lib.spectator.audience.adapter.PlayerAdapter;
import org.screamingsandals.lib.spectator.bossbar.BossBar;
import org.screamingsandals.lib.spectator.sound.SoundStart;
import org.screamingsandals.lib.spectator.sound.SoundStop;
import org.screamingsandals.lib.spectator.title.Title;

import java.util.UUID;

public class BungeePlayerAdapter extends BungeeAdapter implements PlayerAdapter {
    public BungeePlayerAdapter(ProxiedPlayer sender, PlayerAudience owner) {
        super(sender, owner);
    }

    @Override
    public PlayerAudience owner() {
        return (PlayerAudience) super.owner();
    }

    @Override
    public ProxiedPlayer sender() {
        return (ProxiedPlayer) super.sender();
    }

    @Override
    public void sendMessage(@Nullable UUID source, @NotNull ComponentLike message, @NotNull MessageType messageType) {
        var comp = message instanceof AudienceComponentLike ? ((AudienceComponentLike) message).asComponent(owner()) : message.asComponent();
        if (messageType == MessageType.CHAT) {
            if (source == null) {
                sender().sendMessage(comp.as(BaseComponent.class));
            } else {
                sender().sendMessage(source, comp.as(BaseComponent.class));
            }
        } else {
            sender().sendMessage(ChatMessageType.SYSTEM, comp.as(BaseComponent.class));
        }
    }

    @Override
    public void sendActionBar(@NotNull ComponentLike message) {
        var comp = message instanceof AudienceComponentLike ? ((AudienceComponentLike) message).asComponent(owner()) : message.asComponent();
        sender().sendMessage(ChatMessageType.ACTION_BAR, comp.as(BaseComponent.class));
    }

    @Override
    public void sendPlayerListHeaderFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {
        var headerComp = header instanceof AudienceComponentLike ? ((AudienceComponentLike) header).asComponent(owner()) : header.asComponent();
        var footerComp = footer instanceof AudienceComponentLike ? ((AudienceComponentLike) footer).asComponent(owner()) : footer.asComponent();
        sender().setTabHeader(headerComp.as(BaseComponent.class), footerComp.as(BaseComponent.class));

    }

    @Override
    public void showTitle(@NotNull Title title) {
        // TODO
    }

    @Override
    public void clearTitle() {

    }

    @Override
    public void showBossBar(@NotNull BossBar bossBar) {

    }

    @Override
    public void hideBossBar(@NotNull BossBar bossBar) {

    }

    @Override
    public void playSound(@NotNull SoundStart sound) {

    }

    @Override
    public void playSound(@NotNull SoundStart sound, double x, double y, double z) {

    }

    @Override
    public void stopSound(@NotNull SoundStop sound) {

    }

    @Override
    public void openBook(@NotNull Book book) {

    }
}
