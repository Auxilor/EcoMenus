# `open_menu`
:::infoRequires:
EcoMenus
:::

:::dangerTriggered Effect
This effect requires a [Trigger](https://plugins.auxilor.io/effects/all-triggers) to activate.
:::

Opens an EcoMenus menu.
# Effect Syntax
```yaml
- id: open_menu
  args:
    menu: menu_id # The menu to open, configured in /menus/ folder
  ...other config (eg triggers, filters, mutators, etc)
```