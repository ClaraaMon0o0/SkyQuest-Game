package skyquest;

import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

class Meteor extends GameObject {
    public Meteor() {
        super("/skyquest/resources/meteor.png", 75, 75, 10 + new Random().nextDouble() * 4);
    }

    @Override
    public void spawn(AnchorPane pane, ImageView balloon, Runnable onCollision) {
        double startY = new Random().nextDouble() * (pane.getHeight() - imageView.getFitHeight());
        imageView.setLayoutX(pane.getWidth());
        imageView.setLayoutY(startY);
        pane.getChildren().add(imageView);

        TranslateTransition transition = new TranslateTransition(Duration.seconds(speed), imageView);
        transition.setByX(-pane.getWidth() - imageView.getFitWidth());
        transition.setOnFinished(e -> pane.getChildren().remove(imageView));
        transition.play();

        collisionCheck = new Timeline(new KeyFrame(Duration.millis(50), e -> {
            if (balloon.getBoundsInParent().intersects(imageView.getBoundsInParent())) {
                collisionCheck.stop(); // Stop collision detection
                pane.getChildren().remove(imageView);
                onCollision.run();
            }
        }));

        collisionCheck.setCycleCount(Timeline.INDEFINITE);
        collisionCheck.play();
    }
}
