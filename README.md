Proposed commands:
==================
```
/setup
  lane
    add [lane]
    clear [lane]
    info
  sumo
  wool
    clear
    
/team
  create <name> <color>
    in: lobby
  modify <name> <color>
    in: lobby
  join <name>
    in: lobby
  list
    in: lobby - show all teams
    in: match - show match teams

/match
  new <map>
  addteam <team>
  sumo
  assign [team] <lane>
  start
  end
```

Sample config file
==================
```yaml
name: Direct Fire
author: Vechz

sumo:
  zone:
    min:
      x: 340.7
      y: 71.1
      z: -43.3
    max:
      x: 347.3
      y: 75.0
      z: -37.7
lanes:
  left:
    wools:
      red wool:
        at:
          x: 290.0
          y: 68.0
          z: -18.0
        block: wool:red
      blue wool:
        at:
          x: 291.0
          y: 68.0
          z: -18.0
        block: wool:blue
      green wool:
        at:
          x: 292.0
          y: 68.0
          z: -18.0
        block: wool:green    
    zones:
    - min:
        x: -256.3
        y: 0.0
        z: -32.3
      max:
        x: 320.3
        y: 128.0
        z: -15.7
  right:
    wools:
      red wool:
        at:
          x: 290.0
          y: 68.0
          z: -63.0
        block: wool:red
      blue wool:
        at:
          x: 291.0
          y: 68.0
          z: -68.0
        block: wool:blue
      green wool:
        at:
          x: 292.0
          y: 68.0
          z: -63.0
        block: wool:green
    zones:
    - min:
        x: -256.3
        y: 0.0
        z: -64.3
      max:
        x: 320.3
        y: 128.0
        z: -47.7
```