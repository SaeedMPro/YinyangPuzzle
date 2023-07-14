import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public class MainScene {
    private final int NUM_RANDOM_CELLS;
    private final int WIDTH_GRID;
    private final int HEIGHT_GRID;
    private final int WIDTH_SCENE;
    private final int HEIGHT_SCENE;
    private GridPane gridPane;
    private StackPane stackPane;
    private Scene mainScene;
    private Text statusText;

    public MainScene(int NUM_RANDOM_CELLS, int WIDTH_GRID, int HEIGHT_GRID, int WIDTH_SCENE, int HEIGHT_SCENE) {
        this.NUM_RANDOM_CELLS = NUM_RANDOM_CELLS;
        this.WIDTH_GRID = WIDTH_GRID;
        this.HEIGHT_GRID = HEIGHT_GRID;
        this.WIDTH_SCENE = WIDTH_SCENE;
        this.HEIGHT_SCENE = HEIGHT_SCENE;
    }

    public void game(Stage primaryStage) {

        // Main Scene
        stackPane = new StackPane();
        stackPane.setStyle("-fx-background-color: linear-gradient(to right, #7e6638, #c5c524, #7e6638);");

        gridPane = new GridPane();   // for buttons
        gridPane.setPadding(new Insets(50));
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(4);
        gridPane.setVgap(4);
        StackPane.setAlignment(gridPane, Pos.CENTER);
        stackPane.getChildren().add(gridPane);

        statusText = new Text("<Let's go>\n<Click on a button>");  // To report user error status or successful completion
        statusText.setFont(Font.font("Verdana", FontWeight.MEDIUM, 18));
        statusText.setFill(Color.BROWN);
        statusText.setTextAlignment(TextAlignment.CENTER);
        statusText.setStroke(Color.BLACK);
        stackPane.getChildren().add(statusText);
        StackPane.setAlignment(statusText, Pos.TOP_CENTER);

        // Creating the "Save Game" button
        Button saveButton = new Button("Save Game");
        saveButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;" +
                " -fx-font-family: fantasy; -fx-font-size: 15px; -fx-padding: 10px 20px;" +
                "-fx-shape: 'M 0 50 C 0 20, 50 0, 100 0 C 150 0, 200 20, 200 50 C 200 80, 150 100, 100 100 C 50 100, 0 80, 0 50 Z';" +
                "-fx-effect: dropshadow(one-pass-box, rgba(1,1,1,1), 10, 0, 0, 10);");
        stackPane.getChildren().add(saveButton);
        StackPane.setAlignment(saveButton, Pos.BOTTOM_CENTER);
        StackPane.setMargin(saveButton, new Insets(0, 0, 10, 0));
        saveButton.setOnAction(event -> saveToFile());

        mainScene = new Scene(stackPane, WIDTH_SCENE, HEIGHT_SCENE);

        createButtons();
        do {
            defaultGrayColor();
            randomColoring();
        } while (isNotCondition2() || isNotCondition1());

        // Resize Scene
        mainScene.widthProperty().addListener((observable, oldValue, newValue) -> resizeGridPane(mainScene.getWidth(), mainScene.getHeight()));
        mainScene.heightProperty().addListener((observable, oldValue, newValue) -> resizeGridPane(mainScene.getWidth(), mainScene.getHeight()));

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private void createButtons() {

        for (int row = 0; row < HEIGHT_GRID; row++) {
            for (int col = 0; col < WIDTH_GRID; col++) {
                Button button = new Button();
                button.setFocusTraversable(false);
                button.setPrefSize(60, 60);
                button.setStyle("-fx-background-color: gray");

                gridPane.add(button, col, row);
                addActionListener(row * WIDTH_GRID + col);
            }
        }
    }

    private void addActionListener(int index) {
        Button button = (Button) gridPane.getChildren().get(index);
        ArrayList<String> colors = new ArrayList<>(); // Collection of history colors
        colors.add("-fx-background-color: gray"); // Default

        // Click action listener
        button.setOnMouseClicked((MouseEvent event) -> {

            colors.add(button.getStyle());
            statusText.setText("");

            // Left click action listener
            if (event.getButton() == MouseButton.PRIMARY) {

                if (button.getStyle().contains("white")) {
                    button.setStyle("-fx-background-color: black");
                    colors.add(button.getStyle());

                } else if (button.getStyle().contains("black")) {
                    button.setStyle("-fx-background-color: white");
                    colors.add(button.getStyle());

                } else {
                    button.setStyle("-fx-background-color: white");
                    colors.add(button.getStyle());
                }

                // checking condition 1 and condition 2
                if (isNotCondition2() || isNotCondition1()) {
                    button.setStyle(colors.get(colors.size() - 2));

                    statusText.setFill(Color.RED);
                    statusText.setStroke(Color.RED);
                    statusText.setText("");
                    statusText.setText("\n! Your choice is wrong !");
                }

                // Checking for the condition that all the houses have been successfully painted
                if (isEndOfSuccess()) {
                    Alert endAlert = new Alert(Alert.AlertType.INFORMATION);

                    // locking buttons
                    for (int row1 = 0; row1 < HEIGHT_GRID; row1++) {
                        for (int col1 = 0; col1 < WIDTH_GRID; col1++) {
                            Button button1 = (Button) gridPane.getChildren().get(row1 * WIDTH_GRID + col1);
                            button1.setDisable(true);
                        }
                    }


                    endAlert.setTitle("End of game");
                    endAlert.setHeaderText("...Congratulations, you won...");
                    endAlert.setContentText("Click OK to finish the game.");
                    Optional<ButtonType> result = endAlert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        Platform.exit();
                    }

                }
            }

            // Right click action listener
            if (event.getButton() == MouseButton.SECONDARY) {
                colors.add(button.getStyle());

                if (button.getStyle().contains("white") || button.getStyle().contains("black")) {
                    button.setStyle("-fx-background-color: gray");
                    colors.add(button.getStyle());
                }

                // checking condition 2
                if (isNotCondition2()) {
                    button.setStyle(colors.get(colors.size() - 2));

                    statusText.setFill(Color.RED);
                    statusText.setStroke(Color.RED);
                    statusText.setText("");
                    statusText.setText("\n! Your choice is wrong !");
                }

            }
        });

        // action listener for mouse enter or exit to show/hide button borders
        button.setOnMouseEntered(event -> button.setBorder(new Border(new BorderStroke(Color.DARKGREEN, BorderStrokeStyle.DOTTED,
                CornerRadii.EMPTY, new BorderWidths(5)))));

        button.setOnMouseExited(event -> button.setBorder(new Border(new BorderStroke(Color.DARKGREEN, BorderStrokeStyle.DOTTED,
                CornerRadii.EMPTY, new BorderWidths(0)))));

    }

    private void defaultGrayColor() {

        for (int row = 0; row < HEIGHT_GRID; row++) {
            for (int col = 0; col < WIDTH_GRID; col++) {
                Button button = (Button) gridPane.getChildren().get(row * WIDTH_GRID + col);
                button.setStyle("-fx-background-color: gray");
            }
        }
    }

    private void randomColoring() {
        Random random = new Random();

        while (true) {
            int row = random.nextInt(HEIGHT_GRID);
            int col = random.nextInt(WIDTH_GRID);
            Button button = (Button) gridPane.getChildren().get(row * WIDTH_GRID + col); // get the random button

            int randomColor = (int) (Math.random() * 2);
            if (randomColor == 0) {
                button.setStyle("-fx-background-color: white");
            } else {
                button.setStyle("-fx-background-color: black");
            }

            int count = 0;
            for (int row1 = 0; row1 < HEIGHT_GRID; row1++) {
                for (int col1 = 0; col1 < WIDTH_GRID; col1++) {
                    Button button1 = (Button) gridPane.getChildren().get(row1 * WIDTH_GRID + col1);
                    if (button1.getStyle().contains("white") || button1.getStyle().contains("black"))
                        count++;
                }
            }

            if (count == NUM_RANDOM_CELLS)
                break;
        }

    }

    private boolean isNotCondition1() { // Check 2x2 squares

        for (int row = 0; row < HEIGHT_GRID - 1; row++) {
            for (int col = 0; col < WIDTH_GRID - 1; col++) {

                // get buttons of 2x2 square
                Button button1 = (Button) gridPane.getChildren().get(row * WIDTH_GRID + col);
                Button button2 = (Button) gridPane.getChildren().get(row * WIDTH_GRID + (col + 1));
                Button button3 = (Button) gridPane.getChildren().get((row + 1) * WIDTH_GRID + col);
                Button button4 = (Button) gridPane.getChildren().get((row + 1) * WIDTH_GRID + (col + 1));

                if (button1.getStyle().contains("white") || button1.getStyle().contains("black"))
                    if (
                            button1.getStyle().equals(button2.getStyle())
                                    && button2.getStyle().equals(button3.getStyle())
                                    && button3.getStyle().equals(button4.getStyle())
                    ) {
                        return true;
                    }
            }
        }
        return false;
    }

    // Check continuity of buttons
    private boolean isNotCondition2() {

        // Collection of white or black button indices
        ArrayList<Integer> whiteButtons = new ArrayList<>();
        ArrayList<Integer> blackButtons = new ArrayList<>();

        for (int row = 0; row < HEIGHT_GRID; row++) {
            for (int col = 0; col < WIDTH_GRID; col++) {
                Button button1 = (Button) gridPane.getChildren().get(row * WIDTH_GRID + col);

                if (button1.getStyle().contains("white")) {
                    whiteButtons.add(row * WIDTH_GRID + col);
                } else if (button1.getStyle().contains("black")) {
                    blackButtons.add(row * WIDTH_GRID + col);
                }
            }
        }

        if (whiteButtons.size() > 0 && blackButtons.size() > 0) {
            boolean isWhiteConnected = checkConnectivity(whiteButtons);
            boolean isBlackConnected = checkConnectivity(blackButtons);

            return !(isWhiteConnected && isBlackConnected);
        } else {
            return true;
        }
    }

    private boolean checkConnectivity(ArrayList<Integer> buttons) {
        HashSet<Integer> visited = new HashSet<>();  // Buttons that have already been checked
        Stack<Integer> stack = new Stack<>();  // Used to perform depth-first search (DFS).

        int startButton = buttons.get(0);
        stack.push(startButton);

        while (!stack.isEmpty()) {
            int currentButton = stack.pop();
            visited.add(currentButton);

            // Get the indices of neighboring buttons
            int indexUpButton = currentButton - WIDTH_GRID;
            int indexDownButton = currentButton + WIDTH_GRID;
            int indexLeftButton = currentButton - 1;
            int indexRightButton = currentButton + 1;

            // Check if any neighboring button is in the same set of buttons
            if (buttons.contains(indexUpButton) && !visited.contains(indexUpButton)) stack.push(indexUpButton);
            if (buttons.contains(indexDownButton) && !visited.contains(indexDownButton)) stack.push(indexDownButton);
            if (buttons.contains(indexLeftButton) && !visited.contains(indexLeftButton) && currentButton % WIDTH_GRID != 0)
                stack.push(indexLeftButton);
            if (buttons.contains(indexRightButton) && !visited.contains(indexRightButton) && (currentButton + 1) % WIDTH_GRID != 0)
                stack.push(indexRightButton);
        }
        return visited.size() == buttons.size();
    }

    private boolean isEndOfSuccess() {

        int count = 0;  // Number of white or black buttons
        for (int row = 0; row < HEIGHT_GRID; row++) {
            for (int col = 0; col < WIDTH_GRID; col++) {
                Button button = (Button) gridPane.getChildren().get(row * WIDTH_GRID + col);
                if (button.getStyle().contains("white") || button.getStyle().contains("black")) {
                    count++;
                }
            }
        }
        return count == (WIDTH_GRID * HEIGHT_GRID);
    }

    private void resizeGridPane(double width, double height) {

        double buttonSize = Math.min(width / 6, height / 6); // Sizing buttons based on scene size
        stackPane.setPrefSize(width, height);
        gridPane.getChildren().forEach(node -> {
            Button button = (Button) node;
            button.setPrefSize(buttonSize, buttonSize);
        });
    }

    private void saveToFile() {

        // Collection of white or black button indices
        ArrayList<Integer> white = new ArrayList<>();
        ArrayList<Integer> black = new ArrayList<>();

        for (int row = 0; row < HEIGHT_GRID; row++) {
            for (int col = 0; col < WIDTH_GRID; col++) {
                Button button1 = (Button) gridPane.getChildren().get(row * WIDTH_GRID + col);

                if (button1.getStyle().contains("white")) {
                    white.add(row * WIDTH_GRID + col);
                } else if (button1.getStyle().contains("black")) {
                    black.add(row * WIDTH_GRID + col);
                }
            }
        }
        Information information = new Information(white, black, HEIGHT_GRID, WIDTH_GRID);
        File file = new File("RecentGame.txt");
        try (
                ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(file)
                )
        ) {
            objOut.writeObject(information);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
