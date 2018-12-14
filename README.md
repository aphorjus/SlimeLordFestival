# Slime Lord Festival

The following is a README template for the Slime Lord Festival java game.

## Getting Started

Our game was developed using IntelliJ IDE.

### How to play this game

#### OVERWORLD: 

Start off by hosting a game with 2-4 players, or joining someone else's game. Upon entering the game, each player will start off at each 
4 corners of the Overworld map. Player 1 (Blue Slimelord) will start off by moving their slime 15 tiles in any direction on the path. The 
highlighted path provides a 15 tiled path for the player, in which they can click to move to any of the highlighted tiles. After player 1 
makes all his/her moves, it's player 2's turn (Green Slimelord), and so on. Every player can conquer a Token Tent, which generates 100 
tokens per turn for that player, and can also be conquered by another player. Every player can also press 'X' to enter the Shop, in which 
they can buy Slimes (6 slimes: basic slimes, lancers, strikers, advanced lancers, advanced strikers, mortars) and Abilities 
(5 available: slime strike, slime ball, mass heal, summon basic slime, summon advanced lancer). These slimes and abilities are available 
to use in the Battle State when Slimelords battle each other. 
If two Slimelords run into each other on the Overworld map, they have the option of battling one another or passing each other. If a 
Slimelord is defeated in battle, that player is eliminated from the game and can no longer play. A winner is declared if they win 3 
battles, one against each of the other players (if 4 players are playing), and that means that they conquered all of the Arenas. 

#### BATTLE:

The battle state contains the actions of the game. Each Slimelord that is part of the battle has two slime factories that produce a single slime at the start of each turn. Each Slimelord also has between zero and 5 abilities to use in battle. The Battle state has 3 "modes" attack mode, move mode, and ability mode. Pressing the "s" key will toggle between move and attack mode, and pressing the number keys 1-5 will select Slimelord abilities and active ability mode. The player may end their turn at any time with the "e" key.

Slimes can also be combined and upgraded. To combine slimes the player simply moves a slime into another of their slimes. Combining slimes increases their size, and increases the number of options they have for upgrading. Each Slimelord has a different set of special slimes they can upgrade to. To upgrade a slime right click on it and select the desired upgrade from the yellow list of available upgrades for the given size slime and Slimelord. The upgrade menu also provides some more detailed information about the given slime.

Every slime can move and attack once every turn, and each Slimelord can use one ability each turn. The goal of a battle is to destroy the opposing Slimelords two factories.

A test battle state can be entered from the Overworld by pressing "b". This will enter a battle between to slimeLords that between the two of them have every ability and special slime in the game. 


### Prerequisites

What things you need to install the software and how to install them

```
Set the server parameters <number of clients> <port>
Example for host. 
    "port number: 8080" 
    "number of players: 2" 
    
Example for join. 
    "port number: 8080" 
    "IP address: localhost"
```

## Authors

* **Clay Paris**
* **Serene Ghazi**
* **Austin Horjus**
* **Jonathon Carothers**

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* https://opengameart.org
* https://freesound.org