## YingYang Puzzle Game

**Introduction**

YingYang Puzzle is a simple JavaFX-based puzzle game where you need to color the buttons in a grid according to specific rules. The objective of the game is to color the buttons in such a way that two conditions are met:
1. No 2x2 square in the grid should have all buttons of the same color.
2. The white and black buttons should form connected regions in the grid.

**Game Rules**
- Left-clicking on a button will toggle its color between white and black.
- Right-clicking on a button will reset its color to gray.
- You win the game when all the buttons are either white or black, and both conditions mentioned above are met.

**Features**
- New Game: Start a new game and set the grid dimensions and the number of initial random cells through a settings dialog.
- Recent Game: Load the most recent saved game and continue from where you left off.
- Save Game: Save the current game state and continue later from the "Recent Game" button.

**JavaFX**
The game's user interface and graphical components are developed using JavaFX, a powerful framework for building desktop applications and games in Java. JavaFX provides a rich set of features for creating interactive and visually appealing GUI applications, making it well-suited for creating games like YingYang Puzzle. In this game, we utilize JavaFX's features for:
- Creating and managing the main scene layout using StackPane and GridPane.
- Displaying buttons, images, and text elements using JavaFX controls.
- Handling user interactions such as button clicks and mouse events with JavaFX event listeners.
- Dynamically resizing the grid and adapting the scene layout to window size changes.
- Utilizing JavaFX alerts for providing feedback and messages to the player.

**Getting Started**
1. Clone the repository: `git clone https://github.com/your-username/YingYang-Puzzle.git`
2. Open the project in your preferred Java IDE.
3. Build and run the project.

**How to Play**
1. Click on "New Game" to start a new game.
2. Enter the number of rows, columns, and the number of random cells in the settings dialog, then click "OK."
3. The game will start, and you can left-click on the buttons to color them black or white or right-click to reset them to gray.
4. Follow the game rules to color the buttons in a way that meets both conditions mentioned above.
5. Once you have completed the puzzle, an alert will congratulate you on your success.

**Controls**
- Left-click: Color the buttons black or white.
- Right-click: Reset the color to gray.
- "New Game": Start a new game and specify the grid dimensions and the number of random cells.
- "Recent Game": Load the most recent saved game and continue from where you left off.
- "Save Game": Save the current game state and continue later from the "Recent Game" button.

**Technical Details**
- The game is developed using Java and JavaFX.
- The grid layout is created using the GridPane layout manager.
- The game state is serialized and saved to a file named "RecentGame.txt" using the Information class.
- The game's GUI is created using JavaFX's StackPane and Button controls.
- The game features event listeners to handle button clicks and mouse enter/exit events.
- The game dynamically resizes the grid to fit the window size.

**Requirements**
- Java 8 or higher must be installed to run the game.
- JavaFX

**How to Run**
1. Compile the Java files.
2. Run the "WelcomeScene" class.

**Credits**
This game was created by [Your Name] as a personal project.

Enjoy the YingYang Puzzle Game! If you encounter any issues or have suggestions for improvements, feel free to contact [Your Contact Information].
