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

package org.screamingsandals.lib.utils;

public enum PlatformType {
    // Java
    BUKKIT,
    MINESTOM,
    SPONGE,
    FABRIC,
    FORGE,
    BUNGEE,
    VELOCITY,
    // Bedrock
    NUKKIT;

    public boolean isProxy() {
        return this == BUNGEE || this == VELOCITY;
    }

    public boolean isServer() {
        return !isProxy();
    }
}
