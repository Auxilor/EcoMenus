---
title: "How to Make a Custom Menu"
sidebar_position: 1
---

A **menu** is a config-driven GUI that players open with a command or another menu. Each menu file defines its **pages**, the **slots** (clickable items) on them, and the **effects** that run when a slot is clicked or the menu opens and closes. This page builds one from scratch and then breaks down every part.

## Quick start

1. Open the `menus/` folder inside your EcoMenus config folder.
2. Copy `_example.yml` to a new file and name it after your menu, e.g. `store.yml`. The file name is the menu's ID.
3. Set the `title`, `rows`, and (optionally) a `command` to open it.
4. Lay out your background in `pages` and add your clickable items to `slots`.
5. Run `/ecomenus reload` to load the menu.
6. Open it with `/ecomenus open <menu> <player>` (or your `command:` value) and confirm the GUI appears with your items.

:::tip
`_example.yml` is included as a reference and is **never loaded**, so copy or rename it to make a real menu. You can also organise menus into subfolders inside `menus/`, and they'll still load.
:::

## Naming and IDs

The file name without `.yml` is the menu's **ID**: `store.yml` has the ID `store`. You use that ID in `/ecomenus open`, `/ecomenus forceopen`, and the `open_menu` effect. Slot items are resolved through the [Item Lookup System](https://plugins.auxilor.io/the-item-lookup-system), so any item string that works elsewhere in eco works here too.

:::warning ID rules
IDs may only contain lowercase letters, numbers, and underscores (a-z, 0-9, _). No spaces, capitals, or hyphens, or the menu will not load.
:::

## The structure of a menu

A menu config has five parts:

| Part | What it controls |
| --- | --- |
| **Menu info** | The title, optional command, size, and auto-refresh. |
| **Open and close effects** | Conditions to open the menu, and effects that fire on open and close. |
| **Page arrows** | The forwards and backwards navigation items. |
| **Pages and masks** | How many pages there are and the background filler pattern. |
| **Slots** | The clickable items: their position, layer, conditions, and click effects. |

Here is a complete menu with every part in place:

```yaml
# === Menu info: title, command, size, refresh ===
title: "Store Menu" # The GUI title shown to the player
command: store # Optional; registers /store to open this menu, omit for no command
rows: 6 # Number of rows in the GUI, between 1 and 6

refresh: # Optional; re-renders the menu for viewers on a timer
  enabled: false
  interval: 20 # Refresh interval in ticks (20 ticks = 1 second), minimum 1

# === Open and close: conditions and effects ===
conditions: [ ] # Conditions the player must meet to open the menu
cannot-open-messages: # Sent when the open conditions are not met
  - "&cYou cannot open this menu!"
open-effects: [ ] # Effects that run when the menu opens
close-effects: [ ] # Effects that run when the menu closes

# === Page arrows: navigation between pages ===
forwards-arrow:
  item: arrow name:"&fNext Page" # The arrow item; hidden automatically on the last page
  enabled: true
  location:
    row: 6
    column: 8
backwards-arrow:
  item: arrow name:"&fPrevious Page" # Hidden automatically on the first page
  enabled: true
  location:
    row: 6
    column: 2

# === Pages and masks: background layout ===
pages:
  - page: 1
    mask:
      items: # Each pattern digit maps to a material here (1 = first, 2 = second)
        - gray_stained_glass_pane
        - black_stained_glass_pane
      pattern: # One line per row, each exactly 9 characters; 0 means empty
        - "211101112"
        - "211111112"
        - "210000012"
        - "210010012"
        - "211111112"
        - "211101112"

# === Slots: the clickable items ===
slots:
  - item: barrier name:"&cClose"
    location:
      row: 6
      column: 5
      page: 1
    left-click:
      - id: close_inventory
```

### Menu info

The top of the file sets the title, size, and how the player gets in.

```yaml
title: "Store Menu" # The GUI title shown to the player
command: store # Optional; registers /store to open this menu, omit for no command
rows: 6 # Number of rows in the GUI, between 1 and 6

refresh: # Optional; re-renders the menu for viewers on a timer
  enabled: false
  interval: 20 # Refresh interval in ticks (20 ticks = 1 second), minimum 1
```

Enable `refresh` when slot items change while the menu is open (e.g. a balance display), so viewers see updates without reopening.

### Open and close effects

Gate who can open the menu, and run effects when it opens or closes.

```yaml
conditions: [ ] # Conditions the player must meet to open the menu
cannot-open-messages: # Sent when the open conditions are not met
  - "&cYou cannot open this menu!"
open-effects: # Run once when the menu opens
  - id: send_message
    args:
      message: "&aOpened the store"
close-effects: [ ] # Run once when the menu closes
```

:::danger Effects are their own system
Effects, conditions, and chains are a shared libreforge system, documented in full elsewhere:

- [Configuring an Effect](https://plugins.auxilor.io/effects/configuring-an-effect)
- [Configuring an Effect Chain](https://plugins.auxilor.io/effects/configuring-a-chain)
:::

### Page arrows

The forwards and backwards arrows move the player between pages.

```yaml
forwards-arrow:
  item: arrow name:"&fNext Page" # Hidden automatically on the last page
  enabled: true # Set false to remove this arrow entirely
  location:
    row: 6
    column: 8
backwards-arrow:
  item: arrow name:"&fPrevious Page" # Hidden automatically on the first page
  enabled: true
  location:
    row: 6
    column: 2
```

### Pages and masks

Each page has a **mask**: a list of materials and a pattern that paints them onto the background.

```yaml
pages:
  - page: 1 # The page number
    mask:
      items: # Pattern digit 1 = first material, 2 = second, up to 9
        - gray_stained_glass_pane
        - black_stained_glass_pane
      pattern: # One line per row, each exactly 9 characters; 0 leaves the slot empty
        - "211101112"
        - "211111112"
        - "210000012"
        - "210010012"
        - "211111112"
        - "211101112"
```

:::info How masks fill slots
The mask is the background layer. Any slot left as `0`, or covered by a higher-layer slot, shows the slot item instead. See [Pages](https://plugins.auxilor.io/all-plugins/pages) for the full system.
:::

### Slots

A slot is a positioned item with optional conditions and per-click effects.

```yaml
slots:
  - item: barrier name:"&cClose" # Resolved through the Item Lookup System
    lore: [ ] # Optional; extra lore lines added under the item
    location:
      row: 6
      column: 5
      page: 1 # Optional; omit to place the slot on every page
      layer: 2 # Optional; higher layers render on top, so stacked slots can swap by condition
    conditions: [ ] # Conditions required for the slot to show and be clickable
    show-if-not-met: false # If true, the item still shows when conditions fail, but is unclickable
    left-click:
      - id: close_inventory
    right-click: [ ]
    shift-left-click: [ ]
    shift-right-click:
      - id: open_menu # An EcoMenus effect; opens another menu and remembers this one
        args:
          menu: another_menu
```

Each slot supports four click handlers, each running its own effects:

| Action | Runs when |
| --- | --- |
| **`left-click`** | The item is left-clicked. |
| **`right-click`** | The item is right-clicked. |
| **`shift-left-click`** | The item is shift + left-clicked. |
| **`shift-right-click`** | The item is shift + right-clicked. |

:::info Menu navigation effects
EcoMenus adds two effects for linking menus: `open_menu` (opens a menu by ID and pushes the current one onto the back stack, so closing returns to it) and `reset_previous_menu` (clears that back stack). Use them in any click handler.
:::

With your slots in place the menu is complete, so reload and open it to test. If something is off, the common causes are below.

:::tip Troubleshooting
- **Menu won't load after a reload?** The file name is the ID, so check it is all lowercase letters, numbers, and underscores, and that the YAML is valid.
- **`_example.yml` won't open?** It is intentionally never loaded. Copy or rename it to a real menu file first.
- **A slot item is missing?** Its `conditions` are probably not met. Set `show-if-not-met: true` to keep it visible but unclickable, or check the item string against the Item Lookup System.
- **Slot hidden behind the background?** Give it a higher `layer` so it renders on top of the mask.
:::

<hr/>

## Where to go next

- **Effects and conditions:** [Configuring an Effect](https://plugins.auxilor.io/effects/configuring-an-effect) and [Configuring a Chain](https://plugins.auxilor.io/effects/configuring-a-chain) for everything a click can do.
- **Items:** the [Item Lookup System](https://plugins.auxilor.io/the-item-lookup-system) for crafting your slot items.
- **Commands:** [Commands and Permissions](commands-and-permissions) to open menus and gate access.
- **Defaults:** the shipped [`_example.yml`](https://github.com/Auxilor/EcoMenus/tree/master/eco-core/core-plugin/src/main/resources/menus) as a starting point.