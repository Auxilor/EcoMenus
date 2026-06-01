---
title: "API"
sidebar_position: 6
---

This page is for developers who want to hook into EcoMenus from their own plugin, for example to open menus or read the registered menu list. EcoMenus is open-source, so you can also read the implementation directly.

## Source code

The full source is on GitHub at [Auxilor/EcoMenus](https://github.com/Auxilor/EcoMenus).

## Adding the dependency

1. Add the Auxilor repository to your `build.gradle.kts`.
2. Add EcoMenus as a `compileOnly` dependency.

```kotlin
repositories {
    maven("https://repo.auxilor.io/repository/maven-public/")
}

dependencies {
    compileOnly("com.willfp:EcoMenus:<version>")
}
```

The latest version available on the repo can be found [here](https://github.com/Auxilor/EcoMenus/tags).

<hr/>

## Where to go next

- **Shared APIs:** the [eco framework](https://github.com/Auxilor/eco), where the core menu and libreforge APIs live.
- **Config side:** [How to Make a Custom Menu](how-to-make-a-custom-menu) for the config most integrations drive.