/*
 * Copyright 2023 ScreamingSandals
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

//
// MIT License
//
// Copyright (c) 2021 Alexander Söderberg & Contributors
// Copyright (c) 2022 ScreamingSandals
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
package org.screamingsandals.lib.cloud.extras;

import cloud.commandframework.CommandManager;
import cloud.commandframework.exceptions.ArgumentParseException;
import cloud.commandframework.exceptions.CommandExecutionException;
import cloud.commandframework.exceptions.InvalidCommandSenderException;
import cloud.commandframework.exceptions.InvalidSyntaxException;
import cloud.commandframework.exceptions.NoPermissionException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.audience.Audience;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.spectator.utils.ComponentMessageThrowable;

/**
 * Exception handler that sends {@link Component} to the sender. All component builders
 * can be overridden and the handled exception types can be configured (see {@link ExceptionType} for types)
 *
 * @param <C> Command sender type
 */
public final class MinecraftExceptionHandler<C> {

    private static final Component NULL = Component.text("null");

    /**
     * Default component builder for {@link InvalidSyntaxException}
     */
    public static final Function<Exception, Component> DEFAULT_INVALID_SYNTAX_FUNCTION =
            e -> Component.text()
                    .content("Invalid command syntax. Correct command syntax is: ")
                    .color(Color.RED)
                    .append(ComponentHelper.highlight(
                            Component.text()
                                    .content(String.format("/%s", ((InvalidSyntaxException) e).getCorrectSyntax()))
                                    .color(Color.GRAY)
                                    .build(),
                            Color.WHITE
                            )
                    )
                    .build();
    /**
     * Default component builder for {@link InvalidCommandSenderException}
     */
    public static final Function<Exception, Component> DEFAULT_INVALID_SENDER_FUNCTION =
            e -> Component.text()
                    .content("Invalid command sender. You must be of type ")
                    .color(Color.RED)
                    .append(
                        Component.text()
                                .content(String.format("/%s", ((InvalidCommandSenderException) e).getRequiredSender().getSimpleName()))
                                .color(Color.GRAY)
                                .build()
                    )
                    .build();
    /**
     * Default component builder for {@link NoPermissionException}
     */
    public static final Function<Exception, Component> DEFAULT_NO_PERMISSION_FUNCTION =
            e -> Component.text()
                    .content("I'm sorry, but you do not have permission to perform this command. " +
                            "\n Please contact the server administrators if you believe that this is in error.")
                    .color(Color.RED)
                    .build();
    /**
     * Default component builder for {@link ArgumentParseException}
     */
    public static final Function<Exception, Component> DEFAULT_ARGUMENT_PARSING_FUNCTION =
            e -> Component.text()
                    .content("Invalid command argument: ")
                    .color(Color.RED)
                    .append(
                            getMessage(e.getCause())
                                    .withColorIfAbsent(Color.GRAY)
                    )
                    .build();
    /**
     * Default component builder for {@link CommandExecutionException}
     *
     */
    public static final Function<Exception, Component> DEFAULT_COMMAND_EXECUTION_FUNCTION =
            e -> {
                final Throwable cause = e.getCause();
                cause.printStackTrace();

                final StringWriter writer = new StringWriter();
                cause.printStackTrace(new PrintWriter(writer));
                final String stackTrace = writer.toString().replaceAll("\t", "    ");
                final HoverEvent hover = HoverEvent.showText(
                        Component.text()
                                .append(getMessage(cause))
                                .append(Component.newLine())
                                .append(Component.text(stackTrace))
                                .append(Component.newLine())
                                .append(Component.text()
                                            .content("    Click to copy")
                                            .color(Color.GRAY)
                                            .italic()
                                            .build()
                                )
                );
                final ClickEvent click = ClickEvent.builder().action(ClickEvent.Action.COPY_TO_CLIPBOARD).value(stackTrace).build();
                return Component.text()
                        .content("An internal error occurred while attempting to perform this command.")
                        .color(Color.RED)
                        .hoverEvent(hover)
                        .clickEvent(click)
                        .build();
            };

    private final Map<ExceptionType, BiFunction<C, Exception, Component>> componentBuilders = new HashMap<>();
    private Function<Component, Component> decorator = Function.identity();

    /**
     * Use the default invalid syntax handler
     *
     * @return {@code this}
     */
    @NotNull
    public MinecraftExceptionHandler<C> withInvalidSyntaxHandler() {
        return this.withHandler(ExceptionType.INVALID_SYNTAX, DEFAULT_INVALID_SYNTAX_FUNCTION);
    }

    /**
     * Use the default invalid sender handler
     *
     * @return {@code this}
     */
    @NotNull
    public MinecraftExceptionHandler<C> withInvalidSenderHandler() {
        return this.withHandler(ExceptionType.INVALID_SENDER, DEFAULT_INVALID_SENDER_FUNCTION);
    }

    /**
     * Use the default no permission handler
     *
     * @return {@code this}
     */
    @NotNull
    public MinecraftExceptionHandler<C> withNoPermissionHandler() {
        return this.withHandler(ExceptionType.NO_PERMISSION, DEFAULT_NO_PERMISSION_FUNCTION);
    }

    /**
     * Use the default argument parsing handler
     *
     * @return {@code this}
     */
    @NotNull
    public MinecraftExceptionHandler<C> withArgumentParsingHandler() {
        return this.withHandler(ExceptionType.ARGUMENT_PARSING, DEFAULT_ARGUMENT_PARSING_FUNCTION);
    }

    /**
     * Use the default {@link CommandExecutionException} handler
     *
     * @return {@code this}
     */
    @NotNull
    public MinecraftExceptionHandler<C> withCommandExecutionHandler() {
        return this.withHandler(ExceptionType.COMMAND_EXECUTION, DEFAULT_COMMAND_EXECUTION_FUNCTION);
    }

    /**
     * Use all of the default exception handlers
     *
     * @return {@code this}
     */
    @NotNull
    public MinecraftExceptionHandler<C> withDefaultHandlers() {
        return this
                .withArgumentParsingHandler()
                .withInvalidSenderHandler()
                .withInvalidSyntaxHandler()
                .withNoPermissionHandler()
                .withCommandExecutionHandler();
    }

    /**
     * Specify an exception handler
     *
     * @param type             Exception type
     * @param componentBuilder Component builder
     * @return {@code this}
     */
    @NotNull
    public MinecraftExceptionHandler<C> withHandler(final @NotNull ExceptionType type, final @NotNull Function<@NotNull Exception, @NotNull Component> componentBuilder) {
        return this.withHandler(
                type,
                (sender, exception) -> componentBuilder.apply(exception)
        );
    }

    /**
     * Specify an exception handler
     *
     * @param type             Exception type
     * @param componentBuilder Component builder
     * @return {@code this}
     */
    @NotNull
    public MinecraftExceptionHandler<C> withHandler(final @NotNull ExceptionType type, final @NotNull BiFunction<@NotNull C, @NotNull Exception, @NotNull Component> componentBuilder) {
        this.componentBuilders.put(
                type,
                componentBuilder
        );
        return this;
    }

    /**
     * Specify a decorator that acts on a component before it's sent to the sender
     *
     * @param decorator Component decorator
     * @return {@code this}
     */
    @NotNull
    public MinecraftExceptionHandler<C> withDecorator(final @NotNull Function<@NotNull Component, @NotNull Component> decorator) {
        this.decorator = decorator;
        return this;
    }

    /**
     * Register the exception handlers in the manager
     *
     * @param manager        Manager instance
     * @param audienceMapper Mapper that maps command sender to audience instances
     */
    public void apply(final @NotNull CommandManager<C> manager, final @NotNull Function<@NotNull C, @NotNull Audience> audienceMapper) {
        if (this.componentBuilders.containsKey(ExceptionType.INVALID_SYNTAX)) {
            manager.registerExceptionHandler(
                    InvalidSyntaxException.class,
                    (c, e) -> audienceMapper.apply(c).sendMessage(
                            this.decorator.apply(this.componentBuilders.get(ExceptionType.INVALID_SYNTAX).apply(c, e))
                    )
            );
        }
        if (this.componentBuilders.containsKey(ExceptionType.INVALID_SENDER)) {
            manager.registerExceptionHandler(
                    InvalidCommandSenderException.class,
                    (c, e) -> audienceMapper.apply(c).sendMessage(
                            this.decorator.apply(this.componentBuilders.get(ExceptionType.INVALID_SENDER).apply(c, e))
                    )
            );
        }
        if (this.componentBuilders.containsKey(ExceptionType.NO_PERMISSION)) {
            manager.registerExceptionHandler(
                    NoPermissionException.class,
                    (c, e) -> audienceMapper.apply(c).sendMessage(
                            this.decorator.apply(this.componentBuilders.get(ExceptionType.NO_PERMISSION).apply(c, e))
                    )
            );
        }
        if (this.componentBuilders.containsKey(ExceptionType.ARGUMENT_PARSING)) {
            manager.registerExceptionHandler(
                    ArgumentParseException.class,
                    (c, e) -> audienceMapper.apply(c).sendMessage(
                            this.decorator.apply(this.componentBuilders.get(ExceptionType.ARGUMENT_PARSING).apply(c, e))
                    )
            );
        }
        if (this.componentBuilders.containsKey(ExceptionType.COMMAND_EXECUTION)) {
            manager.registerExceptionHandler(
                    CommandExecutionException.class,
                    (c, e) -> audienceMapper.apply(c).sendMessage(
                            this.decorator.apply(this.componentBuilders.get(ExceptionType.COMMAND_EXECUTION).apply(c, e))
                    )
            );
        }
    }

    private static Component getMessage(final Throwable throwable) {
        final Component msg = ComponentMessageThrowable.toComponent(throwable);
        return msg == null ? NULL : msg;
    }

    /**
     * Exception types
     */
    public enum ExceptionType {
        /**
         * The input does not correspond to any known command ({@link InvalidSyntaxException})
         */
        INVALID_SYNTAX,
        /**
         * The sender is not of the right type ({@link InvalidCommandSenderException})
         */
        INVALID_SENDER,
        /**
         * The sender does not have permission to execute the command ({@link NoPermissionException})
         */
        NO_PERMISSION,
        /**
         * An argument failed to parse ({@link ArgumentParseException})
         */
        ARGUMENT_PARSING,
        /**
         * A command handler had an exception ({@link CommandExecutionException})
         */
        COMMAND_EXECUTION
    }

}
