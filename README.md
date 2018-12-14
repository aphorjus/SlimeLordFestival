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
tokens per turn for that player, and can also be conquered by another player. 

#### BATTLE:

The battle state contains the actions of the game. Each Slime Lord that is part of the battle has two slime factories that produce a single slime at the start of each turn. Each Slime Lord also has between zero and 5 abilities to use in battle. The Battle state has 3 "modes" attack mode, move mode, and ability mode. Pressing the "s" key will toggle between move and attack mode, and pressing the number keys 1-5 will select Slime Lord abilities and active ability mode. The player may end their turn at any time with the "e" key.

Every slime can move and attack once every turn, and each Slime Lord can use one ability each turn. The goal of a battle is to destroy the opposing Slime Lords two factories.


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