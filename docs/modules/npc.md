# NPC
Module for creating multi-platform NPC entities.

## Features
* Multiline names
* Skins
* Head following player

## Usage
Supported platforms are the same as the packets module, because this module doesn't have any platform specific code.

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
        <artifactId>npc-YOUR_PLATFORM</artifactId>
        <version>LATEST_VERSION_HERE</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>org.screamingsandals.lib</groupId>
        <artifactId>annotation</artifactId>
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
    implementation 'org.screamingsandals.lib:npc-YOUR_PLATFORM:LATEST_VERSION_HERE'
    annotationProcessor 'org.screamingsandals.lib:annotation:LATEST_VERSION_HERE'
}
```

## Examples
### Creating your first NPC

Start by spawning the NPC on a specified location, like this:
```java
final NPC npc = NPC.of(yourLocation);
```
Now let's set a nice skin for the NPC. I'll use Misat's skin in this example.
```java
final PlayerWrapper misat = PlayerMapper.getPlayer("Misat11").orElseThrow(() -> new RuntimeException("No misat here lol"));
final NPC npc = NPC.of(yourLocation)
NPCSkin.retrieveSkin(misat.getUuid()).thenAccept(skin -> {
    // null if an error occurred, like no internet connection
    if (skin != null) {
        npc.setSkin(skin);
    }
})
```
I think that NPC's looking into your soul are not creepy at all, so let's make the NPC look at players and let's give it a name.
```java
final NPC npc = ...;
// you can totally do this in the npc builder, i chose not to, because i didn't want to copy the huge block of skin values
npc.setShouldLookAtPlayer(true);
// requires a List<Component> to allow for multiline names
npc.setDisplayName(Arrays.asList(Component.text("Misat11")));
```
And you've made yourself a fresh new NPC!

### Destroying a NPC
If you want to destroy a NPC, you can call the `LocatableVisual#destroy()` method.
```java
final NPC npc = NPC.of(yourLocation);
npc.destroy();
```