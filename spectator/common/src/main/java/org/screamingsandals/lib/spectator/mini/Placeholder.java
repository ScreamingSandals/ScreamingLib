/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.spectator.mini;

import lombok.Data;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.Component;

@Data
public class Placeholder {
    @Pattern("[a-z\\d_-]+")
    @NotNull
    private final String name;
    @NotNull
    private final Component result;

    @NotNull
    public static Placeholder text(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull String text) {
        return new Placeholder(name, Component.text(text));
    }

    @NotNull
    public static Placeholder text(@Pattern("[a-z\\d_-]+") @NotNull String name, byte value) {
        return new Placeholder(name, Component.text(value));
    }

    @NotNull
    public static Placeholder text(@Pattern("[a-z\\d_-]+") @NotNull String name, short value) {
        return new Placeholder(name, Component.text(value));
    }

    @NotNull
    public static Placeholder text(@Pattern("[a-z\\d_-]+") @NotNull String name, int value) {
        return new Placeholder(name, Component.text(value));
    }

    @NotNull
    public static Placeholder text(@Pattern("[a-z\\d_-]+") @NotNull String name, long value) {
        return new Placeholder(name, Component.text(value));
    }

    @NotNull
    public static Placeholder text(@Pattern("[a-z\\d_-]+") @NotNull String name, float value) {
        return new Placeholder(name, Component.text(value));
    }

    @NotNull
    public static Placeholder text(@Pattern("[a-z\\d_-]+") @NotNull String name, double value) {
        return new Placeholder(name, Component.text(value));
    }

    @NotNull
    public static Placeholder text(@Pattern("[a-z\\d_-]+") @NotNull String name, boolean value) {
        return new Placeholder(name, Component.text(value));
    }

    @NotNull
    public static Placeholder text(@Pattern("[a-z\\d_-]+") @NotNull String name, char value) {
        return new Placeholder(name, Component.text(value));
    }

    @NotNull
    public static Placeholder component(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull Component component) {
        return new Placeholder(name, component);
    }
}
