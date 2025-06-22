# Contributing to ScreamingLib

This document is currently a **WIP**. Much information is missing and subject to change as the repository evolves.

# Structure

The repository is divided into the following modules:

* `api-utils`: Contains common classes for other modules and for creating plugin APIs.
* `nbt`: A simple module for working with NBT and SNBT data (can be used independently of ScreamingLib).
* `minitag`: A simple module for serializing and deserializing MiniMessage-like data. This module is abstract and does not interpret the data itself (can be used independently of ScreamingLib).
* `nms`: Provides access to NMS classes in Bukkit and other platforms based on the vanilla Minecraft server, using Takenaka.
* `utils`: Utility classes used across most modules.
* `spectator`: Facilitates working with Raw JSON Text Components, inspired by Adventure and BungeeCord Chat API, acting as a wrapper around these.
* `shared-core`: Common classes for `core` and `proxy` modules.
* `core`: API for Minecraft servers.
* `proxy`: API for Minecraft proxies (BungeeCord/Velocity).
* `annotation`: Annotation processor for resolving dependencies between services and generating main plugin classes.
* `extensions`: Contains additional modules with functionalities not present in the basic modules.

Most of these modules usually include the following submodules:

* `common`: Contains the public-facing API and common implementation.
* `[platform]`: Contains platform-specific implementations:
    * `vanilla`: Common classes for servers based on vanilla Minecraft (not present for all modules).
    * `bukkit`: Implementation for Bukkit API (Spigot, Paper), depends on `vanilla` if present.
    * `sponge`: Implementation for Sponge API, depends on `vanilla` if present.
    * `minestom`: Implementation for Minestom.
    * `bungee`: Implementation for BungeeCord/Waterfall, or for `spectator` it includes the implementation for BungeeCord Chat API.
    * `velocity`: Implementation for Velocity.
    * `adventure`: Implementation for Adventure.

All public-facing APIs, common implementations, and platform implementations are organized as follows:

| Type                     | Package Location                                    | Core Module Package Location               |
|--------------------------|-----------------------------------------------------|--------------------------------------------|
| Public-facing APIs       | `org.screamingsandals.lib.[module]`                 | `org.screamingsandals.lib`                 |
| Common implementations   | `org.screamingsandals.lib.impl.[module]`            | `org.screamingsandals.lib.impl`            |
| Platform implementations | `org.screamingsandals.lib.impl.[platform].[module]` | `org.screamingsandals.lib.impl.[platform]` |

The `core` module must not contain a subpackage of `org.screamingsandals.lib` named like any other module.

For `core-bukkit`, the implementation is divided into multiple source sets to maintain binary compatibility for older versions without needing reflection:

- **Source Sets:**
  - `common`: Uses the latest Bukkit API and defines common utility classes, such as `BukkitFeature`, `Version`, and `ClassStorage`. No other classes should be added unless necessary for access in `support_<version>` source sets.
  - `main`: Depends on the same Bukkit API version as `common`, depends on every other source set, including `common` and every `support_<version>` source set. Initialization of all services is done in this module.
- **Support Source Sets:**
  - If a new Bukkit API version deprecates or breaks an API used for older Minecraft versions, the usage of this API should be moved to the support module with the latest version utilizing this API.
  - These classes should be either in the `compat.v<version>` subpackage, or if implementing a type from the `core-common` module, should stay in the same package as they would in `main`, but with a suffix in the form of the version which introduced the API (or `1_8` if the API was introduced earlier than `1.8.x`).
    This version suffixing is also used in the `main` source set when multiple version-dependent implementations of anything are provided.