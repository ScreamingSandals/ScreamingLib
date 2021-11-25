# Event
Module required for handling and dispatching events.

## Features
* Event priorities
* Ignoring cancellation
* Synchronous and asynchronous events
* Listening/firing events

## Usage
Supported platforms are: `bukkit, sponge, bungee, velocity`

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
        <artifactId>event-YOUR_PLATFORM</artifactId>
        <version>LATEST_VERSION_HERE</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>org.screamingsandals.lib</groupId>
        <artifactId>screaming-annotation</artifactId>
        <version>LATEST_VERSION_HERE</version>
        <scope>provided</scope>
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
    implementation 'org.screamingsandals.lib:event-YOUR_PLATFORM:LATEST_VERSION_HERE'
    annotationProcessor 'org.screamingsandals.lib:screaming-annotation:LATEST_VERSION_HERE'
}
```

## Examples

### Handling events
!!! warning "Platform events"

    ScreamingLib `EventManager` is **not** listening to non-standard events fired through the event manager of the platform you're running on!


#### OnEvent annotation
First of all, start by making a simple service class, like this:
```java
@Service
public class ExampleService {
    // Service class
}
```
Then create a new public method that returns void, has an event parameter and annotate it with `@OnEvent`. It is possible to specify the priority field and the ignoreCancelled field in the annotation.
```java
@Service
public class ExampleService {
    @OnEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(SPlayerInteractEvent event) {
        // Event handling logic
    }
}
```
Then register the service class in the `@Init` annotation on the main plugin class, and you're done!

#### EventManager registration
You can also listen to events by registering a `Consumer<AbstractEvent>` with the `EventManager`.
```java
EventManager.getDefaultEventManager().register(SPlayerLeaveEvent.class, event -> PlayerMapper.getConsoleSender().sendMessage("Player " + event.getPlayer() + " has left."));
```

### Firing events
!!! warning "Platform events"

    ScreamingLib `EventManager` is **not** firing events to the event manager of the platform you're running on!


#### Synchronously
You can fire an event synchronously with the `<T extends AbstractEvent> T EventManager#fire(T)` method.
```java
final ExampleEvent event = EventManager.fire(new ExampleEvent());
// if your event is cancellable, you can check for it
if (!event.isCancelled()) {
    PlayerMapper.getConsoleSender().sendMessage("Hello World!");
}
```

#### Asynchronously
You can fire an event asynchronously with the `<T extends AbstractEvent> CompletableFuture<T> EventManager#fireAsync(T)` method.
```java
EventManager.fireAsync(new ExampleAsynchronousEvent()).thenAccept(event -> {
    // if your event is cancellable, you can check for it
    if (!event.isCancelled()) {
        PlayerMapper.getConsoleSender().sendMessage("Hello World!");
    }
});
```

### Creating events

#### Synchronous
A synchronous event needs to extend the `AbstractEvent` class (or the `CancellableAbstractEvent` class, if cancellable).

```java
public class ExampleEvent extends CancellableAbstractEvent {
    private boolean cancelled = false;

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
```

#### Asynchronous
An asynchronous event needs to extend the `AbstractAsyncEvent` class (or the `CancellableAbstractAsyncEvent`, if cancellable).

```java
public class ExampleEvent extends CancellableAbstractAsyncEvent {
    private boolean cancelled = false;

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
```