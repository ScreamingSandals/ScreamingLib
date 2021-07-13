package org.screamingsandals.lib.packet;

public interface SPacketPlayOutGameStateChange extends SPacket {

    SPacketPlayOutGameStateChange setReason(int reason);

    SPacketPlayOutGameStateChange setValue(float value);

    // TODO:
    enum Type {
        NO_RESPAWN_BLOCK_AVAILABLE,
        START_RAINING,
        STOP_RAINING,
        CHANGE_GAME_MODE,
        WIN_GAME,
        DEMO_EVENT,
        ARROW_HIT_PLAYER,
        RAIN_LEVEL_CHANGE,
        THUNDER_LEVEL_CHANGE,
        PUFFER_FISH_STING,
        GUARDIAN_ELDER_EFFECT,
        IMMEDIATE_RESPAWN
    }
}
