# Tasker
A library required for creating synchronous and asynchronous tasks within the plugin.

!!! tip "Managing tasks without Tasker"

    If you want to manage tasks yourself (without the Tasker library), you can utilize the `Server#runSynchronously(Runnable)` method to run a task synchronously.

## Features
* Self-cancelling tasks
* Asynchronous tasks

## Usage
Supported platforms are: `bukkit, bungee, minestom, sponge, velocity`

### Maven
```xml
<repositories>
    <repository>
        <id>screamingrepo</id>
        <url>https://repo.screamingsandals.org/repository/maven-public</url>
    </repository>
    <repository>
        <id>papermc</id>
        <url>https://papermc.io/repo/repository/maven-public</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.screamingsandals.lib</groupId>
        <artifactId>tasker-YOUR_PLATFORM</artifactId>
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
    maven {
        url 'https://papermc.io/repo/repository/maven-public'
    }
}

dependencies {
    implementation 'org.screamingsandals.lib:tasker-YOUR_PLATFORM:LATEST_VERSION_HERE'
    annotationProcessor 'org.screamingsandals.lib:screaming-annotation:LATEST_VERSION_HERE'
}
```

## Examples
### Running tasks

Start with registering the Tasker service class in your plugin's `@Init` annotation.
```java
@Init(services = {
    Tasker.class
})
```

#### Normal task
So let's say, that we want to create a task to send a message to console every 30 seconds until the server stops/plugin disables.  

First of all, let's create a task builder, which takes in a `Runnable`.
```java
Tasker.build(() -> PlayerMapper.getConsoleSender().sendMessage("Hello World!"));
```
Then, let's add the repeat time period.
```java
Tasker.build(() -> PlayerMapper.getConsoleSender().sendMessage("Hello World!")).repeat(30, TaskerTime.SECONDS);
```
Now let's start the task.
```java
Tasker.build(() -> PlayerMapper.getConsoleSender().sendMessage("Hello World!")).repeat(30, TaskerTime.SECONDS).start();
```
And you're done!

#### Self-cancelling task
Let's create a task which sends a message to the console 10 times and then stops.  

First of all, let's create a task builder, which takes in a `Function<TaskBase, Runnable>`.
```java
final AtomicInteger count = new AtomicInteger(0);
Tasker.build(taskBase -> () -> {
    if (count.get() >= 10) {
        taskBase.cancel();
        return;
    }
    PlayerMapper.getConsoleSender().sendMessage("Hello World!");
    count.getAndIncrement();
});
``` 
Then, let's run the task right away.
```java
final AtomicInteger count = new AtomicInteger(0);
Tasker.build(taskBase -> () -> {
    if (count.get() >= 10) {
        taskBase.cancel();
        return;
    }
    PlayerMapper.getConsoleSender().sendMessage("Hello World!");
    count.getAndIncrement();
}).afterOneTick().start();
```
Congrats, you've just made a self-cancelling task!