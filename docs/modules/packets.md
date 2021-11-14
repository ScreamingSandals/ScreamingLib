# Packets
Module for accessing Minecraft's packets.

## Features
* Sending packets
* Listening to packets

## Usage
Supported platforms are: `bukkit`

!!! warning "Packet availability"

    Not all packets are currently mapped! Most serverbound packets are currently not available.

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
        <artifactId>packets-YOUR_PLATFORM</artifactId>
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
    implementation 'org.screamingsandals.lib:packets-YOUR_PLATFORM:LATEST_VERSION_HERE'
    annotationProcessor 'org.screamingsandals.lib:screaming-annotation:LATEST_VERSION_HERE'
}
```

## Examples
### Sending a packet
I will demonstrate the functionality on the ClientboundDisconnectPacket.  

First of all, construct the SLib packet with it's constructor.
```java
final SClientboundDisconnectPacket packet = new SClientboundDisconnectPacket();
```
Then, add the data to the packet.
```java
final SClientboundDisconnectPacket packet = new SClientboundDisconnectPacket();
packet.reason(Component.text("You were kicked, because why not."));
```
And lastly, send the packet.
```java
final SClientboundDisconnectPacket packet = new SClientboundDisconnectPacket();
packet.reason(Component.text("You were kicked, because why not."));
packet.sendPacket(player);
```

---

It is also possible to directly supply the data in the constructor.
```java
final SClientboundDisconnectPacket packet = new SClientboundDisconnectPacket(Component.text("You were kicked, because why not."));
packet.sendPacket(player);
```

And it is very recommended to use the fluent accessors.
```java
new SClientboundDisconnectPacket()
	.reason(Component.text("You were kicked, because why not."))
	.sendPacket(player);
```