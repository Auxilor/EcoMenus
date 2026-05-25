---
title: "Commands and Permissions"
sidebar_position: 4
---

| Command                               | Description                                                        | Permission                         |
|---------------------------------------|--------------------------------------------------------------------|------------------------------------|
| `/ecomenus`                           | Base command (shows unknown subcommand message if used on its own) | `ecomenus.command.ecomenus`        |
| `/ecomenus open <menu> <player>`      | Opens a menu for a player (checks that menu's open conditions)     | `ecomenus.command.open`            |
| `/ecomenus forceopen <menu> <player>` | Force-opens a menu for a player (bypasses that menu's conditions)  | `ecomenus.command.forceopen`       |
| `/ecomenus reload`                    | Reload the plugin and menu configs                                 | `ecomenus.command.reload`          |
| `/<command>`                          | Opens a menu from its `command:` value in the menu config          | `ecomenus.menus.<menu_id>.command` |
