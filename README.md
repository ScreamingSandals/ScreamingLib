# ScreamingLib
ScreamingLib is a multiplatform library for creating Minecraft plugins.

## Module list

### Modules for servers
* [Base module](core/README.md)
* [Hologram](hologram/README.md)
* [Health Indicator](healthindicator/README.md)
* [Lang](lang/README.md)
* [NPC](npc/README.md)
* [Packet library for Minecraft: Java Edition](packets/README.md)
* [Placeholders](placeholders/README.md)
* [Cloud Command Framework support](command/README.md)
* [Scoreboards and Sidebars](sidebar/README.md)
* [Clickable Signs](signs/README.md)

### Modules for proxy
* [Base module](proxy/README.md)
* [Lang](lang/README.md)
* [Cloud Command Framework support](command/README.md)

## Compiling

Requirements:
* JDK 17 and higher (compiled artifacts are than compatible with Java 11 and higher)
* Internet connection

Clone this repo and run `./gradlew screamCompile`. Binaries for each module will be present in the `build/libs` folder of each subproject and also in your local maven repository.