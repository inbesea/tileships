## ***JumpTiles** 

| Tilename      | Function                             |
|---------------|--------------------------------------|
| SingleJumper  | Moves one map space (does not stack) |
| DoubleJumper  | Can move one-two spaces (No stack)   |
| SkipperJumper | Can only move two spaces (Can stack) |

## PowerTiles

| Tilename | Function                                                             |
|----------|----------------------------------------------------------------------|
| Battery  | Can power a tile for an encounter. Can be recharged with a powerTile |
| WireTile | Extends the range of a current                                       |

## DefenderTiles

| TileName   | Function                                                                                                                   |
|------------|----------------------------------------------------------------------------------------------------------------------------|
| ShieldTile | Can bounce asteroids off a radius. Uses up power with each bounce.                                                         |
| IronTile   | Stronger tile that can take multiple hits without breaking. Grows cracks with each impact. Can be repaired with basicTiles |

## DataTiles
| TileName        | Function                                                              |
|-----------------|-----------------------------------------------------------------------|
| Radar           | Can detect approaching asteroids and display alerts showing direction |
| SwarmReaderTile | Will lengthen the amount of asteroid data available                   |