---
title: "EcoMenus"
---

## What is EcoMenus?

EcoMenus is a custom GUI plugin for building menus from config files. You define each menu's items, pages, and click actions in YAML, and players open them with a command or from another menu.

It is built on libreforge, so every click, open, and close can run effects and check conditions: send messages, give items, run commands, open other menus, and hundreds more.

## What sets EcoMenus apart?

- **Layers:** stack several items in the same slot and show or hide them per condition, so one slot can change with the player's state.
- **Pages and masks:** multi-page menus with a filler pattern, plus automatic forwards and backwards navigation arrows.
- **Effect chains:** string multiple effects under one click with libreforge chains, so complex actions need no code.
- **Menu navigation:** the `open_menu` and `reset_previous_menu` effects let menus link to each other and remember where the player came from.

<hr/>

## Where to go next

- **Build a menu:** [How to Make a Custom Menu](how-to-make-a-custom-menu) walks you through your first menu start to finish.
- **Commands:** [Commands and Permissions](commands-and-permissions) lists every command and permission node.
- **Configuration:** [Plugin Config](plugin-config) covers the global `config.yml`.
- **Developers:** the [API](api) page shows how to hook into EcoMenus.