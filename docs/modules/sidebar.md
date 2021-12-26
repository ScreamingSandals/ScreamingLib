# Sidebar
A module for creating multi-platform scoreboards and sidebars.

## Features
* Sidebars
* Scoreboards (sidebar for showing score)

## Usage
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
        <artifactId>sidebar-YOUR_PLATFORM</artifactId>
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
    implementation 'org.screamingsandals.lib:sidebar-YOUR_PLATFORM:LATEST_VERSION_HERE'
    annotationProcessor 'org.screamingsandals.lib:annotation:LATEST_VERSION_HERE'
}
```

## Examples
### Creating a sidebar
A `Sidebar` extends [LinedVisual](visuals.md#linedvisual), [DatableVisual](visuals.md#datablevisual) and `TeamedSidebar` so you can use any methods from those.  

```java
Sidebar.of()
	.title("Testing sidebar")
	.bottomLine(Component.text("Line 1:"))
    .bottomLine(Component.text("Line 2:"))
    .addViewer(player)
    .show();
```