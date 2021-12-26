# Home
ScreamingLib is a multiplatform library for creating Minecraft plugins/extensions.

## Module list

### Modules for servers
* [Core](modules/core.md) (server)
* Hologram
* Healthindicator
* Lang
* [NPC](modules/npc.md)
* [Packets](modules/packets.md)
* Placeholders
* [Command](modules/command.md)
* [Sidebar](modules/sidebar.md) (includes scoreboards)
* Signs

### Modules for proxies
* Core (proxy)
* Lang
* [Command](modules/command.md)

## Compiling
### Requirements

* JDK 17 or higher
* Internet connection

Clone the [repo](https://github.com/ScreamingSandals/ScreamingLib) and run `./gradlew screamCompile`.
Binaries for each module will be present in the `build/libs` folder of each subproject and also in your local maven repository.