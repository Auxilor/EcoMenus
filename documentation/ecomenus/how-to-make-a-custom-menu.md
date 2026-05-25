---
title: How to make a Menu
sidebar_position: 1
---
EcoMenus lets you build fully configurable GUIs. Menus can have multiple pages, masks, conditional slots, and click effects powered by libreforge.

## Creating a Menu

Each menu has its own config file, placed in the `/menus/` folder. You can add/remove menus as needed, and `_example.yml` is included to help you get started.

The ID of the menu is the file name. This is what you use in commands and in effects like `open_menu`.
ID's must be lowercase letters, numbers, and underscores only.

## Example Menu Config

```yaml
title: "Store Menu"
command: store
rows: 6

refresh:
  enabled: false
  interval: 20

conditions: [ ]

cannot-open-messages:
  - "&cYou cannot open this menu!"

open-effects:
  - id: send_message
    args:
      message: "&aOpened Store Menu"

close-effects:
  - id: send_message
    args:
      message: "&7Closed Store Menu"

forwards-arrow:
  item: arrow name:"&fNext Page"
  enabled: true
  location:
    row: 6
    column: 8

backwards-arrow:
  item: arrow name:"&fPrevious Page"
  enabled: true
  location:
    row: 6
    column: 2

pages:
  - page: 1
    mask:
      items:
        - gray_stained_glass_pane
        - black_stained_glass_pane
      pattern:
        - "211101112"
        - "211111112"
        - "210000012"
        - "210010012"
        - "211111112"
        - "211101112"

  - page: 2
    mask:
      items:
        - gray_stained_glass_pane
        - red_stained_glass_pane
      pattern:
        - "211101112"
        - "211111112"
        - "210000012"
        - "210010012"
        - "211111112"
        - "211101112"

slots:
  - item: barrier name:"&cClose"
    lore: [ ]
    location:
      row: 6
      column: 5
      page: 1
      layer: middle

    conditions: [ ]
    show-if-not-met: false

    left-click:
      - id: close_inventory

  - item: player_head head:%player% name:"&f%player%"
    location:
      row: 1
      column: 5
      page: 1

    shift-right-click:
      - id: open_menu
        args:
          menu: another_menu

      - id: run_command
        args:
          command: "eco give %player% 100"
```

## Understanding all the sections

### The Menu Info Section
```yaml
title: "Store Menu" # GUI title.
command: store # (Optional) Registers /store to open this menu.
rows: 6 # Number of rows, between 1 and 6.

refresh: # Whether to automatically refresh (re-render) this menu for viewers.
  enabled: false
  interval: 20 # The interval in ticks to refresh the menu, if enabled.
```

### The Open/Close Section
:::dangerEffects Section

The effects section is the core functionality of the menu. You can configure effects, conditions, and filters in this section to run when the menu is opened or closed.

Check out [Configuring an Effect](https://plugins.auxilor.io/effects/configuring-an-effect) to understand how to configure this section correctly.

For more advanced users or setups, you can configure chains in this section to string together different effects under one trigger. Check out [Configuring an Effect Chain](https://plugins.auxilor.io/effects/configuring-a-chain) for more info.

:::
```yaml
# Conditions required to open the menu.
conditions: [ ]

# Messages sent when conditions are not met.
cannot-open-messages:
  - "&cYou cannot open this menu!"

# Effects when the menu opens.
open-effects:
  - id: send_message
    args:
      message: "&aOpened Store Menu"

# Effects when the menu closes.
close-effects:
  - id: send_message
    args:
      message: "&7Closed Store Menu"
```

### The Page Navigation Section
```yaml
forwards-arrow:
  item: arrow name:"&fNext Page" # The GUI item.
  enabled: true # If the arrow is enabled.
  location:
    row: 6
    column: 8

backwards-arrow:
  item: arrow name:"&fPrevious Page"
  enabled: true
  location:
    row: 6
    column: 2
```

### The Pages and Mask Section

Learn more about pages [here](https://plugins.auxilor.io/all-plugins/pages)

```yaml
pages:
  - page: 1 # The page number
    mask:
      items:
        - gray_stained_glass_pane
        - black_stained_glass_pane
      pattern:
        - "211101112"
        - "211111112"
        - "210000012"
        - "210010012"
        - "211111112"
        - "211101112"
```

### The Slots Section
:::dangerEffects Section

The effects section is the core functionality of the menu. You can configure effects, conditions, and filters in this section to run when the slot is clicked.

Check out [Configuring an Effect](https://plugins.auxilor.io/effects/configuring-an-effect) to understand how to configure this section correctly.

For more advanced users or setups, you can configure chains in this section to string together different effects under one trigger. Check out [Configuring an Effect Chain](https://plugins.auxilor.io/effects/configuring-a-chain) for more info.

:::
```yaml
slots:
  - item: barrier name:"&cClose" # Item lookup: https://plugins.auxilor.io/the-item-lookup-system
    lore: [ ] # (Optional) The lore of the slot item.
    location: # The location of the item in the GUI
      row: 6
      column: 5
      page: 1
      layer: middle # (Optional) lower, middle, upper, top
    conditions: [ ] # Conditions required for the item to show and be clickable - use this with layers to show items based on conditions!
    show-if-not-met: false # Whether to show the item if conditions are not met. If true, the item will show but be unclickable.
    left-click: 
      - id: close_inventory
    right-click: []
    shift-left-click: []
    shift-right-click: []

  - item: player_head head:%player% name:"&f%player%"
    location:
      row: 1
      column: 5
      page: 1
    shift-right-click: # Effects when shift-right-clicking the item
      - id: open_menu
        args:
          menu: another_menu
```

### Available slot actions
| Action              | Description                                            |
|---------------------|--------------------------------------------------------|
| `left-click`        | Effects that run when left-clicking the item.          |
| `right-click`       | Effects that run when right-clicking the item.         |
| `shift-left-click`  | Effects that run when shift + left-clicking the item.  |
| `shift-right-click` | Effects that run when shift + right-clicking the item. |


## Internal Placeholders

<hr/>

## Default Configs

The default configs can be found in this repository at `https://github.com/Auxilor/EcoMenus/tree/master/eco-core/core-plugin/src/main/resources/menus`. <br/>