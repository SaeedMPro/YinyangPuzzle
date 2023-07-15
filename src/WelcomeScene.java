import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class WelcomeScene extends Application {
    private int row;
    private int col;
    private int randomCells;
    private final int WIDTH_SCENE = 900;
    private final int HEIGHT_SCENE = 700;

    @Override
    public void start(Stage stage) {
        // Creating the title text
        Text text = new Text("\nYingYang Puzzle");
        text.setFont(Font.font("Garamond", FontWeight.BOLD, 35));
        text.setFill(Color.WHITE);

        // Creating the root StackPane and adding the title text to it
        StackPane root = new StackPane();
        root.getChildren().add(text);
        StackPane.setAlignment(text, Pos.TOP_CENTER);

        // Setting the background color of the root StackPane
        root.setStyle("-fx-background-color: #412a2a");

        // Adding the image to the root StackPane
        ImageView imageView = new ImageView();
        Image image = new Image("Image.png");
        imageView.setImage(image);
        imageView.setFitHeight(300);
        imageView.setFitWidth(300);
        root.getChildren().add(imageView);
        StackPane.setAlignment(imageView, Pos.CENTER);

        // Creating the "New Game" button
        Button newGameButton = new Button("New Game");
        newGameButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;" +
                " -fx-font-family: fantasy; -fx-font-size: 15px; -fx-padding: 10px 20px;" +
                "-fx-shape: 'M 0 50 C 0 20, 50 0, 100 0 C 150 0, 200 20, 200 50 C 200 80, 150 100, 100 100 C 50 100, 0 80, 0 50 Z';" +
                "-fx-effect: dropshadow(two-pass-box, rgba(1,1,1,1), 10, 0, 0, 10);");

        // Adding the "New Game" button to the root StackPane
        root.getChildren().add(newGameButton);
        StackPane.setAlignment(newGameButton, Pos.BOTTOM_CENTER);
        StackPane.setMargin(newGameButton, new Insets(0, 0, 100, 0));

        // Handling the "New Game" button click event
        newGameButton.setOnAction(event -> showGameSettingsDialog(stage));

        // Creating the "Recent Game" button
        Button recentGsmeButton = new Button("Recent Game");
        recentGsmeButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;" +
                " -fx-font-family: fantasy; -fx-font-size: 13px; -fx-padding: 10px 20px;" +
                "-fx-shape: 'M 0 50 C 0 20, 50 0, 100 0 C 150 0, 200 20, 200 50 C 200 80, 150 100, 100 100 C 50 100, 0 80, 0 50 Z';" +
                "-fx-effect: dropshadow(two-pass-box, rgba(1,1,1,1), 10, 0, 0, 10);");

        // Adding the "Recent Game" button to the root StackPane
        root.getChildren().add(recentGsmeButton);
        StackPane.setAlignment(recentGsmeButton, Pos.BOTTOM_CENTER);
        StackPane.setMargin(recentGsmeButton, new Insets(0, 0, 50, 0));

        // Handling the "Recent Game" button click event
        recentGsmeButton.setOnAction(event -> loadRecentGame(stage));

        // Configuring the stage
        stage.getIcons().add(image);
        stage.setTitle("YingYang puzzle");
        stage.setHeight(HEIGHT_SCENE);
        stage.setWidth(WIDTH_SCENE);

        // Creating the scene and setting it on the stage
        Scene scene = new Scene(root, WIDTH_SCENE, HEIGHT_SCENE);
        stage.setScene(scene);
        stage.show();
    }

    // Method to show the game settings dialog
    private void showGameSettingsDialog(Stage stage) {
        // Creating a dialog window to get game settings
        Alert settingsDialog = new Alert(Alert.AlertType.NONE);
        settingsDialog.setTitle("New Game");
        settingsDialog.setHeaderText("Game Settings");

        // Creating a grid pane for the settings dialog
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);

        // Creating labels and text fields for the settings
        Label rowsLabel = new Label("Rows:");
        Label colsLabel = new Label("Columns:");
        Label randomCellsLabel = new Label("Random Cells:");

        TextField rowsTextField = new TextField();
        TextField colsTextField = new TextField();
        TextField randomCellsTextField = new TextField();

        // Adding labels and text fields to the grid pane
        gridPane.addRow(0, rowsLabel, rowsTextField);
        gridPane.addRow(1, colsLabel, colsTextField);
        gridPane.addRow(2, randomCellsLabel, randomCellsTextField);

        // Setting the grid pane as the content of the dialog window
        settingsDialog.getDialogPane().setContent(gridPane);
        settingsDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Handling the result of the dialog window
        settingsDialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                row = Integer.parseInt(rowsTextField.getText());
                col = Integer.parseInt(colsTextField.getText());
                randomCells = Integer.parseInt(randomCellsTextField.getText());

                // Starting the game with the provided settings
                new MainScene(randomCells, row, col, WIDTH_SCENE, HEIGHT_SCENE).game(stage);
            }
            return null;
        });

        // Showing the settings dialog
        settingsDialog.showAndWait();
    }

    private void loadRecentGame(Stage stage) {
        File file = new File("RecentGame.txt");

        try (
                ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(file)
                )
        ) {

            Information information = (Information) objIn.readObject();
            new MainScene(information, WIDTH_SCENE, HEIGHT_SCENE).game(stage);

        } catch (IOException | ClassNotFoundException e) {
            Alert endAlert = new Alert(Alert.AlertType.ERROR);
            endAlert.setTitle("Recent Game");
            endAlert.setHeaderText("Recent game not found!");
            endAlert.setContentText("Please, Click on New Game");
            endAlert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
