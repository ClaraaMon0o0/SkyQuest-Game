package skyquest;

import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

abstract class GameObject {
    protected ImageView imageView;
    protected double speed;
    protected Timeline collisionCheck;

    public GameObject(String imagePath, double width, double height, double speed) {
        this.imageView = new ImageView(new Image(imagePath));
        this.imageView.setFitWidth(width);
        this.imageView.setFitHeight(height);
        this.speed = speed;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public abstract void spawn(AnchorPane pane, ImageView balloon, Runnable onCollision);
}
