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

package org.screamingsandals.lib.impl.bungee.spectator.audience.adapter;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.Book;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.spectator.TitleableAudienceComponentLike;
import org.screamingsandals.lib.spectator.audience.PlayerAudience;
import org.screamingsandals.lib.spectator.audience.adapter.PlayerAdapter;
import org.screamingsandals.lib.spectator.bossbar.BossBar;
import org.screamingsandals.lib.spectator.sound.SoundStart;
import org.screamingsandals.lib.spectator.sound.SoundStop;
import org.screamingsandals.lib.spectator.title.TimesProvider;
import org.screamingsandals.lib.spectator.title.Title;

public class BungeePlayerAdapter extends BungeeAdapter implements PlayerAdapter {
    public BungeePlayerAdapter(@NotNull ProxiedPlayer sender, @NotNull PlayerAudience owner) {
        super(sender, owner);
    }

    @Override
    public @NotNull PlayerAudience owner() {
        return (PlayerAudience) super.owner();
    }

    @Override
    public @NotNull ProxiedPlayer sender() {
        return (ProxiedPlayer) super.sender();
    }

    @Override
    public void sendMessage(@NotNull ComponentLike message) {
        var comp = message instanceof AudienceComponentLike ? ((AudienceComponentLike) message).asComponent(owner()) : message.asComponent();
        sender().sendMessage(ChatMessageType.SYSTEM, comp.as(BaseComponent.class));
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
        title.as(net.md_5.bungee.api.Title.class).send(sender());
    }

    @Override
    public void showTitle(@NotNull TitleableAudienceComponentLike title, @Nullable TimesProvider times) {
        showTitle(title.asTitle(owner(), times));
    }

    @Override
    public void clearTitle() {
        ProxyServer.getInstance().createTitle().clear().send(sender());
    }

    @Override
    public void showBossBar(@NotNull BossBar bossBar) {
        // TODO
    }

    @Override
    public void hideBossBar(@NotNull BossBar bossBar) {
        // TODO
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
