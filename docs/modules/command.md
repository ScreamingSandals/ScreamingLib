# Command
Module required for creating multi-platform commands.

## Features
* [Cloud Command Framework](https://github.com/Incendo/cloud) integration

## Usage
Supported platforms are: `bukkit, bungee, minestom, sponge, velocity`

### Maven
```xml
<repositories>
    <repository>
        <id>screamingrepo</id>
        <url>https://repo.screamingsandals.org/repository/maven-public</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.screamingsandals.lib</groupId>
        <artifactId>command-YOUR_PLATFORM</artifactId>
        <version>LATEST_VERSION_HERE</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>org.screamingsandals.lib</groupId>
        <artifactId>screaming-annotation</artifactId>
        <version>LATEST_VERSION_HERE</version>
        <scope>provided</scope>
    </dependency>
    <!-- Optional, but recommended (change version to the latest if necessary) -->
    <!-- https://mvnrepository.com/artifact/cloud.commandframework/cloud-minecraft-extras -->
    <dependency>
    	<groupId>cloud.commandframework</groupId>
    	<artifactId>cloud-minecraft-extras</artifactId>
    	<version>1.5.0</version>
	</dependency>
</dependencies>

<!-- Shade plugin configuration and relocation package org.screamingsandals.lib to your own package -->
```

### Gradle
```groovy
repositories {
    maven { 
        url 'https://repo.screamingsandals.org/repository/maven-public'
    }
}

dependencies {
    implementation 'org.screamingsandals.lib:command-YOUR_PLATFORM:LATEST_VERSION_HERE'
    annotationProcessor 'org.screamingsandals.lib:screaming-annotation:LATEST_VERSION_HERE'
    // Optional, but recommended (change the version to the latest if necessary)
    // https://mvnrepository.com/artifact/cloud.commandframework/cloud-minecraft-extras
    implementation 'cloud.commandframework:cloud-minecraft-extras:1.5.0'
}
```

## Examples
### Creating commands with Cloud

#### Command service
Let's start with creating a service that will construct all of our commands (feel free to copy paste the snippet).
```java
@Service(dependsOn = {
    CloudConstructor.class
})
public final class CommandService {
    @Provider(level = Provider.Level.POST_ENABLE)
    public static CommandManager<CommandSenderWrapper> provideCommandManager() {
        try {
            final CommandManager<CommandSenderWrapper> manager = CloudConstructor.construct(CommandExecutionCoordinator.simpleCoordinator());

            // provides a central place for handling common command usage errors
            // remove this part if you don't have the cloud-minecraft-extras module
            new MinecraftExceptionHandler<CommandSenderWrapper>()
                    .withDefaultHandlers()
                    .withHandler(MinecraftExceptionHandler.ExceptionType.NO_PERMISSION, (senderWrapper, e) ->
                            // Component from Adventure
                            Component.text("Insufficient permissions!")
                    )
                    .withHandler(MinecraftExceptionHandler.ExceptionType.INVALID_SYNTAX, (senderWrapper, e) ->
                            // Component from Adventure
                            Component.text("Invalid syntax!")
                    )
                    .apply(manager, s -> s);

            return manager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
```

#### Base command class
Then, create an abstract class that will be extended by the command classes (feel free to copy paste the snippet).
```java
@ServiceDependencies(dependsOn = {
    CommandService.class // the command service class you created in the last step
})
public abstract class BaseCommand {
    protected final @NonNull String name;
    protected final @Nullable Permission permission;
    protected final boolean allowConsole;

    public BaseCommand(String name, Permission permission, boolean allowConsole) {
        this.name = name;
        this.permission = permission;
        this.allowConsole = allowConsole;
    }

    public @NonNull String getName() {
        return name;
    }

    public @Nullable Permission getPermission() {
        return permission;
    }

    public boolean isConsoleAllowed() {
        return allowConsole;
    }

    protected abstract void construct(Command.Builder<CommandSenderWrapper> commandSenderWrapperBuilder, CommandManager<CommandSenderWrapper> manager);

    @OnPostEnable
    public void construct(@ProvidedBy(CommandService.class) CommandManager<CommandSenderWrapper> manager) {
        // all commands will have the same root, e.g. /someplugin <command name>
        Command.Builder<CommandSenderWrapper> builder = manager.commandBuilder("someplugin")
                .literal(name);
        // or you can have separated commands
        // Command.Builder<CommandSenderWrapper> builder = manager.commandBuilder(name);

        // checks for permissions
        if (permission != null) {
            builder = builder.permission(
                    PredicatePermission.of(SimpleCloudKey.of(name), perm ->
                            perm.getType() == CommandSenderWrapper.Type.CONSOLE || permission.hasPermission(perm)
                    )
            );
        }

        // sender will be directly PlayerWrapper, if console is not allowed
        if (!allowConsole) {
            builder = builder.senderType(PlayerWrapper.class);
        }
        construct(builder, manager);
    }
}
```

#### Command implementation
Now let's actually implement the command.
```java
@Service
public class ExampleCommand extends BaseCommand { // extend the base command class that we created in the last step
    public ExampleCommand() {
        // command name, command permission (can be null for no permission), is console allowed?
        super("examplecommand", null, true);
    }

    @Override
    protected void construct(Command.Builder<CommandSenderWrapper> commandSenderWrapperBuilder, CommandManager<CommandSenderWrapper> manager) {
        manager.command(
                commandSenderWrapperBuilder
                        // PlayerWrapper argument
                        .argument(
                                manager
                                        .argumentBuilder(String.class, "player")
                                        .withSuggestionsProvider((c, s) ->
                                                Server.getConnectedPlayers().stream().map(PlayerWrapper::getName).toList()
                                        )
                        )
                        .handler(commandContext -> {
                            final Optional<PlayerWrapper> player = PlayerMapper.getPlayer((String) commandContext.get("player"));
                            if (player.isEmpty()) {
                                commandContext.getSender().sendMessage("Invalid player specified!");
                                return;
                            }
                            final String senderName = (commandContext.getSender().getType() == CommandSenderWrapper.Type.CONSOLE) ? "CONSOLE" : commandContext.getSender().as(PlayerWrapper.class).getName();
                            player.orElseThrow().sendMessage("Hello from " + senderName);
                        })
        );
    }
}
```
Now register the command implementation class in your plugin's `@Init` annotation, and you're done! You've just made yourself a command in the `/someplugin examplecommand <player>` format.