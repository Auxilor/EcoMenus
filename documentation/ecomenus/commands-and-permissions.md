---
title: "Commands and Permissions"
sidebar_position: 4
---

EcoMenus adds one base command for opening and managing menus, plus an optional per-menu command. This page lists every command and the permission node that gates it.

| Command | Description | Permission |
| --- | --- | --- |
| **`/ecomenus`** | Base command; shows an unknown-subcommand message on its own. | `ecomenus.command.ecomenus` |
| **`/ecomenus open <menu> <player>`** | Opens a menu for a player, checking that menu's open conditions. | `ecomenus.command.open` |
| **`/ecomenus forceopen <menu> <player>`** | Force-opens a menu for a player, bypassing its conditions. | `ecomenus.command.forceopen` |
| **`/ecomenus reload`** | Reloads the plugin and all menu configs. | `ecomenus.command.reload` |
| **`/<command>`** | Opens a menu from its `command:` value in the menu config. | `ecomenus.menus.<menu_id>.command` |

<hr/>

## Where to go next

- **Build a menu:** [How to Make a Custom Menu](how-to-make-a-custom-menu) to set a menu's `command:` and open conditions.
- **Configuration:** [Plugin Config](plugin-config) for the global `config.yml`.