import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;

public class MainScene extends Application {
    private final int NUM_RANDOM_CELLS = 10;
    private final int WIDTH_GRID = 6;
    private final int HEIGHT_GRID = 6;
    private final int WIDTH_SCENE = 900;
    private final int HEIGHT_SCENE = 700;
    private GridPane gridPane;
    private StackPane stackPane;
    private Scene mainScene;
    private WelcomeScene welcomeScene;
    private Text statusText;


    @Override
    public void start(Stage primaryStage) {

        //Welcome scene
        welcomeScene = WelcomeScene.createWelcomeScene(WIDTH_SCENE, HEIGHT_SCENE);

        //Main Scene
        stackPane = new StackPane();
        stackPane.setStyle("-fx-background-color: linear-gradient(to right, #7e6638, #c5c524, #7e6638);");

        gridPane = new GridPane();   // for buttons
        gridPane.setPadding(new Insets(50));
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(4);
        gridPane.setVgap(4);
        StackPane.setAlignment(gridPane, Pos.CENTER);
        stackPane.getChildren().add(gridPane);

        statusText = new Text("<Let's go>\n<Click on button>");  // To report user error status or successful completion
        statusText.setFont(Font.font("Verdana", FontWeight.MEDIUM, 18));
        statusText.setFill(Color.BROWN);
        statusText.setTextAlignment(TextAlignment.CENTER);
        statusText.setStroke(Color.BLACK);
        stackPane.getChildren().add(statusText);
        StackPane.setAlignment(statusText, Pos.TOP_CENTER);

        createButtons();
        do {
            defaultGrayColor();
            randomColoring();

        } while (isNotCondition2() || isNotCondition1());

        mainScene = new Scene(stackPane, WIDTH_SCENE, HEIGHT_SCENE);

        Image image = new Image("Image.png");
        primaryStage.getIcons().add(image);
        primaryStage.setTitle("YingYang puzzle");

        primaryStage.setHeight(HEIGHT_SCENE);
        primaryStage.setWidth(WIDTH_SCENE);

        primaryStage.setScene(welcomeScene);
        primaryStage.show();


        int delaySeconds = 3;
        PauseTransition delay = new PauseTransition(Duration.seconds(delaySeconds));
        delay.setOnFinished(event -> primaryStage.setScene(mainScene));
        delay.play();

        // Resize Scene
        mainScene.widthProperty().addListener((observable, oldValue, newValue) -> resizeGridPane(mainScene.getWidth(), mainScene.getHeight()));
        mainScene.heightProperty().addListener((observable, oldValue, newValue) -> resizeGridPane(mainScene.getWidth(), mainScene.getHeight()));
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

                // checking on condition1 and condition2
                if (isNotCondition2() || isNotCondition1()) {
                    button.setStyle(colors.get(colors.size() - 2));

                    statusText.setFill(Color.RED);
                    statusText.setStroke(Color.RED);
                    statusText.setText("");
                    statusText.setText("\n! Your choice is wrong !");
                }

                // Checking for the condition that all the houses have been successfully painted
                if (isEndOfSuccess()) {
                    statusText.setFill(Color.DARKGREEN);
                    statusText.setStroke(Color.DARKGREEN);
                    statusText.setFont(Font.font(20));

                    // locking buttons
                    for (int row1 = 0; row1 < HEIGHT_GRID; row1++) {
                        for (int col1 = 0; col1 < WIDTH_GRID; col1++) {
                            Button button1 = (Button) gridPane.getChildren().get(row1 * WIDTH_GRID + col1);
                            button1.setDisable(true);
                        }
                    }
                    statusText.setText("**Congratulations, you won**");
                }
            }

            // Right click action listener
            if (event.getButton() == MouseButton.SECONDARY) {
                colors.add(button.getStyle());

                if (button.getStyle().contains("white") || button.getStyle().contains("black")) {
                    button.setStyle("-fx-background-color: gray");
                    colors.add(button.getStyle());
                }

                // checking on condition2
                if (isNotCondition2()) {
                    button.setStyle(colors.get(colors.size() - 2));

                    statusText.setFill(Color.RED);
                    statusText.setStroke(Color.RED);
                    statusText.setText("");
                    statusText.setText("\n! Your choice is wrong !");
                }

            }
        });

        // action listener of mouse is entered or exited for border of the button
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

    private boolean isNotCondition1() { // Check 2*2 squares

        for (int row = 0; row < HEIGHT_GRID - 1; row++) {
            for (int col = 0; col < WIDTH_GRID - 1; col++) {

                //get buttons of 2*2 square
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

    // check Continuity of buttons
    private boolean isNotCondition2() {

        // Collection of white or black button index
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
        HashSet<Integer> visited = new HashSet<>();  // For button saves that have already been checked
        Stack<Integer> stack = new Stack<>();  // Used to perform depth first search(DFS algorithm).

        int startButton = buttons.get(0);
        stack.push(startButton);

        while (!stack.isEmpty()) {
            int currentButton = stack.pop();
            visited.add(currentButton);

            // Getting the index of nearby buttons
            int indexUpButton = currentButton - WIDTH_GRID;
            int indexDownButton = currentButton + WIDTH_GRID;
            int indexLeftButton = currentButton - 1;
            int indexRightButton = currentButton + 1;

            // Check if any nearby button is in the same set of buttons
            if (buttons.contains(indexUpButton) && !visited.contains(indexUpButton)) stack.push(indexUpButton);
            if (buttons.contains(indexDownButton) && !visited.contains(indexDownButton)) stack.push(indexDownButton);
            if (buttons.contains(indexLeftButton) && !visited.contains(indexLeftButton) && currentButton % WIDTH_GRID != 0) stack.push(indexLeftButton);
            if (buttons.contains(indexRightButton) && !visited.contains(indexRightButton) && (currentButton + 1) % WIDTH_GRID != 0) stack.push(indexRightButton);
        }
        return visited.size() == buttons.size();
    }

    private boolean isEndOfSuccess() {

        int count = 0;  // Number of white or black button
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

    public static void main(String[] args) {
        launch(args);
    }
}