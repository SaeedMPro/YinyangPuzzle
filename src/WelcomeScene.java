import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

class WelcomeScene extends Scene {
    private WelcomeScene(Parent parent, double v, double v1) {
        super(parent, v, v1);

    }

    public static WelcomeScene createWelcomeScene(double v, double v1) {
        Text text = new Text("\nYingYang Puzzle");
        text.setFont(Font.font("Garamond", FontWeight.BOLD, 35));
        text.setFill(Color.WHITE);
        StackPane root = new StackPane();
        root.getChildren().add(text);
        StackPane.setAlignment(text, Pos.TOP_CENTER);

        root.setStyle("-fx-background-color: #412a2a");

        ImageView imageView = new ImageView();
        Image image = new Image("Image.png");
        imageView.setImage(image);
        imageView.setFitHeight(300);
        imageView.setFitWidth(300);
        root.getChildren().add(imageView);
        StackPane.setAlignment(imageView, Pos.CENTER);

        return new WelcomeScene(root, v, v1);
    }
}