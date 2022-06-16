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

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.Command;
import cloud.commandframework.CommandComponent;
import cloud.commandframework.CommandHelpHandler;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.CommandArgument;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.spectator.audience.Audience;

import static org.screamingsandals.lib.spectator.Component.text;
import static org.screamingsandals.lib.spectator.Component.space;
import static org.screamingsandals.lib.spectator.event.ClickEvent.runCommand;

/**
 * Opinionated extension of {@link CommandHelpHandler} for Minecraft
 *
 * @param <C> Command sender type
 */
@SuppressWarnings("unused")
public final class MinecraftHelp<C> {

    public static final int DEFAULT_HEADER_FOOTER_LENGTH = 46;
    public static final int DEFAULT_MAX_RESULTS_PER_PAGE = 6;

    /**
     * The default color scheme for {@link MinecraftHelp}
     */
    public static final HelpColors DEFAULT_HELP_COLORS = HelpColors.of(
            Color.GOLD,
            Color.GREEN,
            Color.YELLOW,
            Color.GRAY,
            Color.DARK_GRAY
    );

    public static final String MESSAGE_HELP_TITLE = "help";
    public static final String MESSAGE_COMMAND = "command";
    public static final String MESSAGE_DESCRIPTION = "description";
    public static final String MESSAGE_NO_DESCRIPTION = "no_description";
    public static final String MESSAGE_ARGUMENTS = "arguments";
    public static final String MESSAGE_OPTIONAL = "optional";
    public static final String MESSAGE_SHOWING_RESULTS_FOR_QUERY = "showing_results_for_query";
    public static final String MESSAGE_NO_RESULTS_FOR_QUERY = "no_results_for_query";
    public static final String MESSAGE_AVAILABLE_COMMANDS = "available_commands";
    public static final String MESSAGE_CLICK_TO_SHOW_HELP = "click_to_show_help";
    public static final String MESSAGE_PAGE_OUT_OF_RANGE = "page_out_of_range";
    public static final String MESSAGE_CLICK_FOR_NEXT_PAGE = "click_for_next_page";
    public static final String MESSAGE_CLICK_FOR_PREVIOUS_PAGE = "click_for_previous_page";

    private final AudienceProvider<C> audienceProvider;
    private final CommandManager<C> commandManager;
    private final String commandPrefix;
    private final Map<String, ComponentLike> messageMap = new HashMap<>();

    private BiPredicate<Command<C>, C> commandFilter = (c, s) -> true;
    private MessageProvider<C> messageProvider = basicMessageProvider(this);
    private Function<String, Component> descriptionDecorator = Component::text;
    private HelpColors colors = DEFAULT_HELP_COLORS;
    private int headerFooterLength = DEFAULT_HEADER_FOOTER_LENGTH;
    private int maxResultsPerPage = DEFAULT_MAX_RESULTS_PER_PAGE;


    /**
     * Construct a new Minecraft help instance for a sender type which is an {@link Audience}.
     *
     * @param commandPrefix  Command that was used to trigger the help menu. Used to help insertion generation
     * @param commandManager command manager
     * @param <C>            sender type extending {@link Audience}
     * @return new MinecraftHelp instance
     */
    public static <C extends Audience> MinecraftHelp<C> createNative(
            final @NotNull String commandPrefix,
            final @NotNull CommandManager<C> commandManager
    ) {
        return new MinecraftHelp<>(
                commandPrefix,
                AudienceProvider.nativeAudience(),
                commandManager
        );
    }

    public static <C extends Audience> MessageProvider<C> audienceMessageProvider(MinecraftHelp<C> minecraftHelp) {
        return (sender, key, args) -> {
            var cl = minecraftHelp.messageMap.get(key);
            if (cl instanceof AudienceComponentLike) {
                return ((AudienceComponentLike) cl).asComponent(sender);
            }
            return cl.asComponent();
        };
    }

    public static <C> MessageProvider<C> basicMessageProvider(MinecraftHelp<C> minecraftHelp) {
        return (sender, key, args) -> minecraftHelp.messageMap.get(key).asComponent();
    }

    /**
     * Construct a new Minecraft help instance.
     *
     * @param commandPrefix    Command that was used to trigger the help menu. Used to help insertion generation
     * @param audienceProvider Provider that maps the command sender type to {@link Audience}
     * @param commandManager   Command manager instance
     */
    public MinecraftHelp(
            final @NotNull String commandPrefix,
            final @NotNull AudienceProvider<C> audienceProvider,
            final @NotNull CommandManager<C> commandManager
    ) {
        this.commandPrefix = commandPrefix;
        this.audienceProvider = audienceProvider;
        this.commandManager = commandManager;

        /* Default Messages */
        setMessage(MESSAGE_HELP_TITLE, "Help");
        setMessage(MESSAGE_COMMAND, "Command");
        setMessage(MESSAGE_DESCRIPTION, "Description");
        setMessage(MESSAGE_NO_DESCRIPTION, "No description");
        setMessage(MESSAGE_ARGUMENTS, "Arguments");
        setMessage(MESSAGE_OPTIONAL, "Optional");
        setMessage(MESSAGE_SHOWING_RESULTS_FOR_QUERY, "Showing search results for query");
        setMessage(MESSAGE_NO_RESULTS_FOR_QUERY, "No results for query");
        setMessage(MESSAGE_AVAILABLE_COMMANDS, "Available Commands");
        setMessage(MESSAGE_CLICK_TO_SHOW_HELP, "Click to show help for this command");
        setMessage(MESSAGE_PAGE_OUT_OF_RANGE, "Error: Page <page> is not in range. Must be in range [1, <max_pages>]");
        setMessage(MESSAGE_CLICK_FOR_NEXT_PAGE, "Click for next page");
        setMessage(MESSAGE_CLICK_FOR_PREVIOUS_PAGE, "Click for previous page");
    }

    /**
     * Get the command manager instance
     *
     * @return Command manager
     */
    public @NotNull CommandManager<C> getCommandManager() {
        return this.commandManager;
    }

    /**
     * Get the audience provider that was used to create this instance
     *
     * @return Audience provider
     */
    public @NotNull AudienceProvider<C> getAudienceProvider() {
        return this.audienceProvider;
    }

    /**
     * Map a command sender to an {@link Audience}
     *
     * @param sender Sender to map
     * @return Mapped audience
     */
    public @NotNull Audience getAudience(final @NotNull C sender) {
        return this.audienceProvider.apply(sender);
    }

    /**
     * Sets a filter for what commands are visible inside the help menu.
     * When the {@link Predicate} tests {@code true}, then the command
     * is included in the listings.
     * <p>
     * The default filter will return true for all commands.
     *
     * @param commandPredicate Predicate to filter commands by
     */
    @NotNull
    public MinecraftHelp<C> commandFilter(final @NotNull Predicate<Command<C>> commandPredicate) {
        this.commandFilter = (c, s) -> commandPredicate.test(c);
        return this;
    }

    /**
     * Sets a filter for what commands are visible inside the help menu.
     * When the {@link Predicate} tests {@code true}, then the command
     * is included in the listings.
     * <p>
     * The default filter will return true for all commands.
     *
     * @param commandPredicate Predicate to filter commands by
     */
    @NotNull
    public MinecraftHelp<C> commandFilter(final @NotNull BiPredicate<Command<C>, C> commandPredicate) {
        this.commandFilter = commandPredicate;
        return this;
    }

    /**
     * Set the description decorator which will turn command and argument description strings into components.
     * <p>
     * The default decorator simply calls {@link Component#text(String)}
     *
     * @param decorator description decorator
     */
    @NotNull
    public MinecraftHelp<C> descriptionDecorator(final @NotNull Function<@NotNull String, @NotNull Component> decorator) {
        this.descriptionDecorator = decorator;
        return this;
    }

    /**
     * Configure a message
     *
     * @param key   Message key. These are constants in {@link MinecraftHelp}
     * @param value The text for the message
     */
    @NotNull
    public MinecraftHelp<C> setMessage(
            final @NotNull String key,
            final @NotNull String value
    ) {
        this.messageMap.put(key, text(value));
        return this;
    }

    /**
     * Configure a message
     *
     * @param key   Message key. These are constants in {@link MinecraftHelp}
     * @param value The component for the message
     */
    @NotNull
    public MinecraftHelp<C> setMessage(
            final @NotNull String key,
            final @NotNull ComponentLike value
    ) {
        this.messageMap.put(key, value);
        return this;
    }

    /**
     * Set a custom message provider function to be used for getting messages from keys.
     * <p>
     * The keys are constants in {@link MinecraftHelp}.
     *
     * @param messageProvider The message provider to use
     */
    public MinecraftHelp<C> messageProvider(final @NotNull MessageProvider<C> messageProvider) {
        this.messageProvider = messageProvider;
        return this;
    }

    /**
     * Set a custom message provider function to be used for getting messages from keys.
     * <p>
     * The keys are constants in {@link MinecraftHelp}.
     *
     * @param messageProviderFunction Function that will construct message provider
     */
    public MinecraftHelp<C> messageProvider(final @NotNull Function<MinecraftHelp<C> , MessageProvider<C>> messageProviderFunction) {
        this.messageProvider = messageProviderFunction.apply(this);
        return this;
    }

    /**
     * Set the colors to use for help messages.
     *
     * @param colors The new {@link HelpColors} to use
     */
    @NotNull
    public MinecraftHelp<C> setHelpColors(final @NotNull HelpColors colors) {
        this.colors = colors;
        return this;
    }

    /**
     * Get the colors used for help messages.
     *
     * @return The active {@link HelpColors}
     */
    public @NotNull HelpColors getHelpColors() {
        return this.colors;
    }

    /**
     * Set the length of the header/footer of help menus
     * <p>
     * Defaults to {@link MinecraftHelp#DEFAULT_HEADER_FOOTER_LENGTH}
     *
     * @param headerFooterLength The new length
     */
    @NotNull
    public MinecraftHelp<C> setHeaderFooterLength(final int headerFooterLength) {
        this.headerFooterLength = headerFooterLength;
        return this;
    }

    /**
     * Set the maximum number of help results to display on one page
     * <p>
     * Defaults to {@link MinecraftHelp#DEFAULT_MAX_RESULTS_PER_PAGE}
     *
     * @param maxResultsPerPage The new value
     */
    @NotNull
    public MinecraftHelp<C> setMaxResultsPerPage(final int maxResultsPerPage) {
        this.maxResultsPerPage = maxResultsPerPage;
        return this;
    }

    /**
     * Query commands and send the results to the recipient. Will respect permissions.
     *
     * @param rawQuery  Command query (without leading '/', including optional page number)
     * @param recipient Recipient
     */
    public void queryCommands(
            final @NotNull String rawQuery,
            final @NotNull C recipient
    ) {
        final String[] splitQuery = rawQuery.split(" ");
        int page;
        String query;
        try {
            final String pageText = splitQuery[splitQuery.length - 1];
            page = Integer.parseInt(pageText);
            query = rawQuery.substring(0, Math.max(rawQuery.lastIndexOf(pageText) - 1, 0));
        } catch (NumberFormatException e) {
            page = 1;
            query = rawQuery;
        }
        final Audience audience = this.getAudience(recipient);
        this.printTopic(
                recipient,
                query,
                page,
                this.commandManager.getCommandHelpHandler(c -> this.commandFilter.test(c, recipient)).queryHelp(recipient, query)
        );
    }

    private void printTopic(
            final @NotNull C sender,
            final @NotNull String query,
            final int page,
            final CommandHelpHandler.@NotNull HelpTopic<C> helpTopic
    ) {
        if (helpTopic instanceof CommandHelpHandler.IndexHelpTopic) {
            this.printIndexHelpTopic(sender, query, page, (CommandHelpHandler.IndexHelpTopic<C>) helpTopic);
        } else if (helpTopic instanceof CommandHelpHandler.MultiHelpTopic) {
            this.printMultiHelpTopic(sender, query, page, (CommandHelpHandler.MultiHelpTopic<C>) helpTopic);
        } else if (helpTopic instanceof CommandHelpHandler.VerboseHelpTopic) {
            this.printVerboseHelpTopic(sender, query, (CommandHelpHandler.VerboseHelpTopic<C>) helpTopic);
        } else {
            throw new IllegalArgumentException("Unknown help topic type");
        }
    }

    private void printNoResults(
            final @NotNull C sender,
            final @NotNull String query
    ) {
        final Audience audience = this.getAudience(sender);
        audience.sendMessage(this.basicHeader(sender));
        audience.sendMessage(this.messageProvider.provide(sender, MESSAGE_NO_RESULTS_FOR_QUERY)
                .withColor(this.colors.text)
                .linear(
                    text(": \"", this.colors.text),
                    this.highlight(text("/" + query, this.colors.highlight)),
                    text("\"", this.colors.text)
                )
        );
        audience.sendMessage(this.footer(sender));
    }

    private void printIndexHelpTopic(
            final @NotNull C sender,
            final @NotNull String query,
            final int page,
            final CommandHelpHandler.@NotNull IndexHelpTopic<C> helpTopic
    ) {
        if (helpTopic.isEmpty()) {
            this.printNoResults(sender, query);
            return;
        }

        final Audience audience = this.getAudience(sender);
        new Pagination<CommandHelpHandler.VerboseHelpEntry<C>>(
                (currentPage, maxPages) -> {
                    final List<Component> header = new ArrayList<>();
                    header.add(this.paginatedHeader(sender, currentPage, maxPages));
                    header.add(this.showingResults(sender, query));
                    header.add(text()
                            .append(this.lastBranch())
                            .append(space())
                            .append(
                                    this.messageProvider.provide(
                                            sender,
                                            MESSAGE_AVAILABLE_COMMANDS
                                    ).withColor(this.colors.text)
                            )
                            .append(text(":", this.colors.text))
                            .build()
                    );
                    return header;
                },
                (helpEntry, isLastOfPage) -> {
                    final Optional<Component> richDescription =
                            helpEntry.getCommand().getCommandMeta().get(MinecraftExtrasMetaKeys.DESCRIPTION);
                    final Component description;
                    if (richDescription.isPresent()) {
                        description = richDescription.get();
                    } else if (helpEntry.getDescription().isEmpty()) {
                        description = this.messageProvider.provide(sender, MESSAGE_CLICK_TO_SHOW_HELP);
                    } else {
                        description = this.descriptionDecorator.apply(helpEntry.getDescription());
                    }

                    final boolean lastBranch =
                            isLastOfPage || helpTopic.getEntries().indexOf(helpEntry) == helpTopic.getEntries().size() - 1;

                    return text()
                            .append(text("   "))
                            .append(lastBranch ? this.lastBranch() : this.branch())
                            .append(this.highlight(text(
                                    String.format(" /%s", helpEntry.getSyntaxString()),
                                    this.colors.highlight
                                    ))
                                            .withHoverEvent(description.withColor(this.colors.text))
                                            .withClickEvent(runCommand(this.commandPrefix + " " + helpEntry.getSyntaxString()))
                            )
                            .build();
                },
                (currentPage, maxPages) -> this.paginatedFooter(sender, currentPage, maxPages, query),
                (attemptedPage, maxPages) -> this.pageOutOfRange(sender, attemptedPage, maxPages)
        ).render(helpTopic.getEntries(), page, this.maxResultsPerPage).forEach(audience::sendMessage);
    }

    private void printMultiHelpTopic(
            final @NotNull C sender,
            final @NotNull String query,
            final int page,
            final CommandHelpHandler.@NotNull MultiHelpTopic<C> helpTopic
    ) {
        if (helpTopic.getChildSuggestions().isEmpty()) {
            this.printNoResults(sender, query);
            return;
        }

        final Audience audience = this.getAudience(sender);
        final int headerIndentation = helpTopic.getLongestPath().length();
        new Pagination<String>(
                (currentPage, maxPages) -> {
                    final List<Component> header = new ArrayList<>();
                    header.add(this.paginatedHeader(sender, currentPage, maxPages));
                    header.add(this.showingResults(sender, query));
                    header.add(this.lastBranch()
                            .withAppendix(this.highlight(text(" /" + helpTopic.getLongestPath(), this.colors.highlight))));
                    return header;
                },
                (suggestion, isLastOfPage) -> {
                    final boolean lastBranch = isLastOfPage
                            || helpTopic.getChildSuggestions().indexOf(suggestion) == helpTopic.getChildSuggestions().size() - 1;

                    return space().repeat(headerIndentation)
                            .withAppendix(lastBranch ? this.lastBranch() : this.branch())
                            .withAppendix(this.highlight(text(" /" + suggestion, this.colors.highlight))
                                    .withHoverEvent(this.messageProvider.provide(sender, MESSAGE_CLICK_TO_SHOW_HELP)
                                            .withColor(this.colors.text))
                                    .withClickEvent(runCommand(this.commandPrefix + " " + suggestion)));
                },
                (currentPage, maxPages) -> this.paginatedFooter(sender, currentPage, maxPages, query),
                (attemptedPage, maxPages) -> this.pageOutOfRange(sender, attemptedPage, maxPages)
        ).render(helpTopic.getChildSuggestions(), page, this.maxResultsPerPage).forEach(audience::sendMessage);
    }

    private void printVerboseHelpTopic(
            final @NotNull C sender,
            final @NotNull String query,
            final CommandHelpHandler.@NotNull VerboseHelpTopic<C> helpTopic
    ) {
        final Audience audience = this.getAudience(sender);
        audience.sendMessage(this.basicHeader(sender));
        audience.sendMessage(this.showingResults(sender, query));
        final String command = this.commandManager.getCommandSyntaxFormatter()
                .apply(helpTopic.getCommand().getArguments(), null);
        audience.sendMessage(text()
                .append(this.lastBranch())
                .append(space())
                .append(this.messageProvider.provide(sender, MESSAGE_COMMAND).withColor(this.colors.primary))
                .append(text(": ", this.colors.primary))
                .append(this.highlight(text("/" + command, this.colors.highlight)))
        );
        /* Topics will use the long description if available, but fall back to the short description. */
        final Component richDescription =
                helpTopic.getCommand().getCommandMeta().get(MinecraftExtrasMetaKeys.LONG_DESCRIPTION)
                        .orElse(helpTopic.getCommand().getCommandMeta().get(MinecraftExtrasMetaKeys.DESCRIPTION)
                                .orElse(null));

        final Component topicDescription;
        if (richDescription != null) {
            topicDescription = richDescription;
        } else if (helpTopic.getDescription().isEmpty()) {
            topicDescription = this.messageProvider.provide(sender, MESSAGE_NO_DESCRIPTION);
        } else {
            topicDescription = this.descriptionDecorator.apply(helpTopic.getDescription());
        }

        final boolean hasArguments = helpTopic.getCommand().getArguments().size() > 1;
        audience.sendMessage(text()
                .append(text("   "))
                .append(hasArguments ? this.branch() : this.lastBranch())
                .append(space())
                .append(this.messageProvider.provide(sender, MESSAGE_DESCRIPTION).withColor(this.colors.primary))
                .append(text(": ", this.colors.primary))
                .append(topicDescription.withColor(this.colors.text))
        );
        if (hasArguments) {
            audience.sendMessage(text()
                    .append(text("   "))
                    .append(this.lastBranch())
                    .append(space())
                    .append(this.messageProvider.provide(sender, MESSAGE_ARGUMENTS).withColor(this.colors.primary))
                    .append(text(":", this.colors.primary))
            );

            final Iterator<CommandComponent<C>> iterator = helpTopic.getCommand().getComponents().iterator();
            /* Skip the first one because it's the command literal */
            iterator.next();

            while (iterator.hasNext()) {
                final CommandComponent<C> component = iterator.next();
                final CommandArgument<C, ?> argument = component.getArgument();

                final String syntax = this.commandManager.getCommandSyntaxFormatter()
                        .apply(Collections.singletonList(argument), null);

                final var textComponent = text()
                        .append(text("       "))
                        .append(iterator.hasNext() ? this.branch() : this.lastBranch())
                        .append(this.highlight(text(" " + syntax, this.colors.highlight)));
                if (!argument.isRequired()) {
                    textComponent.append(text(" (", this.colors.alternateHighlight));
                    textComponent.append(
                            this.messageProvider.provide(sender, MESSAGE_OPTIONAL).withColor(this.colors.alternateHighlight)
                    );
                    textComponent.append(text(")", this.colors.alternateHighlight));
                }
                final ArgumentDescription description = component.getArgumentDescription();
                if (!description.isEmpty()) {
                    textComponent.append(text(" - ", this.colors.accent));
                    textComponent.append(this.formatDescription(description).withColorIfAbsent(this.colors.text));
                }

                audience.sendMessage(textComponent);
            }
        }
        audience.sendMessage(this.footer(sender));
    }

    private Component formatDescription(final ArgumentDescription description) {
        if (description instanceof RichDescription) {
            return ((RichDescription) description).getContents();
        } else {
            return this.descriptionDecorator.apply(description.getDescription());
        }
    }

    private @NotNull Component showingResults(
            final @NotNull C sender,
            final @NotNull String query
    ) {
        return text()
                .append(this.messageProvider.provide(sender, MESSAGE_SHOWING_RESULTS_FOR_QUERY).withColor(this.colors.text))
                .append(text(": \"", this.colors.text))
                .append(this.highlight(text("/" + query, this.colors.highlight)))
                .append(text("\"", this.colors.text))
                .build();
    }

    private @NotNull Component button(
            final char icon,
            final @NotNull String command,
            final @NotNull Component hoverText
    ) {
        return text()
                .append(space())
                .append(text("[", this.colors.accent))
                .append(text(Character.toString(icon), this.colors.alternateHighlight))
                .append(text("]", this.colors.accent))
                .append(space())
                .clickEvent(runCommand(command))
                .hoverEvent(hoverText)
                .build();
    }

    private @NotNull Component footer(final @NotNull C sender) {
        return this.paginatedFooter(sender, 1, 1, "");
    }

    private @NotNull Component paginatedFooter(
            final @NotNull C sender,
            final int currentPage,
            final int maxPages,
            final @NotNull String query
    ) {
        final boolean firstPage = currentPage == 1;
        final boolean lastPage = currentPage == maxPages;

        if (firstPage && lastPage) {
            return this.line(this.headerFooterLength);
        }

        final String nextPageCommand = String.format("%s %s %s", this.commandPrefix, query, currentPage + 1);
        final Component nextPageButton = this.button('→', nextPageCommand,
                this.messageProvider.provide(sender, MESSAGE_CLICK_FOR_NEXT_PAGE).withColor(this.colors.text)
        );
        if (firstPage) {
            return this.header(sender, nextPageButton);
        }

        final String previousPageCommand = String.format("%s %s %s", this.commandPrefix, query, currentPage - 1);
        final Component previousPageButton = this.button('←', previousPageCommand,
                this.messageProvider.provide(sender, MESSAGE_CLICK_FOR_PREVIOUS_PAGE).withColor(this.colors.text)
        );
        if (lastPage) {
            return this.header(sender, previousPageButton);
        }

        final Component buttons = text()
                .append(previousPageButton)
                .append(this.line(3))
                .append(nextPageButton).build();
        return this.header(sender, buttons);
    }

    private @NotNull Component header(
            final @NotNull C sender,
            final @NotNull Component title
    ) {
        final int sideLength = (this.headerFooterLength - ComponentHelper.length(title)) / 2;
        return text()
                .append(this.line(sideLength))
                .append(title)
                .append(this.line(sideLength))
                .build();
    }

    private @NotNull Component basicHeader(final @NotNull C sender) {
        return this.header(sender,
                space().linear(
                    this.messageProvider.provide(sender, MESSAGE_HELP_TITLE).withColor(this.colors.highlight),
                    space()
                )
        );
    }

    private @NotNull Component paginatedHeader(
            final @NotNull C sender,
            final int currentPage,
            final int pages
    ) {
        return this.header(sender, text()
                .append(space())
                .append(this.messageProvider.provide(sender, MESSAGE_HELP_TITLE).withColor(this.colors.highlight))
                .append(space())
                .append(text("(", this.colors.alternateHighlight))
                .append(text(currentPage, this.colors.text))
                .append(text("/", this.colors.alternateHighlight))
                .append(text(pages, this.colors.text))
                .append(text(")", this.colors.alternateHighlight))
                .append(space())
                .build()
        );
    }

    @NotNull
    private Component line(final int length) {
        return text().content("-").color(this.colors.primary).strikethrough().build().repeat(length);
    }

    @NotNull
    private Component branch() {
        return text("├─", this.colors.accent);
    }

    @NotNull
    private Component lastBranch() {
        return text("└─", this.colors.accent);
    }

    @NotNull
    private Component highlight(final @NotNull Component component) {
        return ComponentHelper.highlight(component, this.colors.alternateHighlight);
    }

    @NotNull
    private Component pageOutOfRange(final @NotNull C sender, final int attemptedPage, final int maxPages) {
        return this.highlight(
                this.messageProvider.provide(
                        sender,
                        MESSAGE_PAGE_OUT_OF_RANGE,
                        String.valueOf(attemptedPage),
                        String.valueOf(maxPages)
                )
                        .withColor(this.colors.text)
                        .replaceText("<page>", String.valueOf(attemptedPage))
                        .replaceText("<max_pages>", String.valueOf(maxPages))
        );
    }

    @FunctionalInterface
    public interface MessageProvider<C> {

        /**
         * Creates a component from a command sender, key, and arguments
         *
         * @param sender command sender
         * @param key    message key (constants in {@link MinecraftHelp}
         * @param args   args
         * @return component
         */
        @NotNull
        Component provide(@NotNull C sender, @NotNull String key, @NotNull String... args);

    }

    @Data
    @RequiredArgsConstructor(staticName = "of")
    @Accessors(fluent = true)
    public static final class HelpColors {
        private final Color primary;
        private final Color highlight;
        private final Color alternateHighlight;
        private final Color text;
        private final Color accent;
    }

}
