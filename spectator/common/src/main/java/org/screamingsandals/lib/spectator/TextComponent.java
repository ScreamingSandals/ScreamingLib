package org.screamingsandals.lib.spectator;

public interface TextComponent extends Component {
    String content();

    interface Builder extends Component.Builder<Builder, TextComponent> {
        Builder content(String content);
    }
}
