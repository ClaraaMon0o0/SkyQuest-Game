package skyquest;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MenuController implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    
    private MediaPlayer mediaPlayer;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String musicFile = getClass().getResource("/skyquest/resources/backsound.mp3").toExternalForm();
        Media media = new Media(musicFile);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.075);
        mediaPlayer.play();
    }

    @FXML
    private void sceneGame(ActionEvent event) throws IOException {
        Parent gameRoot = FXMLLoader.load(getClass().getResource("Game.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(gameRoot);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void sceneAbout(ActionEvent event) throws IOException {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("SkyQuest");
        alert.setContentText(
        "🎮Cara Bermain\n" +
        "🎈 Gunakan tombol panah atas (↑) untuk naik dan panah bawah (↓) untuk turun.\n" +
        "💰 Kumpulkan koin emas untuk mendapatkan skor.\n" +
        "☄️ Hindari meteor! Jika terkena, nyawa akan berkurang.\n\n" +
        "✨ Tentang Proyek\n" +
        "Proyek ini adalah game berbasis Java menggunakan JavaFX yang menguji ketangkasan dan refleks.\n\n" +
        "🛠️Pengembang\n" +
        "Clara Monica                    2317051055\n" +
        "Indah Febriana Della        2317051066\n" +
        "Sulthon Aris Setiawan      2317051099"
        );
        alert.showAndWait();
    }      
}
