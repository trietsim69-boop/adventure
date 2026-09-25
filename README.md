# Hogwarts Adventure

A text adventure game written in Scala 3 for the **Programming 1** course (O1). It builds on the course's `o1.adventure` template and turns it into a Harry Potter–themed quest.

A wizard who shall not be named has taken over Hogwarts. Explore the castle, solve the challenge in each common room, find and destroy the Horcruxes, and defeat Voldemort to save the school.

## Features

- **Nine areas**, from the Main Gate through the four house common rooms to hidden places such as the Chamber of Secrets. The hidden areas are reached through secret exits.
- **Challenges**: each common room asks a riddle or task that you must answer before you can go on. Answers start with `sir,` (for example `sir, dumbledore`), because a true wizard minds their manners.
- **Items and Horcruxes**: pick up items, examine them and destroy the Horcruxes (the cup, the diadem and the diary). The diary can only be destroyed with basilisk venom.
- **Boss fights** against the Basilisk, Nagini and Voldemort, using spells and the Sword of Gryffindor.
- **Two interfaces**: a Swing GUI window and a plain text console.

## How to win

Destroy all the Horcruxes **and** defeat Voldemort. If you beat Voldemort while a Horcrux still exists, the game tells you to go back and finish the job.

## Commands

| Command | What it does |
|---|---|
| `go <direction>` | Move to a neighbouring area (for example `go north`) |
| `get <item>` | Pick up an item by its full name (for example `get the sword of gryffindor`) |
| `examine <item>` | Read the description of an item you are carrying |
| `inventory` | List the items you are carrying |
| `destroy <item>` | Destroy a Horcrux you are carrying |
| `use <attack>` | Attack a boss: `expelliarmus`, `fireball` or `the sword` |
| `sir, <answer>` | Answer the challenge in the current room |
| `help` | Show the in-game help |
| `quit` | Give up |

Stuck? [walkthrough.txt](walkthrough.txt) has the challenge answers and the secret paths (spoilers). [map.pdf](map.pdf) shows the layout of the castle.

## Running the game

The project is an IntelliJ IDEA module ([Wizard Adventure.iml](Wizard%20Adventure.iml)) that uses Scala 3. The GUI needs the bundled [scala-swing_3-3.0.0.jar](scala-swing_3-3.0.0.jar) on the classpath.

1. Open the folder in IntelliJ IDEA with the Scala plugin installed.
2. Run one of the entry points:
   - `o1.adventure.ui.AdventureGUI` opens the game in a window.
   - `o1.adventure.ui.AdventureTextUI` runs the game in the console.

## Project structure

```
o1/adventure/
├── Adventure.scala      # Builds the world: areas, exits, items, challenges, bosses; win/lose logic
├── Area.scala           # A location with exits, items, an optional challenge and bosses
├── Action.scala         # Parses a text command and dispatches it to the player
├── Player.scala         # Player state: location, inventory, Horcruxes, combat
├── Item.scala           # An item, optionally marked as a Horcrux
├── BossFight.scala      # A boss with health that can be damaged and defeated
└── ui/
    ├── AdventureGUI.scala     # Swing user interface (from the course template)
    └── AdventureTextUI.scala  # Console user interface
doc/                     # Generated Scaladoc
map.pdf                  # Map of the game world
walkthrough.txt          # Solutions (spoilers)
```
