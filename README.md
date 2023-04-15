# ScreamingLib
ScreamingLib is a multiplatform library for creating Minecraft plugins.

## Module list

### Modules for servers
* [Base module](core/README.md)
* [Hologram](extensions/hologram/README.md)
* [Health Indicator](extensions/healthindicator/README.md)
* [Lang](extensions/lang/README.md)
* [NPC](extensions/npc/README.md)
* [Packet library for Minecraft: Java Edition](extensions/packets/README.md)
* [Placeholders](extensions/placeholders/README.md)
* [Cloud Command Framework support](extensions/cloud/README.md)
* [Scoreboards and Sidebars](extensions/sidebar/README.md)
* [Clickable Signs](extensions/signs/README.md)

### Modules for proxy
* [Base module](proxy/README.md)
* [Lang](extensions/lang/README.md)
* [Cloud Command Framework support](extensions/cloud/README.md)

## Compiling

Requirements:
* JDK 17 and higher (compiled artifacts are than compatible with Java 11 and higher)
* Internet connection

Clone this repo and run `./gradlew screamCompile`. Binaries for each module will be present in the `build/libs` folder of each subproject and also in your local maven repository.