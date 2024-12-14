package skyquest;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import static javafx.scene.input.KeyCode.UP;
import static javafx.scene.input.KeyCode.DOWN;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GameController implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView background;
    @FXML
    private ImageView balloon;
    @FXML
    private Text coinText;
    @FXML
    private Text liveText;

    private static int coins = 0;
    private static int lives = 3;

    public static int getCoins() {
        return coins;
    }

    public static int getLives() {
        return lives;
    }
    
    public static void resetGameStats() {
        coins = 0;
        lives = 3;
    }
    
    private Timeline coinTimeline;
    private Timeline meteorTimeline;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        anchorPane.setOnKeyPressed(this::movement);
        anchorPane.setOnMouseClicked(e -> anchorPane.requestFocus());
        anchorPane.requestFocus();
        Platform.runLater(() -> anchorPane.requestFocus());

        coinTimeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> spawnObject(new Coin(), this::collectCoin)));
        coinTimeline.setCycleCount(Timeline.INDEFINITE);
        coinTimeline.play();

        meteorTimeline = new Timeline(new KeyFrame(Duration.seconds(4), e -> spawnObject(new Meteor(), this::hitMeteor)));
        meteorTimeline.setCycleCount(Timeline.INDEFINITE);
        meteorTimeline.play();
    }


    @FXML
    private void movement(KeyEvent event) {
        double newX = balloon.getLayoutX();
        double newY = balloon.getLayoutY();

        switch (event.getCode()) {
            case UP:
                newY -= 10;
                break;
            case DOWN:
                newY += 10;
                break;
            default:
                return;
        }
        
        if (newX >= 0 && newX <= anchorPane.getWidth() - balloon.getFitWidth()) {
            balloon.setLayoutX(newX);
        }
        if (newY >= 0 && newY <= anchorPane.getHeight() - balloon.getFitHeight()) {
            balloon.setLayoutY(newY);
        }

    }

    private void spawnObject(GameObject gameObject, Runnable onCollision) {
        gameObject.spawn(anchorPane, balloon, onCollision);
    }

    private void collectCoin() {
        coins++;
        updateCoin();
        playSoundEffect("/skyquest/resources/coin.mp3");
    }

    private void hitMeteor() {
        lives--;
        updateLives();
        playSoundEffect("/skyquest/resources/meteor.mp3");
        if (lives <= 0) {
            endGame();
        }
    }

    private void updateCoin() {
        coinText.setText("Coins: " + coins);
    }

    private void updateLives() {
        liveText.setText("Lives: " + lives);
    }

    private void playSoundEffect(String soundPath) {
        try {
            Media sound = new Media(getClass().getResource(soundPath).toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setVolume(0.25);
            mediaPlayer.play();
        } catch (NullPointerException e) {
            showError("Sound file not found", soundPath + " is missing.");
        } catch (Exception e) {
            showError("Unexpected error", e.getMessage());
        }
    }

    private void endGame() {
        Platform.runLater(() -> {
            coinTimeline.stop();
            meteorTimeline.stop();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            alert.setContentText("Your game is over! Final coin: " + coins);

            ButtonType replayButton = new ButtonType("Play again");
            ButtonType menuButton = new ButtonType("To menu");

            alert.getButtonTypes().setAll(replayButton, menuButton);

            alert.showAndWait().ifPresent(response -> {
                if (response == replayButton) {
                    resetGame();
                } else if (response == menuButton) {
                    goToMenu();
                }
            });
        });
    }

    private void resetGame() {
        resetGameStats();
        updateCoin();
        updateLives();
        balloon.setLayoutX(41);
        balloon.setLayoutY(178);

        coinTimeline.play();
        meteorTimeline.play();

        anchorPane.requestFocus();
    }

    private void goToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Menu error", e.getMessage());
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

