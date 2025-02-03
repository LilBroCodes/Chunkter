Plugin made for those that want to generate the world with Chunky, but don't want it to impact performance for players.

## Configuration:
### (Config file is /plugins/Chunkter/config.yml)
-   max-players: <integer>, default 1
    - This sets the max amount of players that can be online when it runs. That means, that if   it's set to 1, chunky will run when no one, or one person is online. 0 is allowed.
  
-   enforce-generation: false
    - This makes it so you cannot use the chunky command to do anything, except `/chunky progress` which just shows progress.
-   default-enabled: true
    - This makes it, so that you don't need to run `/ct on` at each restart, and makes it be enabled by default.

## Commands: 
There is one command with the plugin, which is `/chunkter` (alias `/ct`)\
This is a list of subcommands (`/ct <subcommand> <args>`), which all have their own arguments.
- on & off (Usage: `/ct <on / off>`)
  - Does what you think it does, enables / disables automatic control over chunky. Does not retain over restarts, you have to run `/ct on` to enable it every restart!
- config (Usage: `/ct config <name> <value>`)
  - Takes two arguments, the name of what to configure and the value. max-players takes an integer, default-eanbled and enforce-generation take either true or false. This is saved to the config file, and will retain through restarts!
- status (Usage: `/ct status`)
  - Shows the following information about chunkter:
    - Is chunky running (Generating)
    - Is chunkter running (Does the plugin think chunky is running, should match the above one)
    - Is chunkter enabled (Enabled as in the config, not the plugin itself)
    - Also shows the current config.
- reload (Usage: `/ct reload`)
  - Reloads the configuration file.
